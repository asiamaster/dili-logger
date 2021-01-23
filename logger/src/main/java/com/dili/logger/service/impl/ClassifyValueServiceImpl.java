package com.dili.logger.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.logger.domain.ClassifyValue;
import com.dili.logger.mapper.ClassifyValueMapper;
import com.dili.logger.service.ClassifyValueService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/23 14:56
 */
@RequiredArgsConstructor
@Service
public class ClassifyValueServiceImpl extends BaseServiceImpl<ClassifyValue, Long> implements ClassifyValueService {

    public ClassifyValueMapper getActualMapper() {
        return (ClassifyValueMapper)getDao();
    }

    @Override
    public Optional<String> saveData(ClassifyValue classifyValue) {
        ClassifyValue query = new ClassifyValue();
        query.setClassify(classifyValue.getClassify());
        query.setCode(classifyValue.getCode());
        List<ClassifyValue> valueList = list(query);
        classifyValue.setModifyTime(LocalDateTime.now());
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        classifyValue.setModifierId(userTicket.getId());
        if (Objects.isNull(classifyValue.getId())) {
            if (CollectionUtil.isNotEmpty(valueList)) {
                return Optional.of("对应编码已存在,请勿重复添加");
            }
            classifyValue.setCreateTime(classifyValue.getModifyTime());
            classifyValue.setCreatorId(classifyValue.getModifierId());
        } else {
            long count = valueList.stream().filter(t -> t.getCode().trim().equalsIgnoreCase(classifyValue.getCode().trim()) && !t.getId().equals(classifyValue.getId())).count();
            if (count > 0) {
                return Optional.of("对应编码已存在,请勿重复添加");
            }
        }
        this.saveOrUpdateSelective(classifyValue);
        return Optional.empty();
    }

    @Override
    public List<ClassifyValue> getByClassify(Integer classify) {
        if (Objects.isNull(classify)) {
            return Collections.emptyList();
        }
        ClassifyValue query = new ClassifyValue();
        query.setClassify(classify);
        return getActualMapper().select(query);
    }

    @Override
    public Optional<ClassifyValue> getByClassifyAndCode(Integer classify, String code) {
        if (Objects.isNull(classify) || StrUtil.isBlank(code)) {
            return Optional.empty();
        }
        ClassifyValue query = new ClassifyValue();
        query.setClassify(classify);
        query.setCode(code);
        return getActualMapper().select(query).stream().findFirst();
    }
}
