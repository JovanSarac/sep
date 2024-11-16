package com.example.PSP.dtos;

import java.time.LocalDate;

public class SubscriptionDto {
    private Long id;
    private Long serviceId; // ID usluge
    private Long userId; // Samo ID korisnika
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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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
