package com.bank2.Bank2.service;

import com.bank2.Bank2.dto.AnswerPCCDto;
import com.bank2.Bank2.dto.TransactionDto;
import com.bank2.Bank2.model.Account;
import com.bank2.Bank2.model.Transaction;
import com.bank2.Bank2.model.TransactionState;
import com.bank2.Bank2.repository.AccountRepository;
import com.bank2.Bank2.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private Boolean checkAccountBalance(TransactionDto transactionDto) {
        /*Optional<Account> accountOptional = accountRepository.findByAccountNumber(transactionDto.sourceAccountNumber);
        if(!accountOptional.isPresent()) throw new ResourceAccessException("No account for source account number " + transactionDto.sourceAccountNumber);

        Account account = accountOptional.get();

        if (account.getBalance() - transactionDto.amount < 0) {
            return false;
        }*/
        return true;
    }

    public void finishTransactionIssuer(AnswerPCCDto answerPCCDto) {
        logger.info("Finishin transaction for issuer..");
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderId(answerPCCDto.acquirerOrderId);
            Account account = accountRepository.findByAccountNumber(transaction.getSourceAccountNumber()).get();
            Double balance = account.getBalance();
            balance = balance - transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
            transaction.setTransactionState(TransactionState.FINISHED);
            transactionRepository.save(transaction);

            logger.info("Transaction for issuer finished successfully and it was saved");
        }
    }
}
