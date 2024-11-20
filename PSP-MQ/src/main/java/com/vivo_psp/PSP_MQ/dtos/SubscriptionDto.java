package com.vivo_psp.PSP_MQ.dtos;

import com.vivo_psp.PSP_MQ.models.PSPService;

import java.time.LocalDate;

public class SubscriptionDto {
    private Long id;
    private PSPService service;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalCost;
    private Boolean isActive;
    private Integer subscriptionDuration;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PSPService getService() {
        return service;
    }

    public void setService(PSPService service) {
        this.service = service;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSubscriptionDuration() {
        return subscriptionDuration;
    }

    public void setSubscriptionDuration(Integer subscriptionDuration) {
        this.subscriptionDuration = subscriptionDuration;
    }
}
