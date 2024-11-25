package com.example.PSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private UUID merchantId;
    private String merchantPassword;
    private Long webShopId;

    public ApiKey() {
    }

    public ApiKey(Long id, UUID merchantId, String merchantPassword, Long webShopId) {
        this.id = id;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.webShopId = webShopId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(UUID merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public Long getWebShopId() {
        return webShopId;
    }

    public void setWebShopId(Long webShopId) {
        this.webShopId = webShopId;
    }
}
