package com.example.bank1.bank1.controller;

import com.example.bank1.bank1.dto.PaymentDataDto;
import com.example.bank1.bank1.dto.RequestDto;
import com.example.bank1.bank1.dto.UserIdentificationDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.User;
import com.example.bank1.bank1.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank1/requests")
public class RequestController {
    private final RequestService requestService;
    @PostMapping("/validateRequest")
    public ResponseEntity<PaymentDataDto> validateRequest(@RequestBody RequestDto requestDto) {
        //treba da se vrati payment_url i payment_id, ne url ce mozda da bude na koju se banku odnosi nmp jos
        //ovde proveri podatke u requestDto
        //i ako je sve dobro vrati ok
        Boolean validData = requestService.checkRequestData(requestDto);
        PaymentDataDto paymentDataDto = new PaymentDataDto();
        if (validData) {
            paymentDataDto.paymentId = -1L;
            paymentDataDto.paymentUrl = "http://localhost:4202/";
            return ResponseEntity.ok(paymentDataDto);
        }
        return (ResponseEntity<PaymentDataDto>) ResponseEntity.badRequest();
    }
}
