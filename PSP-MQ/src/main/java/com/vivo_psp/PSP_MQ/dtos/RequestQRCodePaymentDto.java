package com.vivo_psp.PSP_MQ.dtos;

import java.util.UUID;

public class RequestQRCodePaymentDto {
    public String qrData;
    public Long paymentId;
    public String paymentUrl;
    public Double amount;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
    public UUID qrPaymentId;
}
