package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.AnswerPCCDto;
import com.example.bank1.bank1.dto.PCCRequestDto;
import com.example.bank1.bank1.dto.TransactionDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.Transaction;
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
        Account account = accountRepository.findByAccountNumber(transactionDto.getSourceAccountNumber()).get();
        if (account.getBalance() - transactionDto.getAmount() < 0) {
            return false;
        }
        return true;
    }

    public void finishTransaction(AnswerPCCDto answerPCCDto) {
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderIdAAndIssuerOrderId(answerPCCDto.acquirerOrderId, answerPCCDto.issuerOrderId);
            Account account = accountRepository.findByAccountNumber(transaction.getDestinationAccountNumber()).get();
            Double balance = account.getBalance();
            balance = balance + transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
        }
    }

    public void finishTransactionIssuer(AnswerPCCDto answerPCCDto) {
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderIdAAndIssuerOrderId(answerPCCDto.acquirerOrderId, answerPCCDto.issuerOrderId);
            Account account = accountRepository.findByAccountNumber(transaction.getSourceAccountNumber()).get();
            Double balance = account.getBalance();
            balance = balance - transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
        }
    }

    public void finishTransactionBank2ToBank1(AnswerPCCDto answerPCCDto) {
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderId(answerPCCDto.acquirerOrderId);
            Account account = accountRepository.findByAccountNumber(transaction.getDestinationAccountNumber()).get();
            Double balance = account.getBalance();
            balance = balance + transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
        }
    }
}
