package com.example.PSP.configs;

import com.example.PSP.dtos.*;
import com.example.PSP.models.SubscriptionMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    public String subscriptionListener(SubscriptionMessage message){
        boolean isCreate = message.getMessageId().contains("CREATE") ?
                    true : false;

        String url = isCreate ?
                "http://localhost:8090/api/user/create_subscription"
                : "http://localhost:8090/api/user/update_subscription";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        var requestEntity = new HttpEntity<>(
                isCreate ? message.getRequest() : message.getSubscription(), headers);
        var method = isCreate ? HttpMethod.POST : HttpMethod.PUT;
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, method, requestEntity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
            return null;
        }
    }

    @RabbitListener(queues = MQConfig.QUEUE_REQUEST)
    public String subscriptionListener(RequestMessage message){
        String url = message.getTypeOfCardPayment().equals("card") ?
                "http://localhost:8090/api/psp/requests/sendRequest/" + message.getSessionId()
                : "http://localhost:8090/api/psp/requests/sendRequestQRCode/" + message.getSessionId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        var requestEntity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
            return null;
        }
    }

    @RabbitListener(queues = MQConfig.QUEUE_USER_INFO)
    public String getUserInfoByIdListener(UserInfoMessage message){
        String url = "http://localhost:8090/api/user/" + message.getUserId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        var requestEntity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
            return null;
        }
    }

    @RabbitListener(queues = MQConfig.QUEUE_PSP_SERVICES_BY_SESSION)
    public String sessionChannel(SessionMessage message){
        String url = message.getTpyeOfOutput().equals("PSPServices") ?
                "http://localhost:8090/api/active_pspservices_bysession/" + message.getSessionId()
                : "http://localhost:8090/api/session/" + message.getSessionId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", message.getJWTToken());

        var requestEntity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
            return null;
        }
    }
}
