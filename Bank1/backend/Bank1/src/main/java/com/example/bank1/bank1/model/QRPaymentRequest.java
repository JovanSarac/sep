package com.example.bank1.bank1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="QRPaymentRequests")
public class QRPaymentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID paymentId;
    private String paymentUrl;
    private QRPaymentRequestState qrPaymentRequestState;

    public QRPaymentRequest() {
    }

    public QRPaymentRequest(UUID paymentId, String paymentUrl, QRPaymentRequestState qrPaymentRequestState) {
        this.paymentId = paymentId;
        this.paymentUrl = paymentUrl;
        this.qrPaymentRequestState = qrPaymentRequestState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public QRPaymentRequestState getQrPaymentRequestState() {
        return qrPaymentRequestState;
    }

    public void setQrPaymentRequestState(QRPaymentRequestState qrPaymentRequestState) {
        this.qrPaymentRequestState = qrPaymentRequestState;
    }
}
