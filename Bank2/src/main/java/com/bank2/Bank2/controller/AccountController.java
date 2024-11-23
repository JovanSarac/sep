package com.bank2.Bank2.controller;

import com.bank2.Bank2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bank2/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    /*@PostMapping("/checkValidPAN")
    public ResponseEntity<Boolean> isPANValid(@RequestBody Long PAN){
        var response = accountService.isValidPan(PAN);

        return ResponseEntity.ok(response);
    }*/
}
