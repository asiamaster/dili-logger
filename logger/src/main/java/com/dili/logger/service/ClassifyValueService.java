package com.dili.logger.service;

import com.dili.logger.domain.ClassifyValue;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/23 14:56
 */
public interface ClassifyValueService extends BaseService<ClassifyValue, Long> {

    /**
     * 保存信息
     * @param classifyValue
     * @return
     */
    Optional<String> saveData(ClassifyValue classifyValue);

    /**
     * 根据类型，获取对应的值配置
     * @param classify
     * @return
     */
    List<ClassifyValue> getByClassify(Integer classify);


    /**
     * 根据类型及编码，获取唯一的值配置
     * @param classify
     * @param code     编码
     * @return
     */
    Optional<ClassifyValue> getByClassifyAndCode(Integer classify, String code);
}
