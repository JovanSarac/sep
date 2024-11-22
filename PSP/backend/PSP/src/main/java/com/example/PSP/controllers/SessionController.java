package com.example.PSP.controllers;

import com.example.PSP.dtos.SubscriptionDto;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Session;
import com.example.PSP.services.SessionService;
import com.example.PSP.services.SubscriptionService;
import com.example.PSP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class SessionController {
    private final SubscriptionService subscriptionService;
    private final SessionService sessionService;

    @Autowired
    public SessionController(SubscriptionService subscriptionService, SessionService sessionService) {
        this.subscriptionService = subscriptionService;
        this.sessionService = sessionService;
    }

    @GetMapping("/active_pspservices_bysession/{id}")
    public ResponseEntity<?> getActiveSubscriptions(@PathVariable Long id) {
        try {
            Session session = sessionService.getSessionById(id);
            if (session != null) {
                List<SubscriptionDto> activeSubscriptionsForWebShop = subscriptionService.getActiveSubscriptionsByUserId(session.getUser().getId());

                List<PSPService> pspServices = activeSubscriptionsForWebShop.stream()
                        .map(SubscriptionDto::getService)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(pspServices);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving session");
        }
    }
}
