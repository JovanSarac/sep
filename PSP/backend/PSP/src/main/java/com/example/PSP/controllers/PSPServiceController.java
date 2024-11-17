package com.example.PSP.controllers;

import com.example.PSP.models.PSPService;
import com.example.PSP.services.PSPServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class PSPServiceController {
    private final PSPServiceService pspServiceService;

    public PSPServiceController(PSPServiceService pspServiceService) {
        this.pspServiceService = pspServiceService;
    }

    @GetMapping("/user/active_payment_services")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PSPService>> getActiveServices() {
        List<PSPService> activeServices = pspServiceService.getActiveServices();
        return ResponseEntity.ok(activeServices);
    }
}
