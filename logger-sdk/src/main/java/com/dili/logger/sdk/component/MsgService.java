package com.dili.logger.sdk.component;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.boot.LoggerRabbitProducerConfiguration;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.logger.sdk.dto.CorrelationDataExt;
import com.dili.logger.sdk.glossary.LoggerConstant;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * MQ消息服务
 */
@Component
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class MsgService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LoggerRabbitProducerConfiguration loggerRabbitProducerConfiguration;

    /**
     * 发送业务日志到MQ
     * @param businessLog
     */
    public void sendBusinessLog(BusinessLog businessLog) {
        if (Objects.nonNull(businessLog)) {
            if (businessLog.getCreateTime() == null) {
                businessLog.setCreateTime(LocalDateTime.now());
            }
            sendMsg(LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, JSON.toJSONString(businessLog));
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
            sendMsg(LoggerConstant.MQ_LOGGER_ADD_EXCEPTION_KEY, JSON.toJSONString(exceptionLog));
        }
    }

    /**
     * 发送消息
     * 通用服务
     * @param routingKey
     * @param json
     */
    public void sendMsg(String routingKey, String json) {
        String uuid = UUID.randomUUID().toString();
        Message message = MessageBuilder.withBody(json.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8")
                .setMessageId(uuid)
                .build();
        this.rabbitTemplate.setReturnCallback(loggerRabbitProducerConfiguration);
        this.rabbitTemplate.setConfirmCallback(loggerRabbitProducerConfiguration);

        //使用继承扩展的CorrelationData 、id消息流水号
        CorrelationDataExt correlationData =
                new CorrelationDataExt(uuid);
        correlationData.setMessage(message);
        correlationData.setExchange(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE);
        correlationData.setRoutingKey(routingKey);
        //有可能长时间retry之后依然不能恢复Connection，如rabbitmq挂掉的情况，不能一直retry下去阻塞接口调用
        //这种情况是没有confirm的，因为消息都没有发出去。所以处理就更简单了
        //retry失败或者没有retry机制都会抛出AmqpConnectException，catch之后将消息保存起来即可
        try{
            this.rabbitTemplate.convertAndSend(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE, routingKey, message, correlationData);
        }catch (AmqpConnectException e) {
            SendFailedMessageHolder.add(correlationData);
        }
    }
}
