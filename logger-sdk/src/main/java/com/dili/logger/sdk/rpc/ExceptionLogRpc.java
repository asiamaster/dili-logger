package com.dili.logger.sdk.rpc;

import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 15:16
 */
@FeignClient(name = "dili-logger", contextId = "exceptionLogRpc")
public interface ExceptionLogRpc {

    /**
     * 保存单个异常日志数据
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/exceptionLog/save", method = {RequestMethod.POST})
    BaseOutput save(ExceptionLog condition, @RequestHeader("Referer") String referer);

    /**
     * 批量保存异常日志数据
     * @param exceptionLogList
     * @return
     */
    @RequestMapping(value = "/api/exceptionLog/batchSave", method = {RequestMethod.POST})
    BaseOutput batchSave(List<ExceptionLog> exceptionLogList, @RequestHeader("Referer") String referer);

    /**
     * 获取客户列表信息
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/exceptionLog/listPage", method = RequestMethod.POST)
    PageOutput<List<ExceptionLog>> listPage(ExceptionLogQueryInput condition);

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/exceptionLog/list", method = {RequestMethod.POST})
    BaseOutput list(ExceptionLogQueryInput condition);
}
