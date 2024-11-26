package com.pcc.PCC.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {
    public String transactionResult;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
    public UUID issuerOrderId;
    public Long issuerOrderTimestamp;
}
