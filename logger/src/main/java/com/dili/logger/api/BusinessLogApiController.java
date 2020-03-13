package com.dili.logger.api;

import com.dili.logger.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.service.BusinessLogService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/businessLog")
public class BusinessLogApiController {

    @Autowired
    private BusinessLogService businessLogService;

    /**
     * 保存业务日志数据
     *
     * @param condition
     * @return
     */
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/save",method = {RequestMethod.POST})
    public BaseOutput save(@RequestBody BusinessLog condition) {
        businessLogService.save(condition);
        return BaseOutput.success();
    }

    /**
     * 批量保存业务日志信息
     * @param businessLogList
     * @return
     */
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/batchSave",method = {RequestMethod.POST})
    public BaseOutput batchSave(@RequestBody List<BusinessLog> businessLogList) {
        businessLogService.batchSave(businessLogList);
        return BaseOutput.success();
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage", method = {RequestMethod.POST})
    public PageOutput listPage(@RequestBody(required = false) BusinessLogQueryInput condition) {
        return businessLogService.searchPage(condition);
    }

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public BaseOutput list(@RequestBody(required = false) BusinessLogQueryInput condition) {
        return businessLogService.list(condition);
    }


    /**
     * 删除对应的所有数据
     *
     * @return
     */
    @RequestMapping(value = "/deleteAll", method = {RequestMethod.POST})
    public BaseOutput deleteAll() {
        businessLogService.deleteAll();
        return BaseOutput.success();
    }
}
