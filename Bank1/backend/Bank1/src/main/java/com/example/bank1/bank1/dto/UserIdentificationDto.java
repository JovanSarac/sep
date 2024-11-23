package com.example.bank1.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentificationDto {
    public Long userId;
    public Long PAN;
    public Integer securityCode;
    public String cardHolderName;
    public Date cardExpirationDate;
}
