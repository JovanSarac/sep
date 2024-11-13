package com.example.PSP.controllers;

import com.example.PSP.dtos.RegistrationDto;
import com.example.PSP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto) {
        String registrationStatus = userService.registerUser(registrationDto);
        if (registrationStatus.equals("Registration successful!")) {
            return ResponseEntity.ok(registrationStatus);
        }
        return ResponseEntity.badRequest().body(registrationStatus);
    }
}
