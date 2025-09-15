package com.example.PSP.dtos;

public class PaypalRequestDto {
    private String amount;
    private String currency;
    private String successUrl;
    private String cancelUrl;

    public PaypalRequestDto() {}

    public PaypalRequestDto(String amount, String currency, String successUrl, String cancelUrl) {
        this.amount = amount;
        this.currency = currency;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
