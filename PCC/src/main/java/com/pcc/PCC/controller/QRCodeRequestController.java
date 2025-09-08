package com.pcc.PCC.controller;

import com.pcc.PCC.dto.BankResponse;
import com.pcc.PCC.dto.QRCodeRequestDto;
import com.pcc.PCC.dto.RequestDto;
import com.pcc.PCC.service.QRCodeRequestService;
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
@RequestMapping("/api/pcc/qrCodeRequests")
public class QRCodeRequestController {
    @Autowired
    public RestTemplate qrCodeRestTemplate;
    @Autowired
    QRCodeRequestService qrCodeRequestService;

    @PostMapping("/checkAndRoute")
    public ResponseEntity<String> checkAndRouteQRCode(@RequestBody QRCodeRequestDto qrCodeRequestDto) {
        //treba dodati proveru da li su validni podaci
        //if(!requestService.isDataValid(requestDto)) return ResponseEntity.badRequest().body("{\"message\": \"Invalid card info\"}");
        QRCodeRequestDto qrCodeRequest = qrCodeRequestService.create(qrCodeRequestDto);

        String url = qrCodeRequestService.isBank1(qrCodeRequestDto.buyerAccountNumber) ?
                "https://localhost:8091/api/bank1/transactions/PCCRequestQRCode" //ovde treba vrv menjati header
                :"https://localhost:8092/api/bank2/transactions/PCCRequestQRCode";

        HttpHeaders headers = new HttpHeaders();
        var requestEntity = new HttpEntity<>(qrCodeRequestService.isBank1(qrCodeRequestDto.buyerAccountNumber) ?
                new BankResponse() : qrCodeRequestDto, headers);
        var method = HttpMethod.POST;

        try {
            String response = qrCodeRestTemplate.exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }

        return ResponseEntity.ok("{\"message\": \"Request saved and routed\"}");
    }
}
