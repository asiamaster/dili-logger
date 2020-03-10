package com.dili.logger.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <B>RabbitMQ的相关配置信息</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/17 10:27
 */
@Configuration
public class RabbitMQConfig {

    //消息交换机
    public static final String LOGGER_TOPIC_EXCHANGE = "dili.logger.topicExchange";
    public static final String LOGGER_ADD_BUSINESS_KEY = "dili.logger.addBusinessKey";
    public static final String LOGGER_ADD_BUSINESS_QUEUE = "dili.logger.addBusinessQueue";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(LOGGER_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue addOperationQueue() {
        return new Queue(LOGGER_ADD_BUSINESS_QUEUE, true, false, false);
    }

    @Bean
    public Binding addOperationBinding() {
        return BindingBuilder.bind(addOperationQueue()).to(topicExchange()).with(LOGGER_ADD_BUSINESS_KEY);
    }
}
