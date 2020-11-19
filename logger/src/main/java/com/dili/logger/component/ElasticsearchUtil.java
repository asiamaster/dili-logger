package com.dili.logger.component;

import com.dili.ss.domain.PageOutput;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/17 18:03
 */
@RequiredArgsConstructor
@Component
public class ElasticsearchUtil {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 根据条件查询数据
     * @param scrollSearch 是否需要游标滚动查询
     * @param query        查询条件
     * @param clazz        结果mapping 类
     * @param index        索引对象
     * @param <T>
     * @return
     */
    public <T> PageOutput<List<T>> searchData(Boolean scrollSearch, Query query, Class<T> clazz, IndexCoordinates index) {
        PageOutput output = PageOutput.success();
        Pageable pageable = query.getPageable();
        List<T> businessLogList = Lists.newArrayList();
        if (scrollSearch) {
            SearchScrollHits<T> scrollHits = elasticsearchRestTemplate.searchScrollStart(5000L, query, clazz, index);
            output.setTotal(scrollHits.getTotalHits());
            //保存游标ID，以便于统一清除
            List<String> scrollIdList = Lists.newArrayList();
            String scrollId = scrollHits.getScrollId();
            scrollIdList.add(scrollId);
            while (scrollHits.hasSearchHits()) {
                scrollHits.getSearchHits().forEach(t -> {
                    T result = t.getContent();
                    populateHighLightedFields(result, t.getHighlightFields());
                    businessLogList.add(result);
                });
                scrollId = scrollHits.getScrollId();
                scrollIdList.add(scrollId);
                scrollHits = elasticsearchRestTemplate.searchScrollContinue(scrollId, 1000, clazz, index);
            }
            elasticsearchRestTemplate.searchScrollClear(scrollIdList);
        } else {
            SearchHits<T> searchHits = elasticsearchRestTemplate.search(query, clazz);
            if (0 == searchHits.getTotalHits() || searchHits.getSearchHits().size() == 0) {
                return output;
            }
            output.setPageNum(pageable.getPageNumber() + 1).setTotal(searchHits.getTotalHits());
            int pageCount = (output.getTotal().intValue() + pageable.getPageSize() - 1) / pageable.getPageSize();
            output.setPages(pageCount);
            searchHits.getSearchHits().forEach(t -> {
                T result = t.getContent();
                populateHighLightedFields(result, t.getHighlightFields());
                businessLogList.add(result);
            });
            output.setData(businessLogList);
        }
        return output;
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
