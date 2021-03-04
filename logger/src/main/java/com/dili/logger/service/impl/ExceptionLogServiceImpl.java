package com.dili.logger.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.logger.component.ElasticsearchUtil;
import com.dili.logger.config.ESConfig;
import com.dili.logger.domain.ClassifyValue;
import com.dili.logger.domain.ExceptionLog;
import com.dili.logger.enums.LoggerClassify;
import com.dili.logger.repository.ExceptionLogRepository;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.logger.service.ClassifyValueService;
import com.dili.logger.service.ExceptionLogService;
import com.dili.logger.utils.LogAppUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 17:48
 */
@RequiredArgsConstructor
@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {

    private static final IndexCoordinates index = IndexCoordinates.of("dili-exception-logger");

    private final ExceptionLogRepository exceptionLogRepository;
    private final ElasticsearchUtil elasticsearchUtil;
    private final ClassifyValueService classifyValueService;

    @Override
    public PageOutput<List<ExceptionLog>> searchPage(ExceptionLogQueryInput condition) {
        // 创建对象
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        QueryBuilder queryBuilder = produceQuery(condition);
        searchQuery.withQuery(queryBuilder);
        searchQuery.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
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
        return elasticsearchUtil.searchData(scrollSearch, searchQuery.build(), ExceptionLog.class, index);
    }

    @Override
    public BaseOutput<List<ExceptionLog>> list(ExceptionLogQueryInput condition) {
        return BaseOutput.success().setData(this.searchPage(condition).getData());
    }

    @Override
    public void save(ExceptionLog log) {
        if (StrUtil.isBlank(log.getExceptionTypeText())) {
            Optional<ClassifyValue> byClassifyAndCode = classifyValueService.getByClassifyAndCode(LoggerClassify.EXCEPTION.getCode(), log.getExceptionType());
            if (byClassifyAndCode.isPresent()) {
                log.setExceptionTypeText(byClassifyAndCode.get().getValue());
            }
        }
        log.setId(IdUtil.getSnowflake(1, 1).nextId());
        if (Objects.isNull(log.getCreateTime())) {
            log.setCreateTime(LocalDateTime.now());
        }
        log.setRemoteIp(LogAppUtil.cutFirstIp(log.getRemoteIp()));
        log.setServerIp(LogAppUtil.cutFirstIp(log.getServerIp()));
        exceptionLogRepository.save(log);
    }

    @Override
    public void batchSave(List<ExceptionLog> logList) {
        if (CollectionUtil.isNotEmpty(logList)) {
            List<ClassifyValue> byClassify = classifyValueService.getByClassify(LoggerClassify.EXCEPTION.getCode());
            Map<String, ClassifyValue> exceptionTypeMap = StreamEx.of(byClassify).toMap(ClassifyValue::getCode, t -> t, (v1, v2) -> v1);
            logList.forEach(l -> {
                l.setId(IdUtil.getSnowflake(1, 1).nextId());
                if (Objects.isNull(l.getCreateTime())) {
                    l.setCreateTime(LocalDateTime.now());
                }
                if (StrUtil.isBlank(l.getExceptionTypeText())) {
                    l.setExceptionTypeText(exceptionTypeMap.containsKey(l.getExceptionType()) ? exceptionTypeMap.get(l.getExceptionType()).getValue() : "");
                }
                l.setRemoteIp(LogAppUtil.cutFirstIp(l.getRemoteIp()));
                l.setServerIp(LogAppUtil.cutFirstIp(l.getServerIp()));
            });
            exceptionLogRepository.saveAll(logList);
        }
    }

    @Override
    public void deleteAll() {
        exceptionLogRepository.deleteAll();
    }

    /**
     * 构造查询条件
     *
     * @param condition 根据传入参数，构造查询条件
     * @return
     */
    private QueryBuilder produceQuery(ExceptionLogQueryInput condition) {
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
                queryBuilder.filter(QueryBuilders.rangeQuery("createTime").gte(condition.getCreateTimeStart()));
            }
            if (Objects.nonNull(condition.getCreateTimeEnd())) {
                queryBuilder.filter(QueryBuilders.rangeQuery("createTime").lte(condition.getCreateTimeEnd()));
            }
            if (StrUtil.isNotBlank(condition.getBusinessCode())){
                queryBuilder.must(QueryBuilders.termQuery("businessCode", condition.getBusinessCode()));
            }
            if (StrUtil.isNotBlank(condition.getExceptionType())){
                queryBuilder.must(QueryBuilders.termQuery("exceptionType", condition.getExceptionType()));
            }
            if (Objects.nonNull(condition.getMarketId())) {
                queryBuilder.must(QueryBuilders.termQuery("marketId", condition.getMarketId()));
            }
            if (CollectionUtil.isNotEmpty(condition.getMarketIdSet())) {
                queryBuilder.filter(QueryBuilders.termsQuery("marketId", condition.getMarketIdSet()));
            }
            if (CollectionUtil.isNotEmpty(condition.getExceptionTypeSet())) {
                queryBuilder.filter(QueryBuilders.termsQuery("exceptionType", condition.getExceptionTypeSet()));
            }
            if (StrUtil.isNotBlank(condition.getContent())){
                queryBuilder.filter(QueryBuilders.matchPhrasePrefixQuery("content", condition.getContent()));
            }
        }
        return queryBuilder;
    }
}
