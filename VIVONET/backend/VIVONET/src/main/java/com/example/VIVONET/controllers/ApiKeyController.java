package com.example.VIVONET.controllers;

import com.example.VIVONET.dtos.ApiKeyDto;
import com.example.VIVONET.models.ApiKey;
import com.example.VIVONET.services.ApiKeyService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api")
public class ApiKeyController {
    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping("/apiKey")
    //@PreAuthorize("hasAnyRole('ROLE_BUSINESS_USER', 'ROLE_PERSONAL_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> saveApiKey(@RequestBody ApiKeyDto apiKeyDto){
         ApiKey apiKey = apiKeyService.create(apiKeyDto);

         return ResponseEntity.ok("sacuvan apiKey");
    }
}
