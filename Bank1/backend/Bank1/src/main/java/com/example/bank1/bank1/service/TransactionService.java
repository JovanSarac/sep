package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.AnswerPCCDto;
import com.example.bank1.bank1.dto.PCCRequestDto;
import com.example.bank1.bank1.dto.TransactionDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.Transaction;
import com.example.bank1.bank1.model.TransactionState;
import com.example.bank1.bank1.repository.AccountRepository;
import com.example.bank1.bank1.repository.QRPaymentRequestRepository;
import com.example.bank1.bank1.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private QRPaymentRequestRepository qrPaymentRequestRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private Boolean checkAccountBalance(TransactionDto transactionDto) {
        logger.info("Retrieving account by accountNumber " + transactionDto.sourceAccountNumber + " for checking balance");
        Account account = accountRepository.findByAccountNumber(transactionDto.getSourceAccountNumber()).get();
        if (account.getBalance() - transactionDto.getAmount() < 0) {
            return false;
        }
        return true;
    }

    public void finishTransaction(AnswerPCCDto answerPCCDto) {
        logger.info("Finishing transaction..");
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderIdAAndIssuerOrderId(answerPCCDto.acquirerOrderId, answerPCCDto.issuerOrderId);
            Account account = accountRepository.findByAccountNumber(transaction.getDestinationAccountNumber()).get();
            Double balance = account.getBalance();
            balance = balance + transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
            //treba sacuvati i transakciju da je finished
            transaction.setTransactionState(TransactionState.FINISHED);
            transactionRepository.save(transaction);
            logger.info("Transaction finished successfully and it was saved");
        }
    }

    public void finishTransactionIssuer(AnswerPCCDto answerPCCDto) {
        logger.info("Finishing transaction for issuer..");
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderIdAAndIssuerOrderId(answerPCCDto.acquirerOrderId, answerPCCDto.issuerOrderId);
            Account account = accountRepository.findByAccountNumber(transaction.getSourceAccountNumber()).get();
            Double balance = account.getBalance();
            balance = balance - transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
            //ovde mozda da se doda da transankcija bude finished
            transaction.setTransactionState(TransactionState.FINISHED);
            transactionRepository.save(transaction);

            logger.info("Transaction for issuer finished successfully and it was saved");
        }
    }

    public void finishTransactionBank2ToBank1(AnswerPCCDto answerPCCDto) {
        logger.info("Finishing transaction bank2 to bank1");
        if (answerPCCDto.transactionResult.equals("uspesno")) {
            Transaction transaction = transactionRepository.findByAcquirerOrderId(answerPCCDto.acquirerOrderId);
            //String accountNumber = transaction.getDestinationAccountNumber();
            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(transaction.getDestinationAccountNumber());
            if (optionalAccount.isEmpty()) {
                throw new IllegalStateException("Account not found");
            }

            Account account = optionalAccount.get();
            Double balance = account.getBalance();
            balance = balance + transaction.getAmount();
            account.setBalance(balance);
            accountRepository.save(account);
            //mozda da se sacuva i transakcija kao finished
            transaction.setTransactionState(TransactionState.FINISHED);
            transactionRepository.save(transaction);

            logger.info("Transaction bank2 to bank1 finished successfully and it was saved");
        }
    }
}
