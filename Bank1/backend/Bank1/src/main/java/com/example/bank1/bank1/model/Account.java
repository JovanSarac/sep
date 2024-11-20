package com.example.bank1.bank1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private Long cardNumber;
    private Double balance;
    private Long PAN;
    private Date cardExpirationDate;

    public Account(Long id, String accountNumber, Long cardNumber, Double balance, Long PAN, Date cardExpirationDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.PAN = PAN;
        this.cardExpirationDate = cardExpirationDate;
    }

    public Account() {

    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public Long getPAN() {
        return PAN;
    }

    public Date getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setPAN(Long PAN) {
        this.PAN = PAN;
    }

    public void setCardExpirationDate(Date cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }
}
