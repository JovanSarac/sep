package com.example.bank1.bank1.controller;

import com.example.bank1.bank1.dto.AnswerPCCDto;
import com.example.bank1.bank1.dto.PCCRequestDto;
import com.example.bank1.bank1.dto.RequestDto;
import com.example.bank1.bank1.service.TransactionService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/PCCRequest")
    public ResponseEntity<?> PCCRequest(@RequestBody AnswerPCCDto answerPCCDto) {
        transactionService.finishTransaction(answerPCCDto);
        return ResponseEntity.ok("Zavrsena transakcija");
    }

    @PostMapping("/PCCIssuer")
    public ResponseEntity<String> PCCIssuer(@RequestBody AnswerPCCDto answerPCCDto){
        transactionService.finishTransactionIssuer(answerPCCDto);
        return  ResponseEntity.ok("");
    }
}
