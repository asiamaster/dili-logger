package com.dili.logger.sdk.domain.input;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.ss.dto.IBaseDomain;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Long> marketIdList;

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

    public List<Long> getMarketIdList() {
        return marketIdList;
    }
    public void setMarketIdList(List<Long> marketIdList) {
        this.marketIdList = marketIdList;
    }
}
