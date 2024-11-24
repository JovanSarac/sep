package com.example.bank1.bank1.controller;

import com.example.bank1.bank1.dto.RequestDto;
import com.example.bank1.bank1.dto.UserIdentificationDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank1/requests")
public class RequestController {
    @GetMapping("/validateRequest")
    public ResponseEntity<String> validateRequest() {
        //treba da se vrati payment_url i payment_id, ne url ce mozda da bude na koju se banku odnosi nmp jos
        //ovde proveri podatke u requestDto
        //i ako je sve dobro vrati ok
        return new ResponseEntity<>("Response OK", HttpStatus.OK);
    }
}