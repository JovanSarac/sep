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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:4202")
    @PostMapping("/validateData")
    public ResponseEntity<?> validateData(@org.jetbrains.annotations.NotNull @RequestBody UserIdentificationDto userIdentificationDto) {
        //ovde ako su iste banke dalje treba pcc
        if (accountService.checkBanks(userIdentificationDto)) {
            Account account = accountService.getAccountByPAN(userIdentificationDto.getPAN());
            User user = userRepository.findUserByAccount_Id(account.getId());
            if (!accountService.validateData(userIdentificationDto, account)) {
                return ResponseEntity.ok("pogresno");
            }
            if (!accountService.validateName(user.getName(), userIdentificationDto.cardHolderName)) {
                return ResponseEntity.ok("pogresno");
            }

            String result = accountService.sameBanks(userIdentificationDto);
            return ResponseEntity.ok(result);
        } else {
            //vrv treba da se returna negde ne znam
            accountService.differentBanks(userIdentificationDto);
        }
        //return ResponseEntity.ok("{\"message\": \"uspesno\"}");
        return ResponseEntity.ok("uspesno");
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
