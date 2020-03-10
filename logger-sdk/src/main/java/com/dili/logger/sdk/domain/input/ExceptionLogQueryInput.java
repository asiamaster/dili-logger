package com.dili.logger.sdk.domain.input;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.ExceptionLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 17:35
 */
public class ExceptionLogQueryInput extends ExceptionLog implements Serializable {

    private static final long serialVersionUID = -8064052229044638090L;

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
}
