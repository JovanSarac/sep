package com.example.PSP.controllers;

import com.example.PSP.dtos.CheckoutDataDto;
import com.example.PSP.dtos.SubscriptionDto;
import com.example.PSP.dtos.SubscriptionRequest;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Session;
import com.example.PSP.models.User;
import com.example.PSP.services.PSPServiceService;
import com.example.PSP.services.SessionService;
import com.example.PSP.services.SubscriptionService;
import com.example.PSP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class CheckoutDataController {

    private final SubscriptionService subscriptionService;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public CheckoutDataController(SubscriptionService subscriptionService, SessionService sessionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.sessionService = sessionService;
        this.userService = userService;
    }
    @PostMapping("/checking_webshop_services")
    public ResponseEntity<?> checkingWebShopServices(@RequestBody CheckoutDataDto request) {
        User user = userService.getUserById(request.getWebShopId());
        if(!user.getCompanyName().equals(request.getWebShopName()) || !user.getWebURL().equals(request.getWebShopUrl())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The provided webshop details do not match our records. Please verify the webshop name and URL.");
        }
        List<SubscriptionDto> activeSubscriptionsForWebShop = subscriptionService.getActiveSubscriptionsByUserId(request.getWebShopId());
        if (activeSubscriptionsForWebShop.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No active subscriptions found for this webshop.");
        }

        Session session = sessionService.createSession(
                request.getUserId(),
                request.getUserFirstName(),
                request.getUserLastName(),
                request.getUserEmail(),
                request.getCart(),
                user
        );


        String redirectUrl = "http://localhost:4201/available-service/" + session.getId();

        return ResponseEntity.ok(redirectUrl);
    }
}
