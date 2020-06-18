package com.dili.logger.sdk.domain.input;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.logger.sdk.domain.CustomerAccountLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/17 17:39
 */
public class CustomerAccountLogQueryInput extends CustomerAccountLog implements Serializable {

    /**
     * 日志开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    /**
     * 日志结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    /**
     * 所属市场集合
     */
    private Set<Long> marketIdSet;

    public LocalDateTime getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(LocalDateTime createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public LocalDateTime getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(LocalDateTime createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Set<Long> getMarketIdSet() {
        return marketIdSet;
    }

    public void setMarketIdSet(Set<Long> marketIdSet) {
        this.marketIdSet = marketIdSet;
    }
}
