package com.example.bank1.bank1.dto;

import com.example.bank1.bank1.model.TransactionState;
import com.example.bank1.bank1.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
