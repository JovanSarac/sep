package com.example.PSP.dtos;

public class RequestQRCodePaymentDto {
    public String qrData;
    public Long paymentId;
    public String paymentUrl;
    public Double amount;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
}
