package com.bank2.Bank2.controller;

import com.bank2.Bank2.dto.RequestDto;
import com.bank2.Bank2.model.Account;
import com.bank2.Bank2.model.User;
import com.bank2.Bank2.repository.UserRepository;
import com.bank2.Bank2.service.AccountService;
import com.bank2.Bank2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank2/transactions")
public class TransactionController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/PCCRequest")
    public ResponseEntity<String> PCCRequest(@RequestBody RequestDto requestDto){
        Account account = accountService.getAccountByPAN(requestDto.PAN);
        User user = userRepository.findUserByAccount_Id(account.getId());

        if (!accountService.validateData(requestDto, account)) {
            return ResponseEntity.ok("Invalidate card data");
        }
        if (!accountService.validateName(user.getName(), requestDto.cardHolderName)) {
            return ResponseEntity.ok("Invalidate name");
        }

        accountService.reserveFunds(requestDto);

        return ResponseEntity.ok("");
    }

    @PostMapping("/PCCResponse")
    public ResponseEntity<String> PCCResponse(){
        return ResponseEntity.ok("");
    }

}
