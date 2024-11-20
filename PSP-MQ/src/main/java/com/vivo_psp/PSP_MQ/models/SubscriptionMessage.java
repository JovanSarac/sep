package com.vivo_psp.PSP_MQ.models;

import com.vivo_psp.PSP_MQ.dtos.SubscriptionRequest;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class SubscriptionMessage {
    private String messageId;
    private String JWTToken;
    private SubscriptionRequest request;
    private Date messageDate;

    public SubscriptionMessage() {
    }

    public SubscriptionMessage(String messageId, String JWTToken, SubscriptionRequest request, Date messageDate) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.request = request;
        this.messageDate = messageDate;
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

    public SubscriptionRequest getRequest() {
        return request;
    }

    public void setRequest(SubscriptionRequest request) {
        this.request = request;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
