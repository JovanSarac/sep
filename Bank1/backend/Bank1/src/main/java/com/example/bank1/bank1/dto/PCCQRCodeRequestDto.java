package com.example.bank1.bank1.dto;

import java.util.UUID;

public class PCCQRCodeRequestDto {
    public String buyerAccountNumber;
    public Double amount;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
}
