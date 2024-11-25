package com.vivo_psp.PSP_MQ.models;

import com.vivo_psp.PSP_MQ.dtos.SubscriptionDto;
import com.vivo_psp.PSP_MQ.dtos.SubscriptionRequest;
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

    public RequestMessage() {
    }

    public RequestMessage(String messageId, String JWTToken, Long sessionId, Date messageDate) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.sessionId = sessionId;
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
}
