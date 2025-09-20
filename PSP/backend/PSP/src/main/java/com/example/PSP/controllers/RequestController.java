package com.example.PSP.controllers;

import com.example.PSP.configs.ApiKeyResponseMessage;
import com.example.PSP.configs.MQConfig;
import com.example.PSP.configs.RequestMessage;
import com.example.PSP.dtos.*;
import com.example.PSP.models.ApiKey;
import com.example.PSP.models.Session;
import com.example.PSP.services.ApiKeyService;
import com.example.PSP.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @GetMapping("/sendRequestQRCode/{sessionId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_BUSINESS_USER', 'ROLE_PERSONAL_USER', 'ROLE_WEB_SHOP')")
    public RequestQRCodePaymentDto sendRequestQRCode(@PathVariable Long sessionId, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Processing the QR code request..");
        String token = authorizationHeader.replace("Bearer ", "").trim();
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
        headers.setBearerAuth(token);
        RequestDto requestDto = sessionService.createRequestBySession(sessionId);
        HttpEntity<RequestDto> entity = new HttpEntity<RequestDto>(requestDto, headers);

        ResponseEntity<RequestQRCodePaymentDto> response = restTemplate.exchange(apiGatewayUrl + "/bank1QRCodeValidateRequest", HttpMethod.POST, entity, RequestQRCodePaymentDto.class);

        RequestQRCodePaymentDto requestPaymentQRDto = response.getBody();

        //ovde dodajem string za qr data, posle treba namestiti da se ti podaci uzimaju iz banke prodavca i da
        //se rezultat vrati u requestPaymentDto, al moze da se napravi novi dto za qr kod onda

        return requestPaymentQRDto;
    }


    @GetMapping("/sendRequest/{sessionId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_BUSINESS_USER', 'ROLE_PERSONAL_USER', 'ROLE_WEB_SHOP')")
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

        ResponseEntity<RequestPaymentDto> response = restTemplate.exchange(apiGatewayUrl + "/bank1ValidateRequest", HttpMethod.POST, entity, RequestPaymentDto.class);
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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_BUSINESS_USER', 'ROLE_PERSONAL_USER', 'ROLE_WEB_SHOP')")
    public ResponseEntity<ArrayList<String>> sendRequestCrypto() {
        logger.info("Processing crypto request..");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList<String>> response = restTemplate.exchange(apiGatewayUrl  + "/eth", HttpMethod.GET, entity, new ParameterizedTypeReference<ArrayList<String>>() {});
        ArrayList<String> walletIds = response.getBody();

        return ResponseEntity.ok(walletIds);
    }

    @GetMapping("/sendRequestPaypal")
    @PreAuthorize("permitAll()")
    public ResponseEntity<PaypalPaymentDto> sendRequestPaypal(@RequestParam Long sessionId) {
        logger.info("Processing PayPal request for sessionId: {}", sessionId);

        Session sessionInfo = sessionService.getSessionById(sessionId);


        PaypalRequestDto requestBody = new PaypalRequestDto();
        requestBody.setAmount(sessionInfo.getCart().getTotalPrice().toString());
        requestBody.setCurrency("USD");
        requestBody.setSuccessUrl("https://localhost:4200/transaction-status/success");
        requestBody.setCancelUrl("https://localhost:4200/transaction-status/cancel");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaypalRequestDto> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PaypalPaymentDto> response = restTemplate.exchange(
                apiGatewayUrl + "/paypal/create-order",
                HttpMethod.POST,
                entity,
                PaypalPaymentDto.class
        );

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/capturePaypalOrder")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> capturePaypalOrder(
            @RequestBody PaypalCaptureRequestDto request) {

        logger.info("Capturing PayPal order: {} for PayerID: {}", request.getOrderId(), request.getPayerId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("orderId", request.getOrderId());
        requestBody.put("payerId", request.getPayerId());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<PaypalCaptureDto> response = restTemplate.exchange(
                    apiGatewayUrl + "/paypal/capture-order",
                    HttpMethod.POST,
                    entity,
                    PaypalCaptureDto.class
            );

            // Ako treba snimi transakciju
            // transactionService.savePaypalTransaction(request.getOrderId(), request.getPayerId(), response.getBody());

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            logger.error("Error capturing PayPal order: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
