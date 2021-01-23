package com.dili.logger.provider;

import com.dili.logger.enums.LoggerClassify;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/23 15:09
 */
@Component
public class LoggerClassifyProvider implements ValueProvider {
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return Stream.of(LoggerClassify.values())
                .map(e -> new ValuePairImpl<>(e.getValue(), String.valueOf(e.getCode())))
                .collect(Collectors.toList());
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        if (Objects.isNull(val)){
            return null;
        }
        LoggerClassify instance = LoggerClassify.getInstance(Integer.valueOf(val.toString()));
        if (Objects.nonNull(instance)) {
            return instance.getValue();
        }
        return null;
    }
}
