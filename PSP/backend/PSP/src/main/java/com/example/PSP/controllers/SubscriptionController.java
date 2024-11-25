package com.example.PSP.controllers;

import com.example.PSP.dtos.SubscriptionRequest;
import com.example.PSP.models.ApiKey;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Subscription;
import com.example.PSP.models.User;
import com.example.PSP.repositories.PSPServiceRepository;
import com.example.PSP.repositories.SubscriptionRepository;
import com.example.PSP.repositories.UserRepository;
import com.example.PSP.exceptions.ResourceNotFoundException;
import com.example.PSP.exceptions.BadRequestException;
import com.example.PSP.services.ApiKeyService;
import com.example.PSP.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.PSP.dtos.SubscriptionDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    private PSPServiceRepository pspServiceRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/user/create_subscription")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody SubscriptionRequest request) {
        PSPService service = pspServiceRepository.findById(request.getServiceId()).orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean activeSubscriptionExists = subscriptionRepository.existsByServiceAndUserAndIsActive(service, user, true);

        if (activeSubscriptionExists) {
            throw new BadRequestException("You already have an active subscription to this payment service.");
        }

        SubscriptionDto subscription = subscriptionService.createSubscription(user, service, request.getSubscriptionDuration());
        ApiKey apiKey = apiKeyService.create();

        subscription.setMerchantId(apiKey.getMerchantId());
        subscription.setMerchantPassword(apiKey.getMerchantPassword());
        subscription.setPaymentServiceId(request.getServiceId());

        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @GetMapping("/user_active_subscription/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<SubscriptionDto> getActiveSubscriptionsByUserId(@PathVariable Long userId) {
        return subscriptionService.getActiveSubscriptionsByUserId(userId);
    }

    @PutMapping("/user/update_subscription")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> updateSubscription(@RequestBody SubscriptionDto subscriptionDTO) {
        try {
            subscriptionService.updateSubscription(subscriptionDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Subscription updated successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
