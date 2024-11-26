package com.bank2.Bank2.controller;

import com.bank2.Bank2.dto.RequestDto;
import com.bank2.Bank2.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank2/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/PCCRequest")
    public ResponseEntity<String> PCCRequest(@RequestBody RequestDto requestDto){
        return ResponseEntity.ok("");
    }

}
