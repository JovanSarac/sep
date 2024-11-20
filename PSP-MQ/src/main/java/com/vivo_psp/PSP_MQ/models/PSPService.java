package com.vivo_psp.PSP_MQ.models;

import java.util.List;

public class PSPService {
    private Long id;
    private String name;
    private String description;
    private Double monthlyFee;
    private Boolean isActive;
    private List<String> supportedPaymentMethods;

    public PSPService(){

    }
    public PSPService(Long id, String name, String description, Double monthlyFee, Boolean isActive, List<String> supportedPaymentMethods) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.monthlyFee = monthlyFee;
        this.isActive = isActive;
        this.supportedPaymentMethods = supportedPaymentMethods;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<String> getSupportedPaymentMethods() {
        return supportedPaymentMethods;
    }

    public void setSupportedPaymentMethods(List<String> supportedPaymentMethods) {
        this.supportedPaymentMethods = supportedPaymentMethods;
    }
}
