package com.dili.logger.sdk.aop;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LogBuilder;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.ParamErrorException;
import com.dili.ss.util.BeanConver;
import com.dili.ss.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 日志切面
 */
@Component
@Aspect
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class LoggerAspect {

    @Resource(name="StringGroupTemplate")
    GroupTemplate groupTemplate;

    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

    /**
     * 设置token
     * @param point
     * @return
     * @throws Throwable
     */
    @Around( "@annotation(com.dili.logger.sdk.annotation.BusinessLogger)")
    public Object businessLogAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        //当前线程是否包含Request，如果有，则说明在外层已经放入了Request，不需要清除ThreadLocal中的缓存
//        boolean containsRequest = false;
//        if (LoggerContext.getRequest() != null) {
//            containsRequest = true;
//        } else {
            LoggerContext.put(request);
//        }
        Object retValue = null;
        //单独try/catch处理业务执行，业务异常时不记日志
        try {
            //先执行方法
            retValue = point.proceed();
            //如果是BaseOutput.failure()，则不输出日志
            if (retValue instanceof BaseOutput && !((BaseOutput) retValue).isSuccess()) {
                return retValue;
            }
        } catch (Exception e) {
            //如果当前线程是最外层AOP，则需要清除ThreadLocal缓存
//            if (!containsRequest) {
                LoggerContext.resetLocal();
//            }
            throw e;
        }
        //单独try/catch处理日志，日志异常不会异常正常业务回滚，日志发送异常则返回空
        try {
            rabbitMQMessageService.send(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE, LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, JSON.toJSONString(getBusinessLog(point)));
            return retValue;
        } catch (Exception e) {
            LOGGER.error(String.format("日志发送异常:%s", e.getMessage()), e);
            return retValue;
        } finally {
            //如果当前线程是最外层AOP，则需要清除ThreadLocal缓存
//            if (!containsRequest) {
                LoggerContext.resetLocal();
//            }
        }
    }

    /**
     * 根据logBuilder和BusinessLogger注解获取BusinessLog
     * @param point
     * @return
     */
    private BusinessLog getBusinessLog(ProceedingJoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        //获取注解中的信息
        BusinessLogger businessLogger = method.getAnnotation(BusinessLogger.class);
        String logBuilderName = businessLogger.logBuilder();
        LogBuilder logBuilder = null;
        BusinessLog businessLog = new BusinessLog();
        try {
            logBuilder = getObj(logBuilderName, LogBuilder.class);
            businessLog = (BusinessLog) logBuilder.build(method, point.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果未显示的给业务类型赋值，则从注解中再拿取一次
        if (StringUtils.isBlank(businessLog.getBusinessType())) {
            businessLog.setBusinessType(businessLogger.businessType());
        }
        //如果未显示的给操作类型赋值，则从注解中再拿取一次
        if (StringUtils.isBlank(businessLog.getOperationType())) {
            businessLog.setOperationType(businessLogger.operationType());
        }
        //如果未显示给日志内容赋值，则从注解中获取一次
        if (StringUtils.isBlank(businessLog.getContent())) {
            businessLog.setContent(getBeetlContent(method, point.getArgs(), LoggerContext.getRequest(), businessLogger.content()));
        }
        if (StringUtils.isBlank(businessLog.getNotes())) {
            businessLog.setNotes(businessLogger.notes());
        }
        if (StringUtils.isBlank(businessLog.getSystemCode())) {
            businessLog.setSystemCode(businessLogger.systemCode());
        }
        if (businessLog.getCreateTime() == null) {
            businessLog.setCreateTime(LocalDateTime.now());
        }
        return businessLog;
    }

    /**
     * 获取beetl模板内容
     * @param method
     * @param args
     * @param templateStr
     * @return
     */
    private String getBeetlContent(Method method, Object[] args, HttpServletRequest request, String templateStr){
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(StringUtils.isBlank(templateStr)){
//            LOGGER.warn("日志模板为空");
            return null;
        }
        Template template = groupTemplate.getTemplate(templateStr);
        //获取http请求参数
        Map<String, Object> params = getParameterMap(request);

        Map<String, Object> all = LoggerContext.getAll();
        if (!CollectionUtils.isEmpty(all)) {
            params.putAll(all);
        }

        try {
            //获取模板绑定变量
            params.putAll(getBindingMap(parameterAnnotations, parameterTypes, args));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        //绑定参数中的变量
        template.binding(params);
        //模板检验失败则打印日志
        BeetlException exception = template.validate();
        if(exception != null){
            LOGGER.error(exception.getMessage());
            return null;
        }
        return template.render();
    }

    /**
     * 转换request.getParameterMap()为Map<String, Object>
     * @param request
     * @return
     */
    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, Object> returnMap = new HashMap<>();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry<String, String[]> entry;
        String name = null;
        Object value = null;
        while (entries.hasNext()) {
            entry = (Map.Entry<String, String[]>) entries.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                if(values != null && values.length == 1){
                    value = values[0];
                }else{
                    value = values;
                }
            }else{
                value = valueObj;
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 获取模板绑定变量
     * 先取@RequestParam注解，没有注解取Map或DTO的字段，List没有注解默认key为list
     * 没有注解，又不是Map,List或DTO， 直接按参数顺序为key，如args[0],args[1]
     * @param parameterAnnotations
     * @param parameterTypes
     * @param args
     * @return
     * @throws Exception
     */
    private Map<String, Object> getBindingMap(Annotation[][] parameterAnnotations, Class<?>[] parameterTypes, Object[] args) throws Exception {
        //临时记录绑定变量
        Map<String, Object> params = new HashMap<>();
        //循环设置每个方法形参的绑定变量
        for(int i = 0; i < parameterAnnotations.length; i++){
            Annotation[] annotations = parameterAnnotations[i];
            for(Annotation annotation : annotations){
                if(annotation instanceof RequestParam){
                    String bindName = ((RequestParam) annotation).value();
                    bindName = StringUtils.isBlank(bindName) ? ((RequestParam) annotation).name() : bindName;
                    //List和数组如果没有bindName，默认为list
                    if(List.class.isAssignableFrom(parameterTypes[i]) || parameterTypes[i].isArray()){
                        if(StringUtils.isBlank(bindName)){
                            bindName = "list";
                        }
                        params.put(bindName, args[i]);
                    }
                    //DTO、JavaBean和Map可以没有bindName
                    else if(StringUtils.isBlank(bindName) && IDTO.class.isAssignableFrom(parameterTypes[i])){
                        params.putAll(BeanConver.transformObjectToMap(args[i]));
                    }else if(StringUtils.isBlank(bindName) && Map.class.isAssignableFrom(parameterTypes[i])){
                        params.putAll((Map)args[i]);
                    }else{
                        if(StringUtils.isBlank(bindName)){
                            bindName = "args["+i+"]";
                        }
                        if(null != args[i]) {
                            params.put(bindName, args[i]);
                        }
                    }
                }else{
                    //List和数组如果没有bindName，默认为list
                    if(List.class.isAssignableFrom(parameterTypes[i]) || parameterTypes[i].isArray()){
                        params.put("list", args[i]);
                    }
                    //DTO、JavaBean和Map可以没有bindName
                    else if(IDTO.class.isAssignableFrom(parameterTypes[i])){
                        params.putAll(BeanConver.transformObjectToMap(args[i]));
                    }else if(Map.class.isAssignableFrom(parameterTypes[i])){
                        params.putAll((Map)args[i]);
                    }else{
                        String bindName = "args["+i+"]";
                        if(null != args[i]) {
                            params.put(bindName, args[i]);
                        }
                    }
                }
            }
        }
        return params;
    }

    /**
     * 根据名称取对象
     * 反射或取spring bean
     * @param objName
     * @return
     */
    private <T> T getObj(String objName, Class<T> clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException, BeansException {
        if(objName.contains(".")){
            Class objClass = Class.forName(objName);
            if(clazz.isAssignableFrom(objClass)){
                return (T) objClass.newInstance();
            }
            throw new ParamErrorException(objName + "不是" + clazz.getName() +"的实例");
        }else{
            T bean = null;
            try {
                //这里可能bean不存在，会抛异常
                bean = SpringUtil.getBean(objName, clazz);
            } catch (BeansException e) {
                throw e;
            }
            if(clazz.isAssignableFrom(bean.getClass())){
                return bean;
            }
            throw new ParamErrorException(objName + "不是" + clazz.getName() +"的实例");
        }
    }

}
