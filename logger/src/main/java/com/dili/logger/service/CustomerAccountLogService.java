package com.dili.logger.service;

import com.dili.logger.domain.CustomerAccountLog;
import com.dili.logger.sdk.domain.input.CustomerAccountLogQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 10:35
 */
public interface CustomerAccountLogService<T> {

    /**
     * 分页查询客户资金日志信息
     * 默认：Elasticsearch中from + size must be less than or equal to: [10000]
     * @param condition 条件
     * @return
     */
    PageOutput<List<CustomerAccountLog>> searchPage(CustomerAccountLogQueryInput condition);

    /**
     * 根据条件查询客户资金操作日志
     * @param condition 查询条件
     * @return
     */
    BaseOutput<List<CustomerAccountLog>> list(CustomerAccountLogQueryInput condition);

    /**
     * 保存客户资金日志信息
     * @param log 日志对象
     */
    void save(CustomerAccountLog log);

    /**
     * 批量保存日志
     * @param logList
     */
    void batchSave(List<CustomerAccountLog> logList);

    /**
     * 删除对应的所有数据
     */
    void deleteAll();
}
