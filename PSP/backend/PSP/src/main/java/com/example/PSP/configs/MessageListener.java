package com.example.PSP.configs;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues = MQConfig.MESSAGE_QUE)
    public void listener(RabbitMQCustomMessage message){
        System.out.println(message);
    }
}
