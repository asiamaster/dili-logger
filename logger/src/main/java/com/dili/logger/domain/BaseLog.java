package com.dili.logger.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 16:54
 */
@Getter
@Setter
@ToString
public class BaseLog implements Serializable {
    private static final long serialVersionUID = -4958046077120852893L;

    @Id
    private Long id;

    /**
     * 所属市场
     */
    @Field(type = FieldType.Long)
    private Long marketId;

    /**
     * 系统编码
     */
    @Field(type = FieldType.Keyword)
    private String systemCode;

    /**
     * 业务类型
     */
    @Field(type = FieldType.Keyword)
    private String businessType;

    /**
     * 业务ID
     */
    @Field(type = FieldType.Long)
    private Long businessId;

    /**
     * 业务编码
     */
    @Field(type = FieldType.Keyword)
    private String businessCode;

    /**
     * 日志类容
     */
    @Field(type = FieldType.Text, index = false)
    private String content;

    /**
     * 备注说明
     */
    @Field(type = FieldType.Text, index = false)
    private String notes;

    /**
     * 客户端请求IP
     */
    @Field(type = FieldType.Text, index = false)
    private String remoteIp;

    /**
     * 操作人ID
     */
    @Field(type = FieldType.Long)
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @Field(type = FieldType.Text,index = false)
    private String operatorName;

    /**
     * 日志时间
     */
    @Field(type = FieldType.Date)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
