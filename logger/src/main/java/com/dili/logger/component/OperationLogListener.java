package com.dili.logger.component;

import com.alibaba.fastjson.JSONObject;
import com.dili.logger.domain.BusinessLog;
import com.dili.logger.service.BusinessLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <B>操作日志推送消息监听器</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/17 11:05
 */
@Component
@Slf4j
public class OperationLogListener {

    @Autowired
    private BusinessLogService operationLogService;

    /**
     * 日志消息监听器
     *
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitMQConfig.LOGGER_ADD_OPERATION_QUEUE}", concurrency = "10")
    public void processMessage(Channel channel, Message message) {
        log.info("收到消息: " + message);
        try {
            String data = new String(message.getBody(), "UTF-8");
            log.debug("获取到的body数据:" + data);
            BusinessLog operationLog = JSONObject.parseObject(data, BusinessLog.class);
            operationLogService.save(operationLog);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("转换对象: {} 出错 {}", message, e);
            // redelivered = true, 表明该消息是重复处理消息
            Boolean redelivered = message.getMessageProperties().getRedelivered();
            try {
                if (redelivered) {
                    /**
                     * 1. 对于重复处理的队列消息做补偿机制处理
                     * 2. 从队列中移除该消息，防止队列阻塞
                     */
                    // 消息已重复处理失败, 扔掉消息
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                    log.error("消息 {} 重新处理失败，扔掉消息", message);
                } else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            } catch (IOException ex) {
                log.error("消息 {} 重放回队列失败 {}", message, ex);
            }
        }
    }
}
