package com.dili.logger.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.logger.component.ElasticsearchUtil;
import com.dili.logger.config.ESConfig;
import com.dili.logger.domain.BusinessLog;
import com.dili.logger.domain.ClassifyValue;
import com.dili.logger.enums.LoggerClassify;
import com.dili.logger.repository.BusinessLogRepository;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.service.BusinessLogService;
import com.dili.logger.service.ClassifyValueService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/10 18:12
 */
@RequiredArgsConstructor
@Service
public class BusinessLogServiceImpl extends BaseLogServiceImpl<BusinessLog> implements BusinessLogService<BusinessLog> {

    private static final IndexCoordinates index = IndexCoordinates.of("dili-business-logger");

    private final BusinessLogRepository businessLogRepository;
    private final ElasticsearchUtil elasticsearchUtil;
    private final ClassifyValueService classifyValueService;

    //高亮线上的前缀标签
    String preTag = "<font color=\"#dd4b39\"><strong>";
    //高亮线上的后缀标签
    String postTag = "</strong></font>";

    private String highlight_content = "content";

    @Override
    public PageOutput<List<BusinessLog>> searchPage(BusinessLogQueryInput condition) {
        // 创建对象
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        QueryBuilder queryBuilder = produceQuery(condition);
        searchQuery.withQuery(queryBuilder);
        searchQuery.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
        searchQuery.withHighlightFields(new HighlightBuilder.Field(highlight_content).preTags(preTag).postTags(postTag));
        //当前页码
        Integer pageNum = 1;
        //是否需要滚动查询
        Boolean scrollSearch = false;
        //是否超出size限制
        Boolean overrun = false;
        /**
         * 由于es 默认最大允许size有限，所以需要针对size进行数据转换操作
         */
        if (Objects.nonNull(condition.getRows()) && condition.getRows() > 0) {
            //ES 默认size 有限制，超过配置，则会直接报错,所以需要特殊处理
            if (condition.getRows().compareTo(Integer.valueOf(ESConfig.getMaxSize())) > 0) {
                overrun = true;
            }
            //传入的页码是否为空
            if (Objects.nonNull(condition.getPage())) {
                //如果是查询第一页，而且 size 超限，则需要滚动查询
                if (condition.getPage() == 1) {
                    //ES size 有限制，超过配置，则会直接报错
                    if (overrun) {
                        scrollSearch = true;
                    }
                } else {
                    if (overrun) {
                        return PageOutput.failure("分页-每页显示条数过多，暂且不支持");
                    }
                }
                pageNum = condition.getPage();
            } else {
                if (overrun) {
                    scrollSearch = true;
                }
            }
        } else {
            //如果未传入rows，因为es默认查询10条，所有，则认为查询所有(es允许的单页最大值)
            condition.setRows(ESConfig.getMaxSize());
        }
        searchQuery.withPageable(PageRequest.of(pageNum - 1, condition.getRows()));
        return elasticsearchUtil.searchData(scrollSearch, searchQuery.build(), BusinessLog.class, index);
    }

    @Override
    public BaseOutput<List<BusinessLog>> list(BusinessLogQueryInput condition) {
        return BaseOutput.success().setData(this.searchPage(condition).getData());
    }

    @Override
    public void save(BusinessLog log) {
        if (StrUtil.isBlank(log.getOperationTypeText())) {
            Optional<ClassifyValue> byClassifyAndCode = classifyValueService.getByClassifyAndCode(LoggerClassify.BUSINESS.getCode(), log.getOperationType());
            if (byClassifyAndCode.isPresent()) {
                log.setOperationTypeText(byClassifyAndCode.get().getValue());
            }
        }
        if (Objects.isNull(log.getCreateTime())) {
            log.setCreateTime(LocalDateTime.now());
        }
        //由日志系统生成统一的ID，忽略客户传入的ID
        log.setId(IdUtil.getSnowflake(1, 1).nextId());
        businessLogRepository.save(log);
    }

    @Override
    public void batchSave(List<BusinessLog> logList) {
        if (CollectionUtil.isNotEmpty(logList)) {
            List<ClassifyValue> byClassify = classifyValueService.getByClassify(LoggerClassify.BUSINESS.getCode());
            Map<String, ClassifyValue> operationTypeMap = StreamEx.of(byClassify).toMap(ClassifyValue::getCode, t -> t, (v1, v2) -> v1);
            logList.forEach(l -> {
                //由日志系统生成统一的ID，忽略客户传入的ID
                l.setId(IdUtil.getSnowflake(1, 1).nextId());
                if (Objects.isNull(l.getCreateTime())) {
                    l.setCreateTime(LocalDateTime.now());
                }
                if (StrUtil.isBlank(l.getOperationTypeText())) {
                    l.setOperationTypeText(operationTypeMap.containsKey(l.getOperationType()) ? operationTypeMap.get(l.getOperationType()).getValue() : "");
                }
            });
            businessLogRepository.saveAll(logList);
        }
    }

    @Override
    public void deleteAll() {
        businessLogRepository.deleteAll();
    }

    /**
     * 构造查询条件
     *
     * @param condition 根据传入参数，构造查询条件
     * @return
     */
    private QueryBuilder produceQuery(BusinessLogQueryInput condition) {
        // 在queryBuilder对象中自定义查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (Objects.nonNull(condition)) {
            if (Objects.nonNull(condition.getSystemCode())) {
                queryBuilder.must(QueryBuilders.termQuery("systemCode", condition.getSystemCode()));
            }
            if (Objects.nonNull(condition.getOperatorId())) {
                queryBuilder.must(QueryBuilders.termQuery("operatorId", condition.getOperatorId()));
            }
            if (Objects.nonNull(condition.getBusinessId())) {
                queryBuilder.must(QueryBuilders.termQuery("businessId", condition.getBusinessId()));
            }
            if (StrUtil.isNotBlank(condition.getBusinessType())) {
                queryBuilder.must(QueryBuilders.termQuery("businessType", condition.getBusinessType()));
            }
            if (Objects.nonNull(condition.getCreateTimeStart())) {
                queryBuilder.filter(QueryBuilders.rangeQuery("createTime").gte(condition.getCreateTimeStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            }
            if (Objects.nonNull(condition.getCreateTimeEnd())) {
                queryBuilder.filter(QueryBuilders.rangeQuery("createTime").lte(condition.getCreateTimeEnd().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            }
            if (StrUtil.isNotBlank(condition.getBusinessCode())) {
                queryBuilder.must(QueryBuilders.termQuery("businessCode", condition.getBusinessCode()));
            }
            if (StrUtil.isNotBlank(condition.getOperationType())) {
                queryBuilder.must(QueryBuilders.termQuery("operationType", condition.getOperationType()));
            }
            if (Objects.nonNull(condition.getMarketId())) {
                queryBuilder.must(QueryBuilders.termQuery("marketId", condition.getMarketId()));
            }
            if (CollectionUtil.isNotEmpty(condition.getMarketIdSet())) {
                queryBuilder.filter(QueryBuilders.termsQuery("marketId", condition.getMarketIdSet()));
            }
            if (CollectionUtil.isNotEmpty(condition.getOperationTypeSet())) {
                queryBuilder.filter(QueryBuilders.termsQuery("operationType", condition.getOperationTypeSet()));
            }
            if (StrUtil.isNotBlank(condition.getContent())) {
                queryBuilder.filter(QueryBuilders.matchPhrasePrefixQuery("content", condition.getContent()));
            }
        }
        return queryBuilder;
    }


    /**
     * 使用beanutils工具给对象的属性赋值
     *
     * @param result
     * @param highlightFields
     * @param <T>
     */
    private <T> void populateHighLightedFields(T result, Map<String, List<String>> highlightFields) {
        highlightFields.forEach((k, v) -> {
            try {
                PropertyUtils.setProperty(result, k, v.get(0));
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
//                throw new ElasticsearchException("failed to set highlighted value for field: " + field.getName()
//                        + " with value: " + Arrays.toString(field.getFragments()), e);
            }
        });
    }
}
