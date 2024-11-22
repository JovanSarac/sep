package com.example.PSP.services;

import com.example.PSP.dtos.CartDto;
import com.example.PSP.models.Session;
import com.example.PSP.models.User;
import com.example.PSP.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;



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

}