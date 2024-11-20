package com.example.PSP.configs;

import com.example.PSP.dtos.SubscriptionDto;
import com.example.PSP.dtos.SubscriptionRequest;
import com.example.PSP.models.SubscriptionMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MessageListener {

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues = MQConfig.QUEUE_SUBSCRIPTION)
    public void subscriptionListener(SubscriptionMessage message){

        String url = message.getMessageId().contains("CREATE") ?
                "http://localhost:8090/api/user/create_subscription"
                : "http://localhost:8090/api/user/update_subscription";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        HttpEntity<SubscriptionRequest> requestEntity = new HttpEntity<>(message.getRequest(), headers);

        try {
            String response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
            System.out.println("Response from endpoint: " + response);
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }
    }
}
