package com.example.bank1.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRPaymentDto {
    public String sellerAccountNumber;
    public String name;
    public Double amount;
    public Integer paymentCode;
    public String purposeOfPayment;
    public String buyerAccountNumber;
    public String buyerName;
}
