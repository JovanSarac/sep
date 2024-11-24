package com.example.bank1.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    public Long merchantId;
    public String merchantPassword;
    public Double amount;
    public Long merchantOrderId;
    public Long timestamp;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
}
