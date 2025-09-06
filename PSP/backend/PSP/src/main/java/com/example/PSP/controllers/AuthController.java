package com.example.PSP.controllers;

import com.example.PSP.dtos.AccessToken;
import com.example.PSP.dtos.CredentialDto;
import com.example.PSP.dtos.RegistrationDto;
import com.example.PSP.models.User;
import com.example.PSP.security.jwt.JwtUtils;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
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
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody CredentialDto loginRequest) {
        String username = loginRequest.getUsername();

        if (userService.isAccountLocked(username)) {
            return ResponseEntity.status(423).body("Account is locked. Try again later");
        }
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            userService.resetFailedAttempts(username);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            ResponseCookie refreshCookie = jwtUtils.generateRefreshToken(userDetails);

            String jwtSource = jwtCookie.toString().split("=")[1].split(";")[0];
            String refreshSource = refreshCookie.toString().split("=")[1].split(";")[0];

            var user = userService.getUserByUsername(loginRequest.getUsername());
            userService.updateUserRefreshToken(user, refreshSource);

            logger.info("User " + loginRequest.getUsername() + " logged in successfully");
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtSource)
                    .body(new AccessToken(userDetails.getId(), jwtSource, refreshSource));
        } catch (Exception ex) {
            userService.increaseFailedAttempts(username);
            return ResponseEntity.status(401).body("Invalid username or password");
        }
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

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(fromEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("APP CODE");
        message.setText(code);
        mailSender.send(message);

        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/login/code")
    public ResponseEntity<?> loginCodeUser(@Valid @RequestBody CredentialDto loginRequest) throws MessagingException {
        if (userService.isAccountLocked(loginRequest.getUsername())) {
            return ResponseEntity.status(423).body("Account is locked. Try again later");
        }

        var user = userService.validateCode(loginRequest);
        if(user==null){
            return ResponseEntity.badRequest().body("Code has expired");
        }

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        ResponseCookie refreshCookie = jwtUtils.generateRefreshToken(userDetails);

        String jwtSource = jwtCookie.toString().split("=")[1].split(";")[0];
        String refreshSource = refreshCookie.toString().split("=")[1].split(";")[0];

        userService.updateUserRefreshToken(user, refreshSource);

        logger.info("User " + loginRequest.getUsername() + " logged in successfully");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtSource)
                .body(new AccessToken(userDetails.getId(), jwtSource, refreshSource));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();

        try {
            if(!jwtUtils.validateJwtToken(requestToken)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            Long id = jwtUtils.getUserIdFromJwtToken(requestToken);

            User user = userService.getUserById(id);
            if(user == null){
                throw new RuntimeException("Refresh token not found");
            }

            if(!userService.validateRefresh(requestToken,user)){
                throw new RuntimeException("Refresh token has expired please log in");
            }

            ResponseCookie newAccessToken = jwtUtils.generateJwtCookie(UserDetailsImpl.build(user));
            String accessSource = newAccessToken.toString().split("=")[1].split(";")[0];

            return ResponseEntity.ok(new AccessToken(user.getId(), accessSource, null));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
