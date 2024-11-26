package com.pcc.PCC.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    public UUID merchantId;
    public String merchantPassword;
    public UUID acquirerOrderId;
    public Long acquirerTimestamp;
    public Double amount;
    public Long merchantOrderId;
    public Long timestamp;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
}
