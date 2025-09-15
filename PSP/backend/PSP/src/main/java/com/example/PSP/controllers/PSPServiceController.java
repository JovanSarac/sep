package com.example.PSP.controllers;

import com.example.PSP.models.PSPService;
import com.example.PSP.services.PSPServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class PSPServiceController {
    private final PSPServiceService pspServiceService;
    @Autowired
    private DiscoveryClient discoveryClient;

    public PSPServiceController(PSPServiceService pspServiceService) {
        this.pspServiceService = pspServiceService;
    }

    @GetMapping("/user/active_payment_services")
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PSPService>> getActiveServices() {
        List<String> registeredServices = discoveryClient.getServices()
                .stream()
                .filter(s -> !s.toUpperCase().equals("PSP") && !s.toLowerCase().equals("consul"))
                .toList();

        List<PSPService> allPspServices = pspServiceService.getActiveServices();

        List<PSPService> activeServices = new ArrayList<>();

        for(var service : allPspServices){
            for(var registeredService : registeredServices){
                if(service.getName().toLowerCase().contains(registeredService.toLowerCase())){
                    activeServices.add(service);
                }
            }
        }

        return ResponseEntity.ok(activeServices);
    }
}
