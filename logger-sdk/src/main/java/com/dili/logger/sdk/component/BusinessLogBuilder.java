package com.dili.logger.sdk.component;

import com.dili.logger.sdk.base.LogBuilder;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BaseLog;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于统一权限的业务日志构建器
 */
@Component
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class BusinessLogBuilder implements LogBuilder {

    @Override
    public BaseLog build(Method method, Object[] args) {
        BusinessLog businessLog = new BusinessLog();
        if(LoggerContext.get(LoggerConstant.LOG_OPERATOR_ID_KEY) != null){
            businessLog.setOperatorId((Long)LoggerContext.get(LoggerConstant.LOG_OPERATOR_ID_KEY));
        }
        if(LoggerContext.get(LoggerConstant.LOG_MARKET_ID_KEY) != null){
            businessLog.setMarketId((Long)LoggerContext.get(LoggerConstant.LOG_MARKET_ID_KEY));
        }
        if(LoggerContext.get(LoggerConstant.LOG_BUSINESS_CODE_KEY) != null){
            businessLog.setBusinessCode(LoggerContext.get(LoggerConstant.LOG_BUSINESS_CODE_KEY).toString());
        }
        if(LoggerContext.get(LoggerConstant.LOG_BUSINESS_ID_KEY) != null){
            businessLog.setBusinessId((Long)LoggerContext.get(LoggerConstant.LOG_BUSINESS_ID_KEY));
        }
        return businessLog;
    }
}
