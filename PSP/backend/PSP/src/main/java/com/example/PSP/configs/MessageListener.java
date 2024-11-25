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
        boolean isCreate = message.getMessageId().contains("CREATE") ?
                    true : false;

        String url = isCreate ?
                "http://localhost:8090/api/user/create_subscription"
                : "http://localhost:8090/api/user/update_subscription";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        var requestEntity = new HttpEntity<>(
                isCreate ?
                        message.getRequest() : message.getSubscription(), headers);
        var method = isCreate ?
                    HttpMethod.POST : HttpMethod.PUT;
        try {
            String response = restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }
    }

    @RabbitListener(queues = MQConfig.QUEUE_REQUEST)
    public void subscriptionListener(RequestMessage message){
        String url = "http://localhost:8090/api/psp/requests/sendRequest/" + message.getSessionId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        var requestEntity = new HttpEntity<>(null, headers);
        var method = HttpMethod.GET;
        try {
            String response = restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }
    }
}
