package com.example.bank1.bank1.repository;

import com.example.bank1.bank1.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySourceAccountNumber(String sourceAccountNumber);
    Transaction findByAcquirerOrderIdAAndIssuerOrderId(UUID acquirerOrderId, UUID issuerOrderId);
}
