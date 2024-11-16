package com.example.PSP.dtos;

public class SubscriptionRequest {
    private Long userId;
    private Long serviceId;
    private Integer subscriptionDuration;

    public Long getUserId() {
        return userId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public Integer getSubscriptionDuration() {
        return subscriptionDuration;
    }
}
