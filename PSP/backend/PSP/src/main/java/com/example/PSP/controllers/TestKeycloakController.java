package com.example.PSP.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tests")
public class TestKeycloakController {
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Public endpoint – no auth required!";
    }

    @GetMapping("/user/hello")
    public String userHello() {
        return "Hello, authenticated user!";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hello, admin!";
    }
}
