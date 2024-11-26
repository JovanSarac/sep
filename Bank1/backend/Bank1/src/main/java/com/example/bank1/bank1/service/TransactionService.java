package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.AnswerPCCDto;
import com.example.bank1.bank1.dto.AnswerPSPDto;
import com.example.bank1.bank1.dto.PCCRequestDto;
import com.example.bank1.bank1.dto.TransactionDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.Transaction;
import com.example.bank1.bank1.repository.AccountRepository;
import com.example.bank1.bank1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Service
public class TransactionService {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
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
            String url = "http://localhost:8082/answerPSP";
            HttpHeaders headers = new HttpHeaders();
            var requestEntity = new HttpEntity<>(new AnswerPSPDto(
                    "uspesno",
                    answerPCCDto.acquirerOrderId,
                    new Date().getTime(),
                    0L,
                    0L;
            var method = HttpMethod.POST;

            try {
                String response = restTemplate().exchange(url, method, requestEntity, String.class).getBody();
            } catch (HttpClientErrorException e) {
                System.out.println("Error calling endpoint: " + e.getMessage());
            }
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
}
