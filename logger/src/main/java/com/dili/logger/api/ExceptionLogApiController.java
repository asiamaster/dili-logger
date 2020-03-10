package com.dili.logger.api;

import com.dili.logger.domain.BusinessLog;
import com.dili.logger.domain.ExceptionLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.logger.service.BusinessLogService;
import com.dili.logger.service.ExceptionLogService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/11 14:19
 */
@RestController
@RequestMapping("/api/exceptionLog")
public class ExceptionLogApiController {

    @Autowired
    private ExceptionLogService exceptionLogService;

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public BaseOutput save(@RequestBody ExceptionLog condition) {
        exceptionLogService.save(condition);
        return BaseOutput.success();
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage", method = {RequestMethod.POST})
    public PageOutput listPage(@RequestBody(required = false) ExceptionLogQueryInput condition) {
        return exceptionLogService.searchPage(condition);
    }

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public BaseOutput list(@RequestBody(required = false) ExceptionLogQueryInput condition) {
        return exceptionLogService.list(condition);
    }


    /**
     * 删除对应的所有数据
     *
     * @return
     */
    @RequestMapping(value = "/deleteAll", method = {RequestMethod.POST})
    public BaseOutput deleteAll() {
        exceptionLogService.deleteAll();
        return BaseOutput.success();
    }
}