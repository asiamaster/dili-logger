package com.dili.logger.sdk.rpc;

import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
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
@FeignClient(name = "dili-logger")
public interface BusinessLogRpc {

    /**
     * 保存单个业务日志信息
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/businessLog/save", method = {RequestMethod.POST})
    BaseOutput save(BusinessLog condition);

    /**
     * 批量保存业务日志数据
     * @param businessLogList
     * @return
     */
    @RequestMapping(value = "/api/businessLog/batchSave", method = {RequestMethod.POST})
    BaseOutput batchSave(List<BusinessLog> businessLogList);

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @RequestMapping(value = "/api/businessLog/listPage", method = RequestMethod.POST)
    PageOutput<List<BusinessLog>> listPage(BusinessLogQueryInput customer);

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/businessLog/list", method = {RequestMethod.POST})
    BaseOutput list(BusinessLogQueryInput condition);
}
