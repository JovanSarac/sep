package com.example.PSP.dtos;

public class RequestPaymentDto {
    public Long paymentId;
    public String paymentUrl;
    public Double amount;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
}
