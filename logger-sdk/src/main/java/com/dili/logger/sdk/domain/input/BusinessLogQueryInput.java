package com.dili.logger.sdk.domain.input;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.logger.sdk.domain.BusinessLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 17:13
 */
public class BusinessLogQueryInput extends BusinessLog implements Serializable {

    private static final long serialVersionUID = -84299292110016376L;

    /**
     * 日志开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    /**
     * 日志结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    /**
     * 所属市场集合
     */
    private Set<Long> marketIdSet;

    /**
     * 根据操作类型集，查询业务日志信息
     */
    private Set<String> operationTypeSet;

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
    public Set<String> getOperationTypeSet() {
        return operationTypeSet;
    }
    public void setOperationTypeSet(Set<String> operationTypeSet) {
        this.operationTypeSet = operationTypeSet;
    }
}
