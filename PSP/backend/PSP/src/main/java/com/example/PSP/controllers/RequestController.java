package com.example.PSP.controllers;

import com.example.PSP.configs.ApiKeyResponseMessage;
import com.example.PSP.configs.MQConfig;
import com.example.PSP.configs.RequestMessage;
import com.example.PSP.dtos.PaymentDataDto;
import com.example.PSP.dtos.RequestDto;
import com.example.PSP.dtos.RequestPaymentDto;
import com.example.PSP.dtos.RequestQRCodePaymentDto;
import com.example.PSP.models.ApiKey;
import com.example.PSP.services.ApiKeyService;
import com.example.PSP.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/psp/requests")
public class RequestController {
    @Autowired
    RestTemplate restTemplate;

    private final SessionService sessionService;

    @Autowired
    private ApiKeyService apiKeyService;

    private ApiKeyResponseMessage responseMessage;

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @GetMapping("/sendRequestQRCode/{sessionId}")
    @PreAuthorize("permitAll()")
    public RequestQRCodePaymentDto sendRequestQRCode(@PathVariable Long sessionId) {
        logger.info("Processing the QR code request..");
        String url = "https://localhost:9000/publishApiKeyRequest";
        HttpHeaders headersMQ = new HttpHeaders();
        var requestEntity = new HttpEntity<>(-2, headersMQ);
        var method = HttpMethod.POST;
        try {
            String response = restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }

        while(this.responseMessage == null){

        }

        ApiKey apiKey = apiKeyService.findByMerchantId(UUID.fromString(responseMessage.getMerchantId()));
        if(!apiKey.getMerchantPassword().equals(responseMessage.getMerchantPassword())) throw new ResourceAccessException("Invalid apiKey");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        RequestDto requestDto = sessionService.createRequestBySession(sessionId);
        HttpEntity<RequestDto> entity = new HttpEntity<RequestDto>(requestDto, headers);

        ResponseEntity<RequestQRCodePaymentDto> response = restTemplate.exchange("https://localhost:8080/bank1QRCodeValidateRequest", HttpMethod.POST, entity, RequestQRCodePaymentDto.class);
        RequestQRCodePaymentDto requestPaymentQRDto = response.getBody();

        //ovde dodajem string za qr data, posle treba namestiti da se ti podaci uzimaju iz banke prodavca i da
        //se rezultat vrati u requestPaymentDto, al moze da se napravi novi dto za qr kod onda

        return requestPaymentQRDto;
    }


    @GetMapping("/sendRequest/{sessionId}")
    @PreAuthorize("permitAll()")
    public RequestPaymentDto sendRequest(@PathVariable Long sessionId) {
        logger.info("Processing car payment request..");
        //formira se objekat request
        //ocekuje se rezultat da bude objekat koji ce imati payment_url i payment_id
        //ili mozda ne mora to da bude odg
        //mora prvo create subscription da se uradi

        //provera apiKey-a pre redirect-a
        String url = "https://localhost:9000/publishApiKeyRequest";
        HttpHeaders headersMQ = new HttpHeaders();
        var requestEntity = new HttpEntity<>(-1, headersMQ);
        var method = HttpMethod.POST;
        try {
            String response = restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }

        while(this.responseMessage == null){

        }

        ApiKey apiKey = apiKeyService.findByMerchantId(UUID.fromString(responseMessage.getMerchantId()));
        if(!apiKey.getMerchantPassword().equals(responseMessage.getMerchantPassword())) throw new ResourceAccessException("Invalid apiKey");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestDto requestDto = sessionService.createRequestBySession(sessionId);
        HttpEntity<RequestDto> entity = new HttpEntity<RequestDto>(requestDto, headers);

        ResponseEntity<RequestPaymentDto> response = restTemplate.exchange("https://localhost:8080/bank1ValidateRequest", HttpMethod.POST, entity, RequestPaymentDto.class);
        RequestPaymentDto requestPaymentDto = response.getBody();

        //restTemplate.exchange("http://localhost:8080/bank1", HttpMethod.POST, entity, String.class).getBody();
        //return ResponseEntity.ok("{\"message\": \"Uspesno\"}");
        return requestPaymentDto;
    }

    @RabbitListener(queues = MQConfig.QUEUE_APIKEY_RESPONSE)
    public void apiKeyListener(ApiKeyResponseMessage message){

        this.responseMessage = message;
    }

    @GetMapping("/sendRequestCrypto")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ArrayList<String>> sendRequestCrypto() {
        logger.info("Processing crypto request..");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList<String>> response = restTemplate.exchange("https://localhost:8080/eth", HttpMethod.GET, entity, new ParameterizedTypeReference<ArrayList<String>>() {});
        ArrayList<String> walletIds = response.getBody();

        return ResponseEntity.ok(walletIds);
    }
}
