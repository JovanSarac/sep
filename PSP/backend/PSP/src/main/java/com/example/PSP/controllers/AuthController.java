package com.example.PSP.controllers;

import com.example.PSP.dtos.AccessToken;
import com.example.PSP.dtos.CredentialDto;
import com.example.PSP.dtos.GenericReposnse;
import com.example.PSP.dtos.RegistrationDto;
import com.example.PSP.models.User;
//import com.example.PSP.security.jwt.JwtUtils;
import com.example.PSP.security.jwt.TokenRefreshRequest;
import com.example.PSP.security.services.UserDetailsImpl;
import com.example.PSP.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
//    @Autowired
//    JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody CredentialDto loginRequest) {
        String tokenUrl = "http://localhost:8080/realms/sep-realm/protocol/openid-connect/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "sep-psp-backend");
        params.add("client_secret", "wxjosAftVRKXRykM3AgEqnXSHvP1muI7");
        params.add("grant_type", "password");
        params.add("username", loginRequest.getUsername());
        params.add("password", loginRequest.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody()); // access_token, refresh_token, expires_in...
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Login failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Keycloak login error: " + e.getMessage());
        }
//        String username = loginRequest.getUsername();
//
//        if (userService.isAccountLocked(username)) {
//            return ResponseEntity.status(423).body("Account is locked. Try again later");
//        }
//        try {
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//            userService.resetFailedAttempts(username);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//            ResponseCookie refreshCookie = jwtUtils.generateRefreshToken(userDetails);
//
//            String jwtSource = jwtCookie.toString().split("=")[1].split(";")[0];
//            String refreshSource = refreshCookie.toString().split("=")[1].split(";")[0];
//
//            var user = userService.getUserByUsername(loginRequest.getUsername());
//            userService.updateUserRefreshToken(user, refreshSource);
//
//            logger.info("User " + loginRequest.getUsername() + " logged in successfully");
//            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtSource)
//                    .body(new AccessToken(userDetails.getId(), jwtSource, refreshSource));
//        } catch (Exception ex) {
//            userService.increaseFailedAttempts(username);
//            return ResponseEntity.status(401).body("Invalid username or password");
//        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto) {
        try
        {
            String adminTokenUrl = "http://localhost:8080/realms/master/protocol/openid-connect/token";

            // 1. Uzmi admin token
            MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
            tokenParams.add("client_id", "admin-cli");
            tokenParams.add("grant_type", "password");
            tokenParams.add("username", "admin");
            tokenParams.add("password", "admin");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(adminTokenUrl, new HttpEntity<>(tokenParams, headers), Map.class);
            String adminAccessToken = (String) tokenResponse.getBody().get("access_token");

            // 2. Napravi user-a
            String createUserUrl = "http://localhost:8080/admin/realms/sep-realm/users";

            headers = new HttpHeaders();
            headers.setBearerAuth(adminAccessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> newUser = Map.of(
                    "username", registrationDto.getUsername(),
                    "email", registrationDto.getEmail(),
                    "firstName", registrationDto.getFirstName(),
                    "lastName", registrationDto.getLastName(),
                    "enabled", true,
                    "credentials", new Object[] {
                            Map.of("type", "password", "value", registrationDto.getPassword(), "temporary", false)
                    }
            );

            ResponseEntity<String> response = restTemplate.postForEntity(createUserUrl, new HttpEntity<>(newUser, headers), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Ako je 2xx, vrati OK ili Created status
                String registrationStatus = userService.registerUser(registrationDto);
                return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful!");
            } else {
                // Ako nije 2xx, vrati body koji je došao iz Keycloak-a
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception ex)
        {
            return ResponseEntity.status(500).body("Keycloak registration error: " + ex.getMessage());
        }
//        String registrationStatus = userService.registerUser(registrationDto);
//        if (registrationStatus.equals("Registration successful!")) {
//            return ResponseEntity.ok(registrationStatus);
//        }
//        return ResponseEntity.badRequest().body(registrationStatus);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody TokenRefreshRequest request) {
        String logoutUrl = "http://localhost:8080/realms/sep-realm/protocol/openid-connect/logout";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "sep-psp-backend");
        params.add("client_secret", "wxjosAftVRKXRykM3AgEqnXSHvP1muI7");
        params.add("refresh_token", request.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(logoutUrl, new HttpEntity<>(params, headers), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("You've been signed out!");
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Keycloak logout error: " + e.getMessage());
        }
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body("You've been signed out!");
    }

    private static final SecureRandom random = new SecureRandom();

    public static String generateEightDigitCode() {
        int number = 10000000 + random.nextInt(90000000);
        return String.valueOf(number);// ensures 8 digits
    }

    @PostMapping("/send/code")
    public ResponseEntity<?> sendCodeUser(@Valid @RequestBody CredentialDto loginRequest) throws MessagingException {
        if (userService.isAccountLocked(loginRequest.getUsername())) {
            return ResponseEntity.status(423).body("Account is locked. Try again later");
        }

        var user = userService.getUserByUsername(loginRequest.getUsername());
        var email = user.getEmail();

        var code = generateEightDigitCode();
        userService.updateUserCode(code, user);
//TODO uncomment this
//        MimeMessage message = mailSender.createMimeMessage();
//        message.setFrom(fromEmail);
//        message.setRecipients(MimeMessage.RecipientType.TO, email);
//        message.setSubject("APP CODE");
//        message.setText(code);
//        mailSender.send(message);

        GenericReposnse resp = new GenericReposnse(true,"OK");
        return ResponseEntity.ok().body(resp);
    }

//    @PostMapping("/login/code")
//    public ResponseEntity<?> loginCodeUser(@Valid @RequestBody CredentialDto loginRequest) throws MessagingException {
//        if (userService.isAccountLocked(loginRequest.getUsername())) {
//            return ResponseEntity.status(423).body("Account is locked. Try again later");
//        }
//
//        var user = userService.validateCode(loginRequest);
//        if(user==null){
//            return ResponseEntity.badRequest().body("Code has expired");
//        }
//
//        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
//
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//        ResponseCookie refreshCookie = jwtUtils.generateRefreshToken(userDetails);
//
//        String jwtSource = jwtCookie.toString().split("=")[1].split(";")[0];
//        String refreshSource = refreshCookie.toString().split("=")[1].split(";")[0];
//
//        userService.updateUserRefreshToken(user, refreshSource);
//
//        logger.info("User " + loginRequest.getUsername() + " logged in successfully");
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtSource)
//                .body(new AccessToken(userDetails.getId(), jwtSource, refreshSource));
//    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String tokenUrl = "http://localhost:8080/realms/sep-realm/protocol/openid-connect/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "sep-psp-backend");
        params.add("client_secret", "wxjosAftVRKXRykM3AgEqnXSHvP1muI7");
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", request.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, httpRequest, Map.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Keycloak refresh error: " + e.getMessage());
        }
//        String requestToken = request.getRefreshToken();
//
//        try {
//            if(!jwtUtils.validateJwtToken(requestToken)){
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
//            }
//
//            Long id = jwtUtils.getUserIdFromJwtToken(requestToken);
//
//            User user = userService.getUserById(id);
//            if(user == null){
//                throw new RuntimeException("Refresh token not found");
//            }
//
//            if(!userService.validateRefresh(requestToken,user)){
//                throw new RuntimeException("Refresh token has expired please log in");
//            }
//
//            ResponseCookie newAccessToken = jwtUtils.generateJwtCookie(UserDetailsImpl.build(user));
//            String accessSource = newAccessToken.toString().split("=")[1].split(";")[0];
//
//            return ResponseEntity.ok(new AccessToken(user.getId(), accessSource, null));
//        } catch (JwtException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
//        }
    }
}
