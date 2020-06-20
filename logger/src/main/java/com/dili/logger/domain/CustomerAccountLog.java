package com.dili.logger.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * <B>客户账户操作记录</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/17 17:03
 */
@Getter
@Setter
@ToString(callSuper = true)
@Document(indexName = "dili-customer_account-logger", type = "customerAccountLog")
public class CustomerAccountLog extends BaseLog {

    /**
     * 业务卡号
     */
    @Field(type = FieldType.Keyword)
    private String cardNo;

    /**
     * 客户ID
     */
    @Field(type = FieldType.Long)
    private Long customerId;

    /**
     * 客户名称
     */
    @Field(type = FieldType.Text, index = false)
    private String customerName;

    /**
     * 资金项目ID
     */
    @Field(type = FieldType.Long)
    private Long fundItemId;
    /**
     * 资金项目名称
     */
    @Field(type = FieldType.Text, index = false)
    private String fundItemName;
    /**
     * 初始金额
     */
    @Field(type = FieldType.Long)
    private Long InitialAmount;
    /**
     * 交易金额
     */
    @Field(type = FieldType.Long)
    private Long tradeAmount;
    /**
     * 交易后余额
     */
    @Field(type = FieldType.Long)
    private Long balance;
    /**
     * 资金类型ID
     */
    @Field(type = FieldType.Long)
    private Long fundTypeId;
    /**
     * 资金类型名称
     */
    @Field(type = FieldType.Text, index = false)
    private String fundTypeName;

    /**
     * 交易收支类型ID
     */
    @Field(type = FieldType.Long)
    private Long paymentTypeId;

    /**
     * 交易收支类型名称
     */
    @Field(type = FieldType.Text, index = false)
    private String paymentTypeName;
}
