package com.example.PSP.services;

import com.example.PSP.dtos.CartDto;
import com.example.PSP.dtos.RequestDto;
import com.example.PSP.models.ApiKey;
import com.example.PSP.models.Session;
import com.example.PSP.models.User;
import com.example.PSP.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ApiKeyService apiKeyService;


    public Session createSession(Long requestorId, String firstName, String lastName, String email, CartDto cart, User webshop) {
        Session session = new Session();
        session.setRequestorId(requestorId);
        session.setRequestorFirstName(firstName);
        session.setRequestorLastName(lastName);
        session.setRequestorEmail(email);
        session.setCart(cart);
        session.setUser(webshop);
        return sessionRepository.save(session);
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).get();
    }

    public RequestDto createRequestBySession(Long id) {
        Session session = getSessionById(id);
        ApiKey apiKey = apiKeyService.findByWebShopId(session.getUser().getId());

        RequestDto requestDto = new RequestDto();
        requestDto.merchantId = apiKey.getMerchantId();
        requestDto.amount = session.getCart().getTotalPrice();
        //merchantOrderId
        requestDto.merchantOrderId = Math.abs((long) new Random().nextInt());
        //merchantPassword
        requestDto.merchantPassword = apiKey.getMerchantPassword();
        //timestamp
        requestDto.timestamp = new Date().getTime();
        //successUrl vrv ce sve biti url fronta
        requestDto.successUrl = "http://localhost:4200/transaction-status/success";
        //failedUrl
        requestDto.failedUrl = "http://localhost:4200/transaction-status/failed";
        //errorUrl
        requestDto.errorUrl = "http://localhost:4200/transaction-status/error";
        return requestDto;
    }
}