package com.example.VIVONET.controllers;

import com.example.VIVONET.dtos.AccessToken;
import com.example.VIVONET.dtos.CredentialDto;
import com.example.VIVONET.dtos.RegistrationDto;
import com.example.VIVONET.security.jwt.JwtUtils;
import com.example.VIVONET.security.services.UserDetailsImpl;
import com.example.VIVONET.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody CredentialDto loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        String jwtSource = jwtCookie.toString().split("=")[1].split(";")[0];
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtSource)
                .body(new AccessToken(userDetails.getId(), jwtSource));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto) {
        String registrationStatus = userService.registerUser(registrationDto);
        if (registrationStatus.equals("Registration successful!")) {
            return ResponseEntity.ok(registrationStatus);
        }
        return ResponseEntity.badRequest().body(registrationStatus);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }
}
