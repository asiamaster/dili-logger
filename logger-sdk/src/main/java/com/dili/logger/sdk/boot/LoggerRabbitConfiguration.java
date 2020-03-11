package com.dili.logger.sdk.boot;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by asiam on 2019/4/8
 */
@Configuration
@ConditionalOnExpression("'${logger.enable}'=='true'")
@ConditionalOnClass(RabbitTemplate.class)
public class LoggerRabbitConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean(name = "messageConverter")
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //=========================================================================================================
    //消息交换机
    public static final String LOGGER_TOPIC_EXCHANGE = "dili.logger.topicExchange";
    public static final String LOGGER_ADD_BUSINESS_KEY = "dili.logger.addBusinessKey";
    public static final String LOGGER_ADD_BUSINESS_QUEUE = "dili.logger.addBusinessQueue";

    @Bean
    public TopicExchange operationTopicExchange() {
        return new TopicExchange(LOGGER_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue addOperationQueue() {
        return new Queue(LOGGER_ADD_BUSINESS_QUEUE, true, false, false);
    }

    @Bean
    public Binding addOperationBinding() {
        return BindingBuilder.bind(addOperationQueue()).to(operationTopicExchange()).with(LOGGER_ADD_BUSINESS_KEY);
    }
}