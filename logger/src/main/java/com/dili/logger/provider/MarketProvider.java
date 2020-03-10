package com.dili.logger.provider;

import com.dili.logger.service.remote.FirmRpcService;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/3 14:54
 */
@Component
@Scope("prototype")
public class MarketProvider extends BatchDisplayTextProviderSupport {

    @Autowired
    private FirmRpcService firmRpcService;

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        List<Firm> firmList = firmRpcService.getCurrentUserFirms();
        if (CollectionUtils.isNotEmpty(firmList)) {
            return firmList.stream().filter(Objects::nonNull).map(f -> {
                ValuePairImpl<?> vp = new ValuePairImpl<>(f.getName(), f.getCode());
                return vp;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        if (relationIds != null) {
            List<Long> idList = relationIds.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(t -> Long.valueOf(t))
                    .collect(Collectors.toList());
            if (!idList.isEmpty()) {
                FirmDto firmDto = DTOUtils.newDTO(FirmDto.class);
                firmDto.setIdList(idList);
                return firmRpcService.listByExample(firmDto);
            }
        }
        return null;
    }

    @Override
    protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
        BatchProviderMeta batchProviderMeta = DTOUtils.newInstance(BatchProviderMeta.class);
        //设置主DTO和关联DTO需要转义的字段名
        batchProviderMeta.setEscapeFiled("name");
        //忽略大小写关联
        batchProviderMeta.setIgnoreCaseToRef(true);
        //主DTO与关联DTO的关联(java bean)属性(外键)
        batchProviderMeta.setFkField("marketId");
        //关联(数据库)表的主键的字段名，默认取id
        batchProviderMeta.setRelationTablePkField("id");
        //当未匹配到数据时，返回的值
        batchProviderMeta.setMismatchHandler(t -> "");
        return batchProviderMeta;
    }
}
