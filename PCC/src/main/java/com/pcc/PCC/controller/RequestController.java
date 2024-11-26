package com.pcc.PCC.controller;

import com.pcc.PCC.dto.RequestDto;
import com.pcc.PCC.dto.SameBankRequestDto;
import com.pcc.PCC.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pcc/requests")
public class RequestController {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<RequestDto> create(@RequestBody RequestDto requestDto){
        RequestDto request = requestService.create(requestDto);

        return ResponseEntity.ok(request);
    }

    @PostMapping("/checkAndRoute")
    public ResponseEntity<String> checkAndRoute(@RequestBody RequestDto requestDto){
        if(!requestService.isDataValid(requestDto)) return ResponseEntity.badRequest().body("{\"message\": \"Invalid card info\"}");

        RequestDto request = requestService.create(requestDto);

        String url = requestService.isBank1(requestDto.PAN) ?
                "http://localhost:8091/api/bank1/transactions/PCCRequest"
                :"http://localhost:8092/api/bank2/transaction";

        HttpHeaders headers = new HttpHeaders();
        var requestEntity = new HttpEntity<>(requestService.isBank1(requestDto.PAN) ?
                new SameBankRequestDto(requestDto.PAN,
                        requestDto.securityCode,
                        requestDto.cardHolderName,
                        requestDto.cardExpirationDate,
                        requestDto.amount) : requestDto, headers);
        var method = HttpMethod.POST;

        try {
            String response = restTemplate().exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }

        return ResponseEntity.ok("{\"message\": \"Request saved and routed\"}");
    }
}
