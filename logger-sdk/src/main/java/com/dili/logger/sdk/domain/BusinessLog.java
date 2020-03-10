package com.dili.logger.sdk.domain;

import java.io.Serializable;

/**
 * 业务日志
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 14:24
 */
public class BusinessLog implements Serializable {
    private static final long serialVersionUID = -2618238368411037146L;

    /**
     * 操作类型
     */
    private String operationType;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

}
