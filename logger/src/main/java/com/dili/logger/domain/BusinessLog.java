package com.dili.logger.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/10 21:23
 */
@Getter
@Setter
@ToString(callSuper = true)
@Document(indexName = "dili-business-logger")
public class BusinessLog extends BaseLog implements Serializable {

    private static final long serialVersionUID = 7077180344239431179L;

    /**
     * 操作类型
     */
    @Field(type = FieldType.Keyword)
    private String operationType;

    /**
     * 冗余存储操作类型的显示值
     */
    @Field(type = FieldType.Text)
    private String operationTypeText;

}
