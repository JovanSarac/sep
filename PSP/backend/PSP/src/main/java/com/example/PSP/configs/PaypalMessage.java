package com.example.PSP.configs;

import java.util.Date;

public class PaypalMessage {
    private String messageId;
    private String jwtToken;
    private Date createdAt;
    private Long sessionId;

    public PaypalMessage(String messageId, String jwtToken, Date createdAt, Long sessionId) {
        this.messageId = messageId;
        this.jwtToken = jwtToken;
        this.createdAt = createdAt;
        this.sessionId = sessionId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
}
