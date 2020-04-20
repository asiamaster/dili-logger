package com.dili.logger.sdk.boot;

import com.dili.logger.sdk.glossary.LoggerConstant;
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