package com.dili.logger.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dili.logger.config.ESConfig;
import com.dili.logger.domain.BusinessLog;
import com.dili.logger.domain.ExceptionLog;
import com.dili.logger.mapper.ExceptionLogRepository;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.logger.service.ExceptionLogService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.sid.util.IdUtils;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 17:48
 */
@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public PageOutput<List<ExceptionLog>> searchPage(ExceptionLogQueryInput condition) {
        // 创建对象
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        QueryBuilder queryBuilder = produceQuery(condition);
        searchQuery.withQuery(queryBuilder);
        searchQuery.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        //是否需要循环遍历pageNum次轮询
        Boolean isGetAll = false;
        //当前页码
        Integer pageNum = 1;
        /**
         * 由于es 默认最大允许size有限，所以需要针对size进行数据转换操作
         */
        if (Objects.nonNull(condition.getRows()) && condition.getRows() > 0) {
            //传入的页码是否为空
            boolean b = Objects.isNull(condition.getPage());
            if (b) {
                if (condition.getRows().compareTo(Integer.valueOf(ESConfig.getMaxSize())) > 0) {
                    pageNum = (int) Math.ceil((double) condition.getRows() / (double) ESConfig.getMaxSize());
                    condition.setRows(ESConfig.getMaxSize());
                }
                isGetAll = true;
            } else {
                //ES 默认size 是10000，超过配置，则会直接报错
                if (condition.getRows().compareTo(Integer.valueOf(ESConfig.getMaxSize())) > 0) {
                    return PageOutput.failure("分页-每页显示条数过多，暂且不支持");
                }
                pageNum = condition.getPage();
            }
            searchQuery.withPageable(PageRequest.of(pageNum - 1, condition.getRows()));
        }
        ScrolledPage<BusinessLog> pageInfo = elasticsearchRestTemplate.startScroll(5000, searchQuery.build(), BusinessLog.class);
        if (0 == pageInfo.getTotalElements()) {
            return PageOutput.success();
        }
        PageOutput output = PageOutput.success();
        output.setPages(pageInfo.getTotalPages());
        output.setPageNum(pageNum).setTotal(Long.valueOf(pageInfo.getTotalElements()).intValue());
        List<BusinessLog> allData = Lists.newArrayList();
        allData.addAll(pageInfo.getContent());
        for (int i = 0; i < pageNum - 1; i++) {
            pageInfo = elasticsearchRestTemplate.continueScroll(pageInfo.getScrollId(), 5000, BusinessLog.class);
            if (isGetAll) {
                allData.addAll(pageInfo.getContent());
            }
        }
        //释放es服务器资源
        elasticsearchRestTemplate.clearScroll(pageInfo.getScrollId());
        output.setData(isGetAll ? allData : pageInfo.getContent());
        return output;
    }

    @Override
    public BaseOutput<List<ExceptionLog>> list(ExceptionLogQueryInput condition) {
        return BaseOutput.success().setData(this.searchPage(condition).getData());
    }

    @Override
    public void save(ExceptionLog log) {
        if (Objects.isNull(log.getId())) {
            log.setId(IdUtils.nextId());
        }
        if (Objects.isNull(log.getCreateTime())) {
            log.setCreateTime(LocalDateTime.now());
        }
        exceptionLogRepository.save(log);
    }

    @Override
    public void batchSave(List<ExceptionLog> logList) {
        exceptionLogRepository.saveAll(logList);
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
                queryBuilder.must(QueryBuilders.rangeQuery("createTime").gte(condition.getCreateTimeStart()));
            }
            if (Objects.nonNull(condition.getCreateTimeEnd())) {
                queryBuilder.must(QueryBuilders.rangeQuery("createTime").lte(condition.getCreateTimeEnd()));
            }
            if (StrUtil.isNotBlank(condition.getBusinessCode())){
                queryBuilder.must(QueryBuilders.termQuery("businessCode", condition.getBusinessCode()));
            }
            if (StrUtil.isNotBlank(condition.getExceptionType())){
                queryBuilder.must(QueryBuilders.termQuery("exceptionType", condition.getExceptionType()));
            }
        }
        return queryBuilder;
    }
}
