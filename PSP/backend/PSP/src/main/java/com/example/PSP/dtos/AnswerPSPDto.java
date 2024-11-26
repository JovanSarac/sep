package com.example.PSP.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerPSPDto {
    public String transactionResult;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
    public Long merchantOrderId;
    public Long paymentId;
}
