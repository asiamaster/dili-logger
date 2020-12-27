package com.dili.logger.sdk.util;

import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import org.apache.commons.lang.StringUtils;

/**
 * 日志服务util类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/27 15:44
 */
public class LoggerUtil {

    /**
     * MQ发送日志内容时 构建业务日志的 LoggerContext
     * @param businessId 业务ID
     * @param businessCode 业务编号
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param marketId 所属内容
     * @param notes
     */
    public static void buildBusinessLoggerContext(Long businessId, String businessCode, Long operatorId, String operatorName, Long marketId, String notes) {
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, businessCode);
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, businessId);
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, operatorId);
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, operatorName);
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, marketId);
        LoggerContext.put(LoggerConstant.LOG_NOTES_KEY, notes);
    }
}
