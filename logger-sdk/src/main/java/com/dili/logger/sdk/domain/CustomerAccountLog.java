package com.dili.logger.sdk.domain;

/**
 * <B>客户账户操作记录</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/17 17:33
 */
public class CustomerAccountLog extends BaseLog {

    /**
     * 资金项目ID
     */
    private Long fundItemId;
    /**
     * 资金项目名称
     */
    private String fundItemName;
    /**
     * 操作金额
     */
    private Long amount;
    /**
     * 资金类型ID
     */
    private Long fundTypeId;
    /**
     * 资金类型名称
     */
    private String fundTypeName;

    public Long getFundItemId() {
        return fundItemId;
    }

    public void setFundItemId(Long fundItemId) {
        this.fundItemId = fundItemId;
    }

    public String getFundItemName() {
        return fundItemName;
    }

    public void setFundItemName(String fundItemName) {
        this.fundItemName = fundItemName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getFundTypeId() {
        return fundTypeId;
    }

    public void setFundTypeId(Long fundTypeId) {
        this.fundTypeId = fundTypeId;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }
}
