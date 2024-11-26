package com.bank2.Bank2.repository;

import com.bank2.Bank2.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySourceAccountNumber(String sourceAccountNumber);
}
