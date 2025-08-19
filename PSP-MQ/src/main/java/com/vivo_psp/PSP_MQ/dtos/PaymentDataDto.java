package com.vivo_psp.PSP_MQ.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDataDto {
    public Long paymentId;
    public String paymentUrl;
    public Double amount;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
}
