package com.bank2.Bank2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class AnswerPCCDto {
    public String transactionResult;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
    public UUID issuerOrderId;
    public Long issuerOrderTimestamp;

    public AnswerPCCDto() {
    }

    public AnswerPCCDto(String transactionResult, UUID acquirerOrderId, Long acquirerTimestamp, UUID issuerOrderId, Long issuerOrderTimestamp) {
        this.transactionResult = transactionResult;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.issuerOrderId = issuerOrderId;
        this.issuerOrderTimestamp = issuerOrderTimestamp;
    }
}
