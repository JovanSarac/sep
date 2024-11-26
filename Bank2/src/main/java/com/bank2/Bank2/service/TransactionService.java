package com.bank2.Bank2.service;

import com.bank2.Bank2.dto.TransactionDto;
import com.bank2.Bank2.model.Account;
import com.bank2.Bank2.repository.AccountRepository;
import com.bank2.Bank2.repository.TransactionRepository;
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
