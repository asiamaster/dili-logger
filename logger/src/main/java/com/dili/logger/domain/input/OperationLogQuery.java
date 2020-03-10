package com.dili.logger.domain.input;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.logger.domain.OperationLog;
import com.dili.ss.dto.IBaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/10 22:14
 */
@Getter
@Setter
@ToString(callSuper = true)
public class OperationLogQuery extends OperationLog implements IBaseDomain {

    private static final long serialVersionUID = 8121371919114545125L;
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
     * 页码，从1开始
     */
    private Integer page;

    /**
     * 每页行数
     */
    private Integer rows;

    /**
     * 排序字段，以逗号分隔
     */
    private String sort;

    /**
     * 排序类型: asc,desc
     */
    private String order;

    /**
     * 自定义的元数据信息
     */
    private Map metadata;

    @Override
    public Object getMetadata(String key) {
        return metadata == null ? null : metadata.get(key);
    }

    @Override
    public void setMetadata(String key, Object value){
        if(metadata == null){
            metadata = new HashMap();
        }
        metadata.put(key, value);
    }

    /**
     * 附加属性中是否存在
     * @param key
     * @return
     */
    @Override
    public Boolean containsMetadata(String key) {
        return metadata == null ? false : metadata.containsKey(key);
    }
}
