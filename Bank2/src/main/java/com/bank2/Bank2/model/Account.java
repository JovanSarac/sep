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
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private Double balance;
    private Long PAN;
    private Date cardExpirationDate;
    private Integer securityCode;

    public Account(UUID id, Double balance, Long PAN, Date cardExpirationDate, Integer securityCode) {
        this.id = id;
        this.balance = balance;
        this.PAN = PAN;
        this.cardExpirationDate = cardExpirationDate;
        this.securityCode = securityCode;
    }

    public Account() {

    }

    public UUID getId() {
        return id;
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

    public void setId(UUID id) {
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
}
