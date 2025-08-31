package com.example.PSP.configs;

import com.example.PSP.dtos.*;
import com.example.PSP.models.SubscriptionMessage;
import com.example.PSP.models.PSPService;
import com.example.PSP.services.PSPServiceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MessageListener {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PSPServiceService pspServiceService;

    @RabbitListener(queues = MQConfig.QUEUE_SUBSCRIPTION)
    public String subscriptionListener(SubscriptionMessage message){
        boolean isCreate = message.getMessageId().contains("CREATE") ?
                    true : false;

        String url = isCreate ?
                "https://localhost:8090/api/user/create_subscription"
                : "https://localhost:8090/api/user/update_subscription";

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
                "https://localhost:8090/api/psp/requests/sendRequest/" + message.getSessionId()
                : "https://localhost:8090/api/psp/requests/sendRequestQRCode/" + message.getSessionId();

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
        String url = "https://localhost:8090/api/user/" + message.getUserId();

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
    public String sessionListener(SessionMessage message){
        String url = message.getTpyeOfOutput().equals("PSPServices") ?
                "https://localhost:8090/api/active_pspservices_bysession/" + message.getSessionId()
                : "https://localhost:8090/api/session/" + message.getSessionId();

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

    @RabbitListener(queues = MQConfig.QUEUE_PSP_SERVICES)
    public String pspServicesListener(DefaultMessage message){
        String url = "https://localhost:8090/api/user/active_payment_services";

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

    @RabbitListener(queues = MQConfig.QUEUE_SUBSRIPTION)
    public String subscriptionListener(UserInfoMessage message){
        String url = message.getUserType().equals("user") ?
                "https://localhost:8090/api/user_active_subscription/" + message.getUserId()
                : "https://localhost:8090/api/user_subscription/" + message.getUserId();

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

    @RabbitListener(queues = MQConfig.QUEUE_ADMIN)
    public String getAllUsersListener(UserInfoMessage message){
        String url = "https://localhost:8090/api/admin/users";

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

    @Transactional
    @RabbitListener(queues = MQConfig.QUEUE_CONSUL)
    public void saveNewPaymentService(NewPaymentServiceMessage message){
        List<PSPService> activeServices = pspServiceService.findAll();

        boolean serviceAlreadyExists = false;
         for(var service : activeServices){
            if(service.getName().toLowerCase().contains(message.getServiceName().toLowerCase())){
                if(message.getType().equals("remove")){
                    service.setActive(false);
                    pspServiceService.save(service);
                    return;
                }

                if(!service.getActive()){
                    service.setActive(true);
                    pspServiceService.save(service);
                    return;
                }

                serviceAlreadyExists = true;
                break;
            }
        }

        if(!serviceAlreadyExists){
            //add it to the db
            List<String> supportedPaymentMethods = new ArrayList<>();
            supportedPaymentMethods.add(message.getServiceName());

            PSPService newService = new PSPService(
                    message.getServiceName(),
                    "New payment service",
                    100.0,
                    true,
                    supportedPaymentMethods
            );

            pspServiceService.save(newService);
        }
    }
}
