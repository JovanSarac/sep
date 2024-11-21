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
    private Double amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private Long PAN;
}
