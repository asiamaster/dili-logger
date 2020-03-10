package com.dili.logger.controller;

import com.dili.logger.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.service.BusinessLogService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/2 10:26
 */
@Controller
@RequestMapping("/businessLog")
public class BusinessLogController {

    @Autowired
    private BusinessLogService businessLogService;

    /**
     * 跳转到个人客户管理页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "business/index";
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.POST})
    @ResponseBody
    public String listPage(BusinessLogQueryInput condition) throws Exception {
        PageOutput<List<BusinessLog>> pageOutput = businessLogService.searchPage(condition);
        List<BusinessLog> operationLogList = pageOutput.getData();
        if (null == operationLogList) {
            operationLogList = Lists.newArrayList();
        }
        List results = true ? ValueProviderUtils.buildDataByProvider(condition, operationLogList) : operationLogList;
        return new EasyuiPageOutput(pageOutput.getTotal(), results).toString();
    }
}
