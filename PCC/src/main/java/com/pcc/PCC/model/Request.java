package com.pcc.PCC.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="Requests")
public class Request {
    @Id
    @SequenceGenerator(
            name = "request_sequence",
            sequenceName = "request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_sequence"
    )
    private Long id;
    private UUID merchantId;
    private String merchantPassword;
    private UUID acquirerOrderId;
    private Long acquirerTimestamp;
    private Double amount;
    private Long merchantOrderId;
    private Long timestamp;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;

    public Request() {
    }

    public Request(UUID merchantId, String merchantPassword, UUID acquirerOrderId, Long acquirerTimestamp, Double amount, Long merchantOrderId, Long timestamp, String successUrl, String failedUrl, String errorUrl) {
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.timestamp = timestamp;
        this.successUrl = successUrl;
        this.failedUrl = failedUrl;
        this.errorUrl = errorUrl;
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

    public UUID getAcquirerOrderId() {
        return acquirerOrderId;
    }

    public void setAcquirerOrderId(UUID acquirerOrderId) {
        this.acquirerOrderId = acquirerOrderId;
    }

    public Long getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setAcquirerTimestamp(Long acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(Long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailedUrl() {
        return failedUrl;
    }

    public void setFailedUrl(String failedUrl) {
        this.failedUrl = failedUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }
}
