package com.dili.logger.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.logger.domain.OperationLog;
import com.dili.logger.domain.input.OperationLogQuery;
import com.dili.logger.service.OperationLogService;
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
import java.util.Map;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/2 10:26
 */
@Controller
@RequestMapping("/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 跳转到个人客户管理页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "operation/index";
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.POST})
    @ResponseBody
    public String listPage(OperationLogQuery condition) throws Exception {
        PageOutput<List<OperationLog>> pageOutput = operationLogService.searchPage(condition);
        List<OperationLog> operationLogList = pageOutput.getData();
        if (null == operationLogList) {
            operationLogList = Lists.newArrayList();
        }
        List results = true ? ValueProviderUtils.buildDataByProvider(condition, operationLogList) : operationLogList;
        return new EasyuiPageOutput(pageOutput.getTotal(), results).toString();
    }
}
