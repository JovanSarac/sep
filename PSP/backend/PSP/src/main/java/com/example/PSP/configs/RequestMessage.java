package com.example.PSP.configs;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class RequestMessage {
    private String messageId;
    private String JWTToken;
    private Long sessionId;
    private Date messageDate;
    private String typeOfCardPayment;

    public RequestMessage() {
    }

    public RequestMessage(String messageId, String JWTToken, Long sessionId, Date messageDate, String typeOfCardPayment) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.sessionId = sessionId;
        this.messageDate = messageDate;
        this.typeOfCardPayment = typeOfCardPayment;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public void setJWTToken(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getTypeOfCardPayment() {
        return typeOfCardPayment;
    }

    public void setTypeOfCardPayment(String typeOfCardPayment) {
        this.typeOfCardPayment = typeOfCardPayment;
    }
}
