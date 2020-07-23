package com.dili.logger.sdk.component;

import com.dili.logger.sdk.dto.CorrelationDataExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 失败消息重新发送任务
 */
public class ReissueTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ReissueTask.class);

    private RabbitTemplate rabbitTemplate;

    public ReissueTask(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override

    public void run() {
        List<CorrelationDataExt> messageCorrelationDataList = new ArrayList<>(SendFailedMessageHolder.getAll());
        logger.info("------------------获取到" + messageCorrelationDataList.size() + "条ack=false的消息，准备重发------------------");
        SendFailedMessageHolder.clear();
        int i = 1;
        for (CorrelationDataExt messageCorrelationData : messageCorrelationDataList) {
            Object message = messageCorrelationData.getMessage();
            String messageId = messageCorrelationData.getId();
            logger.info("------------------重发第" + i + "条消息，id: " + messageId + "------------------");
            i++;
            rabbitTemplate.convertSendAndReceive(messageCorrelationData.getExchange(), messageCorrelationData.getRoutingKey(),
                    message, messageCorrelationData);
        }
        logger.info("------------------重发完成------------------");
    }

}
