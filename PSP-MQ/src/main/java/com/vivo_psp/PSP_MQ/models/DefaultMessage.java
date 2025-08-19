package com.vivo_psp.PSP_MQ.models;

import java.util.Date;

public class DefaultMessage {
    private String messageId;
    private String JWTToken;
    private Date messageDate;

    public DefaultMessage() {
    }

    public DefaultMessage(String messageId, String JWTToken, Date messageDate) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
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

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
