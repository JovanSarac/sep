package com.pcc.PCC.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="QRCodeRequests")
public class QRCodeRequest {
    @Id
    @SequenceGenerator(
            name = "qrcode_request_sequence",
            sequenceName = "qrcode_request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "qrcode_request_sequence"
    )
    private Long id;
    private String buyerAccountNumber;
    private Double amount;
    private UUID acquirerOrderId;
    private Long acquirerTimestamp;

    public QRCodeRequest() {
    }

    public QRCodeRequest(String buyerAccountNumber, Double amount, UUID acquirerOrderId, Long acquirerTimestamp) {
        this.buyerAccountNumber = buyerAccountNumber;
        this.amount = amount;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuyerAccountNumber() {
        return buyerAccountNumber;
    }

    public void setBuyerAccountNumber(String buyerAccountNumber) {
        this.buyerAccountNumber = buyerAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
}
