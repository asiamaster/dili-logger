package com.dili.logger.sdk.component;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ReissueMessageSchedule implements InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    public void start(){
        scheduledExecutorService.scheduleWithFixedDelay(new ReissueTask(rabbitTemplate), 10, 10, TimeUnit.SECONDS);
    }

    @Override

    public void afterPropertiesSet(){
        this.start();
    }

}
