package com.bank2.Bank2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileBankUserDto {
    public Long PAN;
    public String accountNumber;
    public Double balance;
    public Integer securityCode;
    public String name;
}
