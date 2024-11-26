package com.example.VIVONET.configs;

import com.example.VIVONET.models.ApiKey;
import com.example.VIVONET.services.ApiKeyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Component
public class MessageListener {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private ApiKeyService apiKeyService;

    @RabbitListener(queues = MQConfig.QUEUE_APIKEY_REQUEST)
    public void subscriptionListener(ApiKeyMessage message){
        ApiKey apiKey = apiKeyService.findByPaymentTypeId(message.getPaymentTypeId());

        ApiKeyResponseMessage responseMessage = new ApiKeyResponseMessage(UUID.randomUUID().toString(),
                apiKey.getMerchantId(),
                apiKey.getMerchantPassword(),
                new Date());

        template.convertAndSend(MQConfig.EXCHANGE_APIKEY_RESPONSE,
                MQConfig.ROUTING_KEY_APIKEY_RESPONSE, responseMessage);
    }
}
