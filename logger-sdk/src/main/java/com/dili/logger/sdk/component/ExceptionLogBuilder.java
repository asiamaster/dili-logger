package com.dili.logger.sdk.component;

import com.dili.logger.sdk.base.LogBuilder;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BaseLog;
import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.ss.util.BeanConver;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于统一权限的异常日志构建器
 */
@Component
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class ExceptionLogBuilder implements LogBuilder {

    @Override
    public BaseLog build(Method method, Object[] args) {
        return BeanConver.copyMap(LoggerContext.getAll(), ExceptionLog.class);
    }
}
