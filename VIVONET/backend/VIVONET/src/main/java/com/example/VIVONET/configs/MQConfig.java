package com.example.VIVONET.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String QUEUE_APIKEY_REQUEST = "queue_apiKey_request";
    public static final String EXCHANGE_APIKEY_REQUEST = "exchange_apiKey_request";
    public static final String ROUTING_KEY_APIKEY_REQUEST = "routing_key_apiKey_request";

    public static final String QUEUE_APIKEY_RESPONSE = "queue_apiKey_response";
    public static final String EXCHANGE_APIKEY_RESPONSE = "exchange_apiKey_response";
    public static final String ROUTING_KEY_APIKEY_RESPONSE = "routing_key_apiKey_response";

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
    public Queue queueApiKeyResponse(){
        return new Queue(QUEUE_APIKEY_RESPONSE);
    }

    @Bean
    public TopicExchange exchangeApiKeyResponse(){
        return new TopicExchange(EXCHANGE_APIKEY_RESPONSE);
    }

    @Bean
    public Binding bindingApiKeyResponse(Queue queueApiKeyResponse, TopicExchange exchangeApiKeyResponse){
        return BindingBuilder
                .bind(queueApiKeyResponse)
                .to(exchangeApiKeyResponse)
                .with(ROUTING_KEY_APIKEY_RESPONSE);
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
