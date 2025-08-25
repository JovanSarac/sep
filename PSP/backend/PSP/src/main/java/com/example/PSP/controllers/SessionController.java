package com.example.PSP.controllers;

import com.example.PSP.dtos.SubscriptionDto;
import com.example.PSP.dtos.SessionDto;
import com.example.PSP.dtos.CartItemDto;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Session;
import com.example.PSP.services.SessionService;
import com.example.PSP.services.SubscriptionService;
import com.example.PSP.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

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
                return ResponseEntity.ok(pspServices.stream()
                        .filter(s -> s.getActive())
                        .toList());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
            }
        } catch (Exception e) {
            logger.error("Error retrieving session: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving session");
        }
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Long id){
        try{
            Session session = sessionService.getSessionById(id);
            SessionDto sessionDto = new SessionDto();
            for(CartItemDto cartItemDto : session.getCart().getItems()){
                sessionDto.itemNames.add(cartItemDto.getName());
            }
            sessionDto.totalPrice = session.getCart().getTotalPrice();
            return ResponseEntity.ok(sessionDto);
        } catch (Exception e){
            logger.error("Error retrieving session: ", e.getMessage());
            System.out.println("Error retrieving session: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving session");
        }
    }
}
