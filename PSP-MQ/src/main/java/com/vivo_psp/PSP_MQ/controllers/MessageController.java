package com.vivo_psp.PSP_MQ.controllers;

import com.vivo_psp.PSP_MQ.dtos.SubscriptionDto;
import com.vivo_psp.PSP_MQ.dtos.SubscriptionRequest;
import com.vivo_psp.PSP_MQ.models.RequestMessage;
import com.vivo_psp.PSP_MQ.models.SubscriptionMessage;
import com.vivo_psp.PSP_MQ.configs.MQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/publishCreateSubscription")
    public String publishCreateSubscription(@RequestHeader Map<String, String> headers,
            @RequestBody SubscriptionRequest subscriptionRequestDto){

        SubscriptionMessage message = new SubscriptionMessage("CREATE" + UUID.randomUUID().toString(),
                headers.get("authorization"),
                subscriptionRequestDto,
                new Date());
        template.convertAndSend(MQConfig.EXCHANGE_SUBSCRIPTION,
                MQConfig.ROUTING_KEY_SUBSCRIPTION, message);

        return "Message published";
    }

    @PutMapping("/publishUpdateSubscription")
    public String publishUpdateSubscription(@RequestHeader Map<String, String> headers,
                                            @RequestBody SubscriptionDto subscriptionDto){
        SubscriptionMessage message = new SubscriptionMessage("UPDATE" + UUID.randomUUID().toString(),
                headers.get("authorization"),
                subscriptionDto,
                new Date());
        template.convertAndSend(MQConfig.EXCHANGE_SUBSCRIPTION,
                MQConfig.ROUTING_KEY_SUBSCRIPTION, message);

        return "Message published";
    }

    @GetMapping("/publishSendRequest/{sessionId}")
    public String publishSendRequest(@RequestHeader Map<String, String> headers,
                                      @PathVariable Long sessionId){
        RequestMessage message = new RequestMessage(UUID.randomUUID().toString(),
                headers.get("authorization"),
                sessionId,
                new Date());

        template.convertAndSend(MQConfig.EXCHANGE_REQUEST,
                MQConfig.ROUTING_KEY_REQUEST, message);

        return "Message published";
    }
}
