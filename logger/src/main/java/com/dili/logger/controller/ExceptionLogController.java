package com.dili.logger.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.dili.logger.domain.ClassifyValue;
import com.dili.logger.domain.ExceptionLog;
import com.dili.logger.enums.LoggerClassify;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.logger.service.ClassifyValueService;
import com.dili.logger.service.ExceptionLogService;
import com.dili.logger.service.remote.FirmRpcService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/2 10:26
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/exceptionLog")
public class ExceptionLogController {

    private final ExceptionLogService exceptionLogService;
    private final FirmRpcService firmRpcService;
    private final ClassifyValueService classifyValueService;

    /**
     * 跳转到异常日志管理页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Calendar end = CalendarUtil.endOfDay(Calendar.getInstance());
        modelMap.put("createTimeEnd", DateUtil.format(end.getTime(), "yyyy-MM-dd HH:mm:ss"));
        end.add(Calendar.MONTH, -1);
        end.add(Calendar.DATE, 1);
        Calendar begin = CalendarUtil.beginOfDay(end);
        modelMap.put("createTimeStart", DateUtil.format(begin.getTime(), "yyyy-MM-dd HH:mm:ss"));
        List<ClassifyValue> classifyValueList = classifyValueService.getByClassify(LoggerClassify.EXCEPTION.getCode());
        modelMap.put("exceptionTypeList", classifyValueList);
        return "exception/index";
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.POST})
    @ResponseBody
    public String listPage(ExceptionLogQueryInput condition) {
        try {
            if (Objects.isNull(condition.getMarketId())) {
                List<Firm> userFirms = firmRpcService.getCurrentUserFirms();
                if (CollectionUtil.isEmpty(userFirms)) {
                    return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
                } else {
                    Set<Long> idSet = userFirms.stream().map(Firm::getId).collect(Collectors.toSet());
                    condition.setMarketIdSet(idSet);
                }
            }
            PageOutput<List<ExceptionLog>> pageOutput = exceptionLogService.searchPage(condition);
            List<ExceptionLog> exceptionLogList = pageOutput.getData();
            if (null == exceptionLogList) {
                exceptionLogList = Lists.newArrayList();
            }
            List results = true ? ValueProviderUtils.buildDataByProvider(condition, exceptionLogList) : exceptionLogList;
            return new EasyuiPageOutput(pageOutput.getTotal(), results).toString();
        } catch (Exception e) {
            log.error(String.format("异常日志数据【%s】查询异常:%s", JSONUtil.toJsonStr(condition), e.getMessage()), e);
            return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
        }
    }
}
