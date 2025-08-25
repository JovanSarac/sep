package com.example.PSP.configs;

import java.util.Date;

public class NewPaymentServiceMessage {
    private String messageId;
    private String JWTToken;
    private Date messageDate;
    private String serviceName;
    private String type;

    public NewPaymentServiceMessage() {
    }

    public NewPaymentServiceMessage(String messageId, String JWTToken, Date messageDate, String serviceName, String type) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.messageDate = messageDate;
        this.serviceName = serviceName;
        this.type = type;
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

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
