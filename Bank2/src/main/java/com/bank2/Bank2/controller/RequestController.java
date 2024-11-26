package com.bank2.Bank2.controller;

import com.bank2.Bank2.dto.PaymentDataDto;
import com.bank2.Bank2.dto.RequestDto;
import com.bank2.Bank2.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank2/requests")
public class RequestController {
    private final RequestService requestService;
    @PostMapping("/validateRequest")
    public ResponseEntity<PaymentDataDto> validateRequest(@RequestBody RequestDto requestDto) {
        //treba da se vrati payment_url i payment_id, ne url ce mozda da bude na koju se banku odnosi nmp jos
        //ovde proveri podatke u requestDto
        //i ako je sve dobro vrati ok

        /*Boolean validData = requestService.checkRequestData(requestDto);
        PaymentDataDto paymentDataDto = new PaymentDataDto();
        if (validData) {
            paymentDataDto.paymentId = -1L;
            paymentDataDto.paymentUrl = "http://localhost:4202/";
            return ResponseEntity.ok(paymentDataDto);
        }*/
        return (ResponseEntity<PaymentDataDto>) ResponseEntity.badRequest();
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> transaction(){
        return ResponseEntity.ok("{\"message\": \"Uspesno\"}");
    }
}
