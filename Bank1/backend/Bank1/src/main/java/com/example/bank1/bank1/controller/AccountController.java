package com.example.bank1.bank1.controller;

import com.example.bank1.bank1.dto.UserIdentificationDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.CardType;
import com.example.bank1.bank1.model.User;
import com.example.bank1.bank1.repository.UserRepository;
import com.example.bank1.bank1.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank1/accounts")
public class AccountController {
    private final AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/validateData")
    public ResponseEntity<String> validateData(UserIdentificationDto userIdentificationDto) {
        User user = userRepository.findById(userIdentificationDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Account account = user.getAccount();
        if (!accountService.validateData(userIdentificationDto, account)) {
            return new ResponseEntity<>("An error occurred", HttpStatus.BAD_REQUEST);
        }
        if (!accountService.validateName(user.getName(), userIdentificationDto.getCardHolderName())) {
            return new ResponseEntity<>("An error occurred", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Response OK", HttpStatus.OK);
    }

    @GetMapping("/validatePAN/{PAN}")
    public ResponseEntity<Long> validatePAN(@PathVariable Long PAN) {
        Long number = accountService.validatePAN(PAN);
        return ResponseEntity.ok(number);
    }

    @GetMapping("/checkCardType/{PAN}")
    public ResponseEntity<?> checkCardType(@PathVariable Long PAN) {
        CardType type = accountService.checkCardType(PAN);
        if (type != null) {
            return ResponseEntity.ok(type);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Card type not found for the given PAN: " + PAN);
        }
    }
}
