package com.example.VIVONET.controllers;

import com.example.VIVONET.dtos.UserInfoDto;
import com.example.VIVONET.exceptions.ResourceNotFoundException;
import com.example.VIVONET.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ROLE_BUSINESS_USER', 'ROLE_PERSONAL_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserInfoDto> getUserInfoById(@PathVariable Long id) {
        UserInfoDto user = userService.getUserInfoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        List<UserInfoDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
