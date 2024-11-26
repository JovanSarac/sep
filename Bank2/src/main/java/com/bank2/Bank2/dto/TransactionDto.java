package com.bank2.Bank2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    public Double amount;
    public String sourceAccountNumber;
    public String destinationAccountNumber;
    public Long PAN;
}
