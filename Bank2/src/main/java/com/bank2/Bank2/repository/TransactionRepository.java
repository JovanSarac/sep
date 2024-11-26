package com.bank2.Bank2.repository;

import com.bank2.Bank2.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
