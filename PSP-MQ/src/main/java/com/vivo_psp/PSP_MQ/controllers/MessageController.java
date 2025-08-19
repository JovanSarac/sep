package com.vivo_psp.PSP_MQ.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivo_psp.PSP_MQ.dtos.*;
import com.vivo_psp.PSP_MQ.models.*;
import com.vivo_psp.PSP_MQ.configs.MQConfig;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private AsyncRabbitTemplate asyncRabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/publishCreateSubscription")
    public ResponseEntity<SubscriptionDto> publishCreateSubscription(@RequestHeader Map<String, String> headers,
            @RequestBody SubscriptionRequest subscriptionRequestDto){

        SubscriptionMessage message = new SubscriptionMessage(
                "CREATE" + UUID.randomUUID().toString(),
                headers.get("authorization"),
                subscriptionRequestDto,
                new Date());

        try {
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_SUBSCRIPTION,
                    MQConfig.ROUTING_KEY_SUBSCRIPTION,
                    message).get();

            if (response instanceof String) {
                SubscriptionDto dto = objectMapper.readValue((String) response, SubscriptionDto.class);
                return ResponseEntity.ok(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/publishUpdateSubscription")
    public ResponseEntity<String> publishUpdateSubscription(@RequestHeader Map<String, String> headers,
                                            @RequestBody SubscriptionDto subscriptionDto){
        SubscriptionMessage message = new SubscriptionMessage("UPDATE" + UUID.randomUUID().toString(),
                headers.get("authorization"),
                subscriptionDto,
                new Date());

        try {
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_SUBSCRIPTION,
                    MQConfig.ROUTING_KEY_SUBSCRIPTION,
                    message).get();

            if (response instanceof String) {
                return ResponseEntity.ok(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/publishSendRequest/{sessionId}/{typeOfCardPayment}")
    public ResponseEntity<?> publishSendRequest(@RequestHeader Map<String, String> headers,
                                                             @PathVariable Long sessionId,
                                                             @PathVariable String typeOfCardPayment){
        RequestMessage message = new RequestMessage(
                UUID.randomUUID().toString(),
                headers.get("authorization"),
                sessionId,
                new Date(),
                typeOfCardPayment);

        try{
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_REQUEST,
                    MQConfig.ROUTING_KEY_REQUEST,
                    message).get();

            if (response instanceof String) {
                if(typeOfCardPayment.equals("card")){
                    PaymentDataDto dto = objectMapper.readValue((String) response, PaymentDataDto.class);
                    return ResponseEntity.ok(dto);
                } else {
                    RequestQRCodePaymentDto dto = objectMapper.readValue((String) response, RequestQRCodePaymentDto.class);
                    return ResponseEntity.ok(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/publishApiKeyRequest")
    public String publishApiKeyRequest(@RequestBody Long paymentTypeId){
        ApiKeyMessage message = new ApiKeyMessage(UUID.randomUUID().toString(),
                paymentTypeId,
                new Date());

        template.convertAndSend(MQConfig.EXCHANGE_APIKEY_REQUEST,
                MQConfig.ROUTING_KEY_APIKEY_REQUEST, message);

        return "Message published";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserInfoDto> publishGetUserInfoById(@RequestHeader Map<String, String> headers, @PathVariable Long id){
        UserInfoMessage message = new UserInfoMessage(
                UUID.randomUUID().toString(),
                headers.get("authorization"),
                new Date(),
                id);

        try{
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_USER_INFO,
                    MQConfig.ROUTING_KEY_USER_INFO,
                    message).get();

            if (response instanceof String) {
                UserInfoDto dto = objectMapper.readValue((String) response, UserInfoDto.class);
                return ResponseEntity.ok(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/session/{id}/{typeOfOutput}")
    public ResponseEntity<?> publishGetActivePSPServicesBySessionId(@RequestHeader Map<String, String> headers,
                                                                            @PathVariable Long id,
                                                                            @PathVariable String typeOfOutput){
        SessionMessage message = new SessionMessage(
                UUID.randomUUID().toString(),
                headers.get("authorization"),
                new Date(),
                id,
                typeOfOutput);

        try{
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_PSP_SERVICES_BY_SESSION,
                    MQConfig.ROUTING_KEY_PSP_SERVICES_BY_SESSION,
                    message).get();

            if (response instanceof String) {
                if(typeOfOutput.equals("PSPServices")) {
                    List<PSPService> dto = objectMapper.readValue(
                            (String) response,
                            new TypeReference<List<PSPService>>() {
                            }
                    );
                    return ResponseEntity.ok(dto);
                } else if(typeOfOutput.equals("session")){
                    SessionDto dto = objectMapper.readValue((String) response, SessionDto.class);
                    return ResponseEntity.ok(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/activePaymentServices")
    public ResponseEntity<?> publishGetActivePSPServices(@RequestHeader Map<String, String> headers){
        DefaultMessage message = new DefaultMessage(
                UUID.randomUUID().toString(),
                headers.get("authorization"),
                new Date());

        try{
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_PSP_SERVICES,
                    MQConfig.ROUTING_KEY_PSP_SERVICES,
                    message).get();

            if (response instanceof String) {
                List<PSPService> dto = objectMapper.readValue(
                        (String) response,
                        new TypeReference<List<PSPService>>() {
                        }
                );

                return ResponseEntity.ok(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/userActiveSubscription/{id}")
    public ResponseEntity<List<SubscriptionDto>> publishGetUserActiveSubscription(@RequestHeader Map<String, String> headers,
                                                         @PathVariable Long id){
        UserInfoMessage message = new UserInfoMessage(
                UUID.randomUUID().toString(),
                headers.get("authorization"),
                new Date(),
                id);

        try{
            Object response = asyncRabbitTemplate.convertSendAndReceive(
                    MQConfig.EXCHANGE_SUBSRIPTION,
                    MQConfig.ROUTING_KEY_SUBSRIPTION,
                    message).get();

            if (response instanceof String) {
                List<SubscriptionDto> dto = objectMapper.readValue(
                        (String) response,
                        new TypeReference<List<SubscriptionDto>>() {
                        }
                );

                return ResponseEntity.ok(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
