package com.example.PSP.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class MQConfig {

    public static final String QUEUE_SUBSCRIPTION = "queue_subscription";
    public static final String EXCHANGE_SUBSCRIPTION = "exchange_subscription";
    public static final String ROUTING_KEY_SUBSCRIPTION = "routing_key_subscription";

    public static final String QUEUE_REQUEST = "queue_request";
    public static final String EXCHANGE_REQUEST = "exchange_request";
    public static final String ROUTING_KEY_REQUEST = "routing_key_request";

    public static final String QUEUE_APIKEY_RESPONSE = "queue_apiKey_response";
    public static final String EXCHANGE_APIKEY_RESPONSE = "exchange_apiKey_response";
    public static final String ROUTING_KEY_APIKEY_RESPONSE = "routing_key_apiKey_response";

    public static final String QUEUE_USER_INFO = "queue_user_info";
    public static final String EXCHANGE_USER_INFO = "exchange_user_info";
    public static final String ROUTING_KEY_USER_INFO = "routing_key_user_info";

    public static final String QUEUE_PSP_SERVICES_BY_SESSION = "queue_psp_services_by_session";
    public static final String EXCHANGE_PSP_SERVICES_BY_SESSION = "exchange_psp_services_by_session";
    public static final String ROUTING_KEY_PSP_SERVICES_BY_SESSION = "routing_key_psp_services_by_session";

    public static final String QUEUE_PSP_SERVICES = "queue_psp_services";
    public static final String EXCHANGE_PSP_SERVICES = "exchange_psp_services";
    public static final String ROUTING_KEY_PSP_SERVICES = "routing_key_psp_services";

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
    public Queue queueUserInfo(){
        return new Queue(QUEUE_USER_INFO);
    }

    @Bean
    public TopicExchange exchangeUserInfo(){
        return new TopicExchange(EXCHANGE_USER_INFO);
    }

    @Bean
    public Binding bindingUserInfo(Queue queueUserInfo, TopicExchange exchangeUserInfo){
        return BindingBuilder
                .bind(queueUserInfo)
                .to(exchangeUserInfo)
                .with(ROUTING_KEY_USER_INFO);
    }

    @Bean
    public Queue queuePspServicesBySession(){
        return new Queue(QUEUE_PSP_SERVICES_BY_SESSION);
    }

    @Bean
    public TopicExchange exchangePspServicesBySession(){
        return new TopicExchange(EXCHANGE_PSP_SERVICES_BY_SESSION);
    }

    @Bean
    public Binding bindingPspServicesBySession(Queue queuePspServicesBySession, TopicExchange exchangePspServicesBySession){
        return BindingBuilder
                .bind(queuePspServicesBySession)
                .to(exchangePspServicesBySession)
                .with(ROUTING_KEY_PSP_SERVICES_BY_SESSION);
    }

    @Bean
    public Queue queuePspServices(){
        return new Queue(QUEUE_PSP_SERVICES);
    }

    @Bean
    public TopicExchange exchangePspServices(){
        return new TopicExchange(EXCHANGE_PSP_SERVICES);
    }

    @Bean
    public Binding bindingPspServices(Queue queuePspServices, TopicExchange exchangePspServices){
        return BindingBuilder
                .bind(queuePspServices)
                .to(exchangePspServices)
                .with(ROUTING_KEY_PSP_SERVICES);
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
