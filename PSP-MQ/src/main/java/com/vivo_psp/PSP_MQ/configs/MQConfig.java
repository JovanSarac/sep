package com.vivo_psp.PSP_MQ.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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

    public static final String QUEUE_USER_INFO = "queue_user_info";
    public static final String EXCHANGE_USER_INFO = "exchange_user_info";
    public static final String ROUTING_KEY_USER_INFO = "routing_key_user_info";

    public static final String QUEUE_PSP_SERVICES_BY_SESSION = "queue_psp_services_by_session";
    public static final String EXCHANGE_PSP_SERVICES_BY_SESSION = "exchange_psp_services_by_session";
    public static final String ROUTING_KEY_PSP_SERVICES_BY_SESSION = "routing_key_psp_services_by_session";

    public static final String QUEUE_PSP_SERVICES = "queue_psp_services";
    public static final String EXCHANGE_PSP_SERVICES = "exchange_psp_services";
    public static final String ROUTING_KEY_PSP_SERVICES = "routing_key_psp_services";

    public static final String QUEUE_SUBSRIPTION = "queue_subscription";
    public static final String EXCHANGE_SUBSRIPTION = "exchange_subscription";
    public static final String ROUTING_KEY_SUBSRIPTION = "routing_key_subscription";

    public static final String QUEUE_ADMIN = "queue_admin";
    public static final String EXCHANGE_ADMIN = "exchange_admin";
    public static final String ROUTING_KEY_ADMIN = "routing_key_admin";

    public static final String QUEUE_CONSUL = "queue_consul";
    public static final String EXCHANGE_CONSUL = "exchange_consul";
    public static final String ROUTING_KEY_CONSUL = "routing_key_consul";

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
    public Queue queueSubscription(){
        return new Queue(QUEUE_SUBSRIPTION);
    }

    @Bean
    public TopicExchange exchangeSubscription(){
        return new TopicExchange(EXCHANGE_SUBSRIPTION);
    }

    @Bean
    public Binding bindingSubscription(Queue queueSubscription, TopicExchange exchangeSubscription){
        return BindingBuilder
                .bind(queueSubscription)
                .to(exchangeSubscription)
                .with(ROUTING_KEY_SUBSRIPTION);
    }

    @Bean
    public Queue queueAdmin(){
        return new Queue(QUEUE_ADMIN);
    }

    @Bean
    public TopicExchange exchangeAdmin(){
        return new TopicExchange(EXCHANGE_ADMIN);
    }

    @Bean
    public Binding bindingAdmin(Queue queueAdmin, TopicExchange exchangeAdmin){
        return BindingBuilder
                .bind(queueAdmin)
                .to(exchangeAdmin)
                .with(ROUTING_KEY_ADMIN);
    }

    @Bean
    public Queue queueConsul(){
        return new Queue(QUEUE_CONSUL);
    }

    @Bean
    public TopicExchange exchangeConsul(){
        return new TopicExchange(EXCHANGE_CONSUL);
    }

    @Bean
    public Binding bindingConsul(Queue queueConsul, TopicExchange exchangeConsul){
        return BindingBuilder
                .bind(queueConsul)
                .to(exchangeConsul)
                .with(ROUTING_KEY_CONSUL);
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

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
