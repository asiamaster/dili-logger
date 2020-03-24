package com.dili.logger.sdk.domain;

/**
 * 异常日志
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 15:09
 */
public class ExceptionLog extends BaseLog {

    private static final long serialVersionUID = 1017993514723670640L;

    /**
     * 异常类型
     */
    private String exceptionType;

    /**
     * 冗余存储异常类型的显示值
     */
    private String exceptionTypeText;

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionTypeText() {
        return exceptionTypeText;
    }

    public void setExceptionTypeText(String exceptionTypeText) {
        this.exceptionTypeText = exceptionTypeText;
    }
}
