package com.pcc.PCC.dto;

import java.util.UUID;

public class QRCodeRequestDto {
    public String buyerAccountNumber;
    public Double amount;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
}
