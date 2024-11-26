package com.bank2.Bank2.repository;

import com.bank2.Bank2.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySourceAccountNumber(String sourceAccountNumber);
    @Query("SELECT t FROM Transaction t WHERE t.acquirerOrderId = :acquirerOrderId")
    Transaction findByAcquirerOrderId(@Param("acquirerOrderId") UUID acquirerOrderId);
}
