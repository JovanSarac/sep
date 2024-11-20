package com.vivo_psp.PSP_MQ.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String QUEUE_SUBSCRIPTION = "queue_subscription";
    public static final String EXCHANGE_SUBSCRIPTION = "exchange_subscription";
    public static final String ROUTING_KEY_SUBSCRIPTION = "routing_key_subscription";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_SUBSCRIPTION);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_SUBSCRIPTION);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY_SUBSCRIPTION);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
