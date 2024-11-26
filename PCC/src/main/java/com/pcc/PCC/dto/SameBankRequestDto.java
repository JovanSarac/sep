package com.pcc.PCC.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class SameBankRequestDto {
    public Long PAN;
    public Integer securityCode;
    public String cardHolderName;
    public Date cardExpirationDate;
    public Double amount;

    public SameBankRequestDto() {
    }

    public SameBankRequestDto(Long PAN, Integer securityCode, String cardHolderName, Date cardExpirationDate, Double amount) {
        this.PAN = PAN;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
        this.cardExpirationDate = cardExpirationDate;
        this.amount = amount;
    }
}
