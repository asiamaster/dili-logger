package com.dili.logger.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Like;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/23 11:34
 */
@Entity
@Table(name = "classify_value")
@Data
public class ClassifyValue extends BaseDomain {

    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属客户
     * {@link com.dili.logger.enums.LoggerClassify}
     */
    @Column(name = "`classify`", updatable = false)
    @NotNull(message = "所属类型不能为空")
    private Integer classify;

    /**
     * 所属市场
     */
    @Column(name = "`code`")
    @NotNull(message = "编码不能为空")
    @Size(max = 50,message = "类型编码不能超过50个字符")
    private String code;

    /**
     * 姓名
     */
    @Column(name = "`value`")
    @NotBlank(message = "类型值不能为空")
    @Size(max = 100,message = "类型值请保持在40个字符以内")
    @Like
    private String value;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`",updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    @Column(name = "`creator_id`",updatable = false)
    private Long creatorId;

    /**
     * 修改人
     */
    @Column(name = "`modifier_id`")
    private Long modifierId;

}
