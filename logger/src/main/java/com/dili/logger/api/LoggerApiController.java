package com.dili.logger.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.logger.domain.OperationLog;
import com.dili.logger.domain.input.OperationLogQuery;
import com.dili.logger.service.OperationLogService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.sid.util.IdUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/11 14:19
 */
@RestController
@RequestMapping("/api/logger")
public class LoggerApiController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/operationLog/save", method = {RequestMethod.POST})
    public BaseOutput save(@RequestBody(required = false) OperationLogQuery condition) {
        List<OperationLog> logList = Lists.newArrayList();
        for (int i = 1; i <= condition.getRows(); i++) {
            OperationLog temp = new OperationLog();
            temp.setId(IdUtils.nextId());
            temp.setBusinessId(System.currentTimeMillis());
            temp.setBusinessType("ia");
            temp.setOperationType("created");
            temp.setNotes("这里是备注。。。" + i + "....");
            JSONObject object = new JSONObject();
            object.put("num", i);
            object.put("content", "这是第" + i + "个内容");
            temp.setContent(object.toJSONString());
            temp.setOperatorId(Long.valueOf(i));
            temp.setCreateTime(LocalDateTime.now().minusMinutes(i));
            temp.setMarketId(1L);
            logList.add(temp);
        }
        operationLogService.batchSave(logList);
        return BaseOutput.success();
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/operationLog/listPage", method = {RequestMethod.POST})
    public PageOutput listPage(@RequestBody(required = false) OperationLogQuery condition) {
        return operationLogService.searchPage(condition);
    }

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/operationLog/list", method = {RequestMethod.POST})
    public BaseOutput list(@RequestBody(required = false) OperationLogQuery condition) {
        return operationLogService.list(condition);
    }


    /**
     * 删除对应的所有数据
     *
     * @return
     */
    @RequestMapping(value = "/operationLog/deleteAll", method = {RequestMethod.POST})
    public BaseOutput deleteAll() {
        operationLogService.deleteAll();
        return BaseOutput.success();
    }

}
