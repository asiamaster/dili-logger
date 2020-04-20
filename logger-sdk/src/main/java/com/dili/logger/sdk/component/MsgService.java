package com.dili.logger.sdk.component;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.boot.LoggerRabbitConfiguration;
import com.dili.logger.sdk.boot.LoggerRabbitProducerConfiguration;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.dto.CorrelationDataExt;
import com.dili.ss.mvc.util.RequestUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MQ消息服务
 */
@Component
public class MsgService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LoggerRabbitProducerConfiguration loggerRabbitProducerConfiguration;

    /**
     * 发送业务日志到MQ
     * @param businessLog
     */
    public void sendBusinessLog(BusinessLog businessLog){
        businessLog.setRemoteIp(RequestUtils.getIpAddress(LoggerContext.getRequest()));
        businessLog.setServerIp(LoggerContext.getRequest().getLocalAddr());
        if(businessLog.getCreateTime() == null) {
            businessLog.setCreateTime(LocalDateTime.now());
        }
        sendMsg(LoggerRabbitConfiguration.LOGGER_TOPIC_EXCHANGE, LoggerRabbitConfiguration.LOGGER_ADD_BUSINESS_KEY, JSON.toJSONString(businessLog));
    }
    /**
     * 发送消息
     * 通用服务
     * @param exchange
     * @param routingKey
     * @param json
     */
    public void sendMsg(String exchange, String routingKey, String json){
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
        correlationData.setExchange(LoggerRabbitConfiguration.LOGGER_TOPIC_EXCHANGE);
        correlationData.setRoutingKey(LoggerRabbitConfiguration.LOGGER_ADD_BUSINESS_KEY);
        this.rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
    }
}
