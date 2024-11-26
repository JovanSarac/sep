package com.pcc.PCC.model;

import jakarta.persistence.*;

import java.util.Date;
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
    private Long PAN;
    private Integer securityCode;
    private String cardHolderName;
    private Date cardExpirationDate;
    private Double amount;
    private UUID acquirerOrderId;
    private Long acquirerTimestamp;

    public Request() {
    }

    public Request(Long PAN, Integer securityCode, String cardHolderName, Date cardExpirationDate, Double amount, UUID acquirerOrderId, Long acquirerTimestamp) {
        this.PAN = PAN;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
        this.cardExpirationDate = cardExpirationDate;
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

    public Long getPAN() {
        return PAN;
    }

    public void setPAN(Long PAN) {
        this.PAN = PAN;
    }

    public Integer getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Date getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(Date cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
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
