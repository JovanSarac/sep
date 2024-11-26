package com.example.bank1.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PCCRequestDto {
    public Long PAN;
    public Integer securityCode;
    public String cardHolderName;
    public Date cardExpirationDate;
    public Double amount;
    public UUID acquirerOrderId;
    public Long acqueirerTimestamp;
}
