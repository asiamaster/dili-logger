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
    //rabbitTemplate是thread safe的，主要是channel不能共用，但是在rabbitTemplate源码里channel是threadlocal的，所以singleton没问题。
    //但是rabbitTemplate要设置回调类，如果是singleton，回调类就只能有一个，所以如果想要设置不同的回调类，就要设置为prototype的scope
    //如果需要在生产者需要消息发送后的回调，需要对rabbitTemplate设置ConfirmCallback对象，由于不同的生产者需要对应不同的ConfirmCallback，
    //如果rabbitTemplate设置为单例bean，则所有的rabbitTemplate实际的ConfirmCallback为最后一次申明的ConfirmCallback。
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