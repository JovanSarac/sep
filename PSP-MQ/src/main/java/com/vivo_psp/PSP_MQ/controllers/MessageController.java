package com.vivo_psp.PSP_MQ.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivo_psp.PSP_MQ.dtos.PaymentDataDto;
import com.vivo_psp.PSP_MQ.dtos.SubscriptionDto;
import com.vivo_psp.PSP_MQ.dtos.SubscriptionRequest;
import com.vivo_psp.PSP_MQ.models.ApiKeyMessage;
import com.vivo_psp.PSP_MQ.models.RequestMessage;
import com.vivo_psp.PSP_MQ.models.SubscriptionMessage;
import com.vivo_psp.PSP_MQ.configs.MQConfig;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/publishSendRequest/{sessionId}")
    public ResponseEntity<PaymentDataDto> publishSendRequest(@RequestHeader Map<String, String> headers,
                                                             @PathVariable Long sessionId){
        RequestMessage message = new RequestMessage(
                UUID.randomUUID().toString(),
                headers.get("authorization"),
                sessionId,
                new Date());

        Object response = template.convertSendAndReceive(
                MQConfig.EXCHANGE_REQUEST,
                MQConfig.ROUTING_KEY_REQUEST,
                message
        );

        if (response instanceof PaymentDataDto) {
            return ResponseEntity.ok((PaymentDataDto) response);
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
}
