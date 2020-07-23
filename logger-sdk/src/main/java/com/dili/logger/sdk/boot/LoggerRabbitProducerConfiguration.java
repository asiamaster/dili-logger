package com.dili.logger.sdk.boot;

import com.dili.logger.sdk.component.SendFailedMessageHolder;
import com.dili.logger.sdk.dto.CorrelationDataExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * Created by asiam on 2019/4/8
 */
@Component
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class LoggerRabbitProducerConfiguration implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    protected static final Logger LOGGER = LoggerFactory.getLogger(LoggerRabbitProducerConfiguration.class);
//    @Autowired
//    private RabbitTemplate rabbitTemplate;

//    @PostConstruct
//    public void init(){
//        rabbitTemplate.setConfirmCallback(this);
//        rabbitTemplate.setReturnCallback(this);
//    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        System.out.println("消息唯一标识："+correlationData);
        if (ack) {
            LOGGER.info("消息成功到达Exchange");
        }else{
            // 根据业务逻辑实现消息补偿机制
            if (correlationData instanceof CorrelationDataExt) {
                CorrelationDataExt messageCorrelationData = (CorrelationDataExt) correlationData;
                if (!ack) {
                    //在请求主线程发送1万条消息的过程中，将rabbitmq关闭，这时请求主线程和ConfirmCallback线程都在等待Connection恢复，
                    //然后重新启动rabbitmq，当程序重新建立Connection之后，这两个线程会死锁。
                    //可行的方案：定时任务重发
                    SendFailedMessageHolder.add(messageCorrelationData);
                }
//                LOGGER.error("消息到达Exchange失败，内容:{}，原因:{}", message, cause);
            }
        }
    }

    /**
     * exchange 到达 queue, 则 returnedMessage 不回调
     * exchange 到达 queue 失败, 则 returnedMessage 回调
     * 需要设置spring.rabbitmq.publisher-returns=true
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey){
        LOGGER.info("消息报文:{}", new String(message.getBody()));
        LOGGER.info("消息编号:{}", replyCode);
        LOGGER.info("描述:{}", replyText);
        LOGGER.info("交换机名称:{}", exchange);
        LOGGER.info("路由名称:{}", routingKey);
        // 根据业务逻辑实现消息补偿机制

    }
}