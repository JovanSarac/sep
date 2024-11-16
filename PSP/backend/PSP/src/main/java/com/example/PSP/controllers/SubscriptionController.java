package com.example.PSP.controllers;

import com.example.PSP.dtos.SubscriptionRequest;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Subscription;
import com.example.PSP.models.User;
import com.example.PSP.repositories.PSPServiceRepository;
import com.example.PSP.repositories.UserRepository;
import com.example.PSP.exceptions.ResourceNotFoundException;
import com.example.PSP.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.PSP.dtos.SubscriptionDto;

import java.util.List;

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

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/user/create_subscription")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody SubscriptionRequest request) {
        PSPService service = pspServiceRepository.findById(request.getServiceId()).orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubscriptionDto subscription = subscriptionService.createSubscription(user, service, request.getSubscriptionDuration());

        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @GetMapping("/user_subscription/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<SubscriptionDto> getSubscriptionsByUserId(@PathVariable Long userId) {
        return subscriptionService.getSubscriptionsByUserId(userId);
    }
}
