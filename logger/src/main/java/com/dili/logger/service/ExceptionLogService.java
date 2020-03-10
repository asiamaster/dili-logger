package com.dili.logger.service;

import com.dili.logger.domain.ExceptionLog;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 17:07
 */
public interface ExceptionLogService {

    /**
     * 分页查询操作日志信息
     * 默认：Elasticsearch中from + size must be less than or equal to: [10000]
     * @param condition 条件
     * @return
     */
    PageOutput<List<ExceptionLog>> searchPage(ExceptionLogQueryInput condition);

    /**
     * 根据条件查询操作日志
     * @param condition 查询条件
     * @return
     */
    BaseOutput<List<ExceptionLog>> list(ExceptionLogQueryInput condition);

    /**
     * 保存操作日志信息
     * @param log 日志对象
     */
    void save(ExceptionLog log);

    /**
     * 批量保存操作日志
     * @param logList
     */
    void batchSave(List<ExceptionLog> logList);

    /**
     * 删除对应的所有数据
     */
    void deleteAll();
}
