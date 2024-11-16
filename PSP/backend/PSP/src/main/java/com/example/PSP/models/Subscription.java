package com.example.PSP.models;

import com.example.PSP.dtos.UserInfoDto;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private PSPService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    private Double totalCost;

    private Boolean isActive;

    private Integer subscriptionDuration;

    // Getters and Setters
    public Integer getSubscriptionDuration() {
        return subscriptionDuration;
    }

    public void setSubscriptionDuration(Integer subscriptionDuration) {
        this.subscriptionDuration = subscriptionDuration;
    }

    public Double calculateTotalCost() {
        if (service != null && subscriptionDuration != null) {
            return service.getMonthlyFee() * subscriptionDuration;
        }
        return 0.0;
    }

    public void calculateEndDate() {
        if (startDate != null && subscriptionDuration != null) {
            this.endDate = startDate.plusMonths(subscriptionDuration);
        }
    }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}