package com.dili.logger.sdk.rpc;

import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.CustomerAccountLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.domain.input.CustomerAccountLogQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <B>客户账户操作记录RPC</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/17 17:52
 */
@FeignClient(name = "dili-logger", contextId = "customerAccountLogRpc")
public interface CustomerAccountLogRpc {
    /**
     * 保存单个业务日志信息
     * @param condition
     * @param referer 请求header中的referer,用作域名解析拦截判断
     * @return
     */
    @RequestMapping(value = "/api/customerAccountLog/save", method = {RequestMethod.POST})
    BaseOutput save(CustomerAccountLog condition, @RequestHeader("Referer") String referer);

    /**
     * 批量保存业务日志数据
     * @param customerAccountLogList
     * @param referer 请求header中的referer,用于域名解析拦截判断
     * @return
     */
    @RequestMapping(value = "/api/customerAccountLog/batchSave", method = {RequestMethod.POST})
    BaseOutput batchSave(List<CustomerAccountLog> customerAccountLogList, @RequestHeader("Referer") String referer);

    /**
     * 获取客户列表信息
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/customerAccountLog/listPage", method = RequestMethod.POST)
    PageOutput<List<CustomerAccountLog>> listPage(CustomerAccountLogQueryInput condition);

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/customerAccountLog/list", method = {RequestMethod.POST})
    BaseOutput<List<CustomerAccountLog>> list(CustomerAccountLogQueryInput condition);
}
