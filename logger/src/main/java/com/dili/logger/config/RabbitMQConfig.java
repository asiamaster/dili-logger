package com.dili.logger.config;

import com.dili.logger.sdk.glossary.LoggerConstant;
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

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange loggerTopicExchange() {
        return new TopicExchange(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue addBusinessQueue() {
        return new Queue(LoggerConstant.MQ_LOGGER_ADD_BUSINESS_QUEUE, true, false, false);
    }

    @Bean
    public Binding addBusinessBinding() {
        return BindingBuilder.bind(addBusinessQueue()).to(loggerTopicExchange()).with(LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY);
    }

    @Bean
    public Queue addExceptionQueue() {
        return new Queue(LoggerConstant.MQ_LOGGER_ADD_EXCEPTION_QUEUE, true, false, false);
    }

    @Bean
    public Binding addExceptionBinding() {
        return BindingBuilder.bind(addExceptionQueue()).to(loggerTopicExchange()).with(LoggerConstant.MQ_LOGGER_ADD_EXCEPTION_KEY);
    }
}
