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
     * 业务卡号
     */
    private String cardNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 资金项目ID
     */
    private Long fundItemId;
    /**
     * 资金项目名称
     */
    private String fundItemName;
    /**
     * 初始金额
     */
    private Long InitialAmount;
    /**
     * 交易金额
     */
    private Long tradeAmount;
    /**
     * 交易后余额
     */
    private Long balance;
    /**
     * 资金类型ID
     */
    private Long fundTypeId;
    /**
     * 资金类型名称
     */
    private String fundTypeName;

    /**
     * 交易收支类型ID
     */
    private Long paymentTypeId;

    /**
     * 交易收支类型名称
     */
    private String paymentTypeName;


    /********************getter  setter *************************/

    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
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
    public Long getInitialAmount() {
        return InitialAmount;
    }
    public void setInitialAmount(Long initialAmount) {
        InitialAmount = initialAmount;
    }
    public Long getTradeAmount() {
        return tradeAmount;
    }
    public void setTradeAmount(Long tradeAmount) {
        this.tradeAmount = tradeAmount;
    }
    public Long getBalance() {
        return balance;
    }
    public void setBalance(Long balance) {
        this.balance = balance;
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
    public Long getPaymentTypeId() {
        return paymentTypeId;
    }
    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }
    public String getPaymentTypeName() {
        return paymentTypeName;
    }
    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }
}
