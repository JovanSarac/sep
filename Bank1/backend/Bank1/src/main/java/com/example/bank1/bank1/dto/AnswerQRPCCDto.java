package com.example.bank1.bank1.dto;

import java.util.UUID;

public class AnswerQRPCCDto {
    public String transactionResult;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
    public UUID issuerOrderId;
    public Long issuerOrderTimestamp;
    public String qrPaymentId;

    public AnswerQRPCCDto() {
    }

    public AnswerQRPCCDto(String transactionResult, UUID acquirerOrderId, Long acquirerTimestamp, UUID issuerOrderId, Long issuerOrderTimestamp, String qrPaymentId) {
        this.transactionResult = transactionResult;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.issuerOrderId = issuerOrderId;
        this.issuerOrderTimestamp = issuerOrderTimestamp;
        this.qrPaymentId = qrPaymentId;
    }
}
