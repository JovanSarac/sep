package com.example.bank1.bank1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID transactionNumber;
    private Double amount;
    private TransactionType transactionType;
    private TransactionState transactionState;
    private Date transactionDate;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String payerName;
    private String recipientName;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transaction(Long id, UUID transactionNumber, Double amount, TransactionType transactionType, TransactionState transactionState, Date transactionDate, String sourceAccountNumber, String destinationAccountNumber, String payerName, String recipientName, User user) {
        this.id = id;
        this.transactionNumber = transactionNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionState = transactionState;
        this.transactionDate = transactionDate;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.payerName = payerName;
        this.recipientName = recipientName;
        this.user = user;
    }

    public Transaction() {

    }

    public Long getId() {
        return id;
    }

    public UUID getTransactionNumber() {
        return transactionNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionState getTransactionState() {
        return transactionState;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public String getPayerName() {
        return payerName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTransactionNumber(UUID transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionState(TransactionState transactionState) {
        this.transactionState = transactionState;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
