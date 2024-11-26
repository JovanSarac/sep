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

    public static final String QUEUE_REQUEST = "queue_request";
    public static final String EXCHANGE_REQUEST = "exchange_request";
    public static final String ROUTING_KEY_REQUEST = "routing_key_request";

    public static final String QUEUE_APIKEY_REQUEST = "queue_apiKey_request";
    public static final String EXCHANGE_APIKEY_REQUEST = "exchange_apiKey_request";
    public static final String ROUTING_KEY_APIKEY_REQUEST = "routing_key_apiKey_request";

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
    public Queue queueRequest(){
        return new Queue(QUEUE_REQUEST);
    }

    @Bean
    public TopicExchange exchangeRequest(){
        return new TopicExchange(EXCHANGE_REQUEST);
    }

    @Bean
    public Binding bindingRequest(Queue queueRequest, TopicExchange exchangeRequest){
        return BindingBuilder
                .bind(queueRequest)
                .to(exchangeRequest)
                .with(ROUTING_KEY_REQUEST);
    }

    @Bean
    public Queue queueApiKeyRequest(){
        return new Queue(QUEUE_APIKEY_REQUEST);
    }

    @Bean
    public TopicExchange exchangeApiKeyRequest(){
        return new TopicExchange(EXCHANGE_APIKEY_REQUEST);
    }

    @Bean
    public Binding bindingApiKeyRequest(Queue queueApiKeyRequest, TopicExchange exchangeApiKeyRequest){
        return BindingBuilder
                .bind(queueApiKeyRequest)
                .to(exchangeApiKeyRequest)
                .with(ROUTING_KEY_APIKEY_REQUEST);
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
