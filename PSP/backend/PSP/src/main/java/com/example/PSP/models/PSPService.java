package com.example.PSP.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="services")
public class PSPService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 512)
    private String description;
    private Double monthlyFee;
    private Boolean isActive;
    @ElementCollection
    @CollectionTable(name = "supported_payment_methods", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "payment_method")
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
