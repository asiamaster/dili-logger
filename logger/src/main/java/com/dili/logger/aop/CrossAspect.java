package com.dili.logger.aop;

import com.dili.ss.domain.BaseOutput;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <B>Cross跨域请求拦截器</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/12 14:22
 */
@Aspect
@Component
@Slf4j
public class CrossAspect {

    /**
     *允许跨域访问的地址
     */
    @Value("#{'${dili.logger.cross.allowedOrigin}'.split(',')}")
    private String[] allowedOrigins;

    /**
     * 设置token
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(org.springframework.web.bind.annotation.CrossOrigin)")
    public Object businessLogAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String referer = request.getHeader("Referer");
        String requestOrigin = request.getHeader("Origin");
        int end = requestOrigin.indexOf(".com");
        if (end < 0) {
            log.warn("已拦截源地址[{}]的调用日志请求", referer);
            return BaseOutput.failure("请求域不被支持");
        }
        requestOrigin = requestOrigin.substring(0, end + 4);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        Boolean allowed = false;
        for (String allowedOrigin : allowedOrigins) {
            //推荐方式:正则  注意(CrossOrigin(origins = {"*.abc.com"}) ) 主域会匹配主域+子域   origins = {"*.pay.abc.com"} 子域名只会匹配子域
            if (pathMatcher.isPattern(allowedOrigin) && pathMatcher.match(allowedOrigin, requestOrigin)) {
                allowed = true;
                break;
            }
        }
        if (allowed) {
            log.debug("已允许源地址[{}]的调用日志请求", referer);
            //先执行方法
            Object retValue = point.proceed();
            return retValue;
        } else {
            log.warn("已拦截源地址[{}]的调用日志请求", referer);
            return BaseOutput.failure("请求域不被支持");
        }
    }
}
