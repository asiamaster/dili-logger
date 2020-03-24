package com.dili.logger.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <B>异常日志记录</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 11:49
 */
@Getter
@Setter
@ToString(callSuper = true)
@Document(indexName = "dili-exception-logger", type = "exceptionLog")
public class ExceptionLog extends BaseLog implements Serializable {

    private static final long serialVersionUID = 5204495241923955295L;

    /**
     * 异常类型
     */
    @Field(type = FieldType.Keyword)
    private String exceptionType;

    /**
     * 冗余存储异常类型的显示值
     */
    @Field(type = FieldType.Text, index = false)
    private String exceptionTypeText;

}
