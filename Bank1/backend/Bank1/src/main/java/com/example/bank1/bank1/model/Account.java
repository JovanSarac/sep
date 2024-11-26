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
    private Double balance;
    @Column(name = "PAN")
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

    public CardType getCardType() {
        return cardType;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
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

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
}
