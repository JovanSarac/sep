package com.example.mobilebank.dto;

import java.util.Date;

public class UserIdentificationDto {
    private Long PAN;
    private Integer securityCode;
    private String cardHolderName;
    private Date cardExpirationDate;
    private Double amount;

    // Getteri i setteri
    public Long getPAN() {
        return PAN;
    }

    public void setPAN(Long PAN) {
        this.PAN = PAN;
    }

    public Integer getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Date getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(Date cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
