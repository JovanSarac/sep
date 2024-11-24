package com.example.PSP.controllers;

import com.example.PSP.dtos.RequestDto;
import com.example.PSP.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/psp/requests")
public class RequestController {
    @Autowired
    RestTemplate restTemplate;

    private final SessionService sessionService;

    @GetMapping("/sendRequest/{sessionId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> sendRequest(@PathVariable Long sessionId) {
        //formira se objekat request
        //ocekuje se rezultat da bude objekat koji ce imati payment_url i payment_id
        //ili mozda ne mora to da bude odg
        //mora prvo create subscription da se uradi
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        RequestDto requestDto = sessionService.createRequestBySession(sessionId);
        HttpEntity<RequestDto> entity = new HttpEntity<RequestDto>(requestDto, headers);

        restTemplate.exchange("http://localhost:8080/bank1", HttpMethod.POST, entity, String.class).getBody();
        return new ResponseEntity<>("Response OK", HttpStatus.OK);
    }
}
