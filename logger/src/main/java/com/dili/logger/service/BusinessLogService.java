package com.dili.logger.service;

import com.dili.logger.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/10 18:03
 */
public interface BusinessLogService<T> extends BaseLogService<T> {

    /**
     * 分页查询操作日志信息
     * 默认：Elasticsearch中from + size must be less than or equal to: [10000]
     * @param condition 条件
     * @return
     */
    PageOutput<List<BusinessLog>> searchPage(BusinessLogQueryInput condition);

    /**
     * 根据条件查询操作日志
     * @param condition 查询条件
     * @return
     */
    BaseOutput<List<BusinessLog>> list(BusinessLogQueryInput condition);

    /**
     * 保存操作日志信息
     * @param log 日志对象
     */
    void save(BusinessLog log);

    /**
     * 批量保存操作日志
     * @param logList
     */
    void batchSave(List<BusinessLog> logList);

    /**
     * 删除对应的所有数据
     */
    void deleteAll();
}
