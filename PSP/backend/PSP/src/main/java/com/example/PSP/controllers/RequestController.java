package com.example.PSP.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/psp/requests")
public class RequestController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/sendRequest")
    public ResponseEntity<String> sendRequest() {
        //formira se objekat request
        //ocekuje se rezultat da bude objekat koji ce imati payment_url i payment_id
        //ili mozda ne mora to da bude odg
        //mora prvo create subscription da se uradi
        restTemplate.exchange("http://localhost:8080/bank1", HttpMethod.GET, null, String.class).getBody();
        return new ResponseEntity<>("Response OK", HttpStatus.OK);
    }
}
