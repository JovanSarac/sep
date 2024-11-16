package com.example.PSP.controllers;

import com.example.PSP.dtos.UserInfoDto;
import com.example.PSP.exceptions.ResourceNotFoundException;
import com.example.PSP.models.User;
import com.example.PSP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserInfoDto> getUserInfoById(@PathVariable Long id) {
        UserInfoDto user = userService.getUserInfoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return ResponseEntity.ok(user);
    }

}
