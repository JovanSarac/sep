package com.example.VIVONET.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="ApiKeys")
public class ApiKey {
    @Id
    @SequenceGenerator(
            name="apiKey_sequence",
            sequenceName = "apiKey_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "apiKey_sequence"
    )
    private Long id;
    private String merchantId;
    private String merchantPassword;
    private Long paymentType;

    public ApiKey() {
    }

    public ApiKey(Long id, String merchantId, String merchantPassword, Long paymentType) {
        this.id = id;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.paymentType = paymentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public Long getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Long paymentType) {
        this.paymentType = paymentType;
    }
}
