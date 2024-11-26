package com.bank2.Bank2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private Double balance;
    private Long PAN;
    private Date cardExpirationDate;
    private Integer securityCode;
    private CardType cardType;

    public Account(Long id, String accountNumber, Double balance, Long PAN, Date cardExpirationDate, Integer securityCode, CardType cardType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.PAN = PAN;
        this.cardExpirationDate = cardExpirationDate;
        this.securityCode = securityCode;
        this.cardType = cardType;
    }

    public Account() {

    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Integer getSecurityCode() {
        return securityCode;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
