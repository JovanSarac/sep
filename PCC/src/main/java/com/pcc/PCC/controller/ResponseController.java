package com.pcc.PCC.controller;

import com.pcc.PCC.dto.BankResponse;
import com.pcc.PCC.model.Request;
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
@RequestMapping("/api/pcc/responses")
public class ResponseController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RequestService requestService;

    @PostMapping("/forward")
    public ResponseEntity<String> forward(@RequestBody BankResponse bankResponse){
        Request request = requestService.findByAcquirerOrderId(bankResponse.acquirerOrderId);

        String url = requestService.isBank1(request.getPAN()) ?
                "http://localhost:8091/api/bank1/transactions/PCCRequest" : "http://localhost:8092/api/bank2/transactions/PCCResponse";

        HttpHeaders headers = new HttpHeaders();
        var requestEntity = new HttpEntity<>(bankResponse, headers);
        var method = HttpMethod.POST;

        try {
            String response = restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }

        return ResponseEntity.ok("");
    }
}
