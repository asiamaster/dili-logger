package com.dili.logger.sdk.component;

import com.dili.logger.sdk.base.LogBuilder;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BaseLog;
import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于统一权限的异常日志构建器
 */
@Component
@ConditionalOnExpression("'${logger.enable}'=='true'")
public class ExceptionLogBuilder implements LogBuilder {

    @Override
    public BaseLog build(Method method, Object[] args) {
        ExceptionLog exceptionLog = new ExceptionLog();
        if(LoggerContext.get(LoggerConstant.LOG_OPERATOR_ID_KEY) != null){
            exceptionLog.setOperatorId((Long)LoggerContext.get(LoggerConstant.LOG_OPERATOR_ID_KEY));
        }
        if(LoggerContext.get(LoggerConstant.LOG_MARKET_ID_KEY) != null){
            exceptionLog.setMarketId((Long)LoggerContext.get(LoggerConstant.LOG_MARKET_ID_KEY));
        }
        if(LoggerContext.get(LoggerConstant.LOG_EXCEPTION_TYPE_KEY) != null){
            exceptionLog.setExceptionType(LoggerContext.get(LoggerConstant.LOG_EXCEPTION_TYPE_KEY).toString());
        }
        if(LoggerContext.get(LoggerConstant.LOG_IP_KEY) != null){
            exceptionLog.setIp(LoggerContext.get(LoggerConstant.LOG_IP_KEY).toString());
        }
        return exceptionLog;
    }
}
