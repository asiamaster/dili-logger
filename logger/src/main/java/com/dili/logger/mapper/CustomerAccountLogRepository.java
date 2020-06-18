package com.dili.logger.mapper;

import com.dili.logger.domain.CustomerAccountLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 10:44
 */
public interface CustomerAccountLogRepository extends ElasticsearchRepository<CustomerAccountLog,Long> {
}
