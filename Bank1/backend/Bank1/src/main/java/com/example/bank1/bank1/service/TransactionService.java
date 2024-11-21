package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.TransactionDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.repository.AccountRepository;
import com.example.bank1.bank1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Boolean checkAccountBalance(TransactionDto transactionDto) {
        Account account = accountRepository.getAccountByAccountNumber(transactionDto.getSourceAccountNumber());
        if (account.getBalance() - transactionDto.getAmount() < 0) {
            return false;
        }
        return true;
    }
}
