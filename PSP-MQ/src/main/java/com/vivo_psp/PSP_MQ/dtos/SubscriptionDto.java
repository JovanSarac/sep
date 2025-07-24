package com.vivo_psp.PSP_MQ.dtos;

import com.vivo_psp.PSP_MQ.models.PSPService;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionDto {
    public Long id;
    public PSPService service;
    public Long userId;
    public LocalDate startDate;
    public LocalDate endDate;
    public Double totalCost;
    public Boolean isActive;
    public Integer subscriptionDuration;
    public UUID merchantId;
    public String merchantPassword;
    public Long paymentServiceId;
}
