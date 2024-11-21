package com.bank2.Bank2.dto;

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
public class UserIdentificationDto {
    public UUID userId;
    public Long PAN;
    public Integer securityCode;
    public String cardHolderName;
    public Date cardExpirationDate;
}
