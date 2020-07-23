package com.dili.logger.sdk.component;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MQ消息服务
 */
@Component
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class MsgService {

    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    /**
     * 发送业务日志到MQ
     * @param businessLog
     */
    public void sendBusinessLog(BusinessLog businessLog) {
        if (Objects.nonNull(businessLog)) {
            if (businessLog.getCreateTime() == null) {
                businessLog.setCreateTime(LocalDateTime.now());
            }
            rabbitMQMessageService.send(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE, LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, JSON.toJSONString(businessLog));
        }
    }

    /**
     * 发送异常日志到MQ
     * @param exceptionLog
     */
    public void sendExceptionLog(ExceptionLog exceptionLog) {
        if (Objects.nonNull(exceptionLog)) {
            if (exceptionLog.getCreateTime() == null) {
                exceptionLog.setCreateTime(LocalDateTime.now());
            }
            rabbitMQMessageService.send(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE, LoggerConstant.MQ_LOGGER_ADD_EXCEPTION_KEY, JSON.toJSONString(exceptionLog));
        }
    }


}
