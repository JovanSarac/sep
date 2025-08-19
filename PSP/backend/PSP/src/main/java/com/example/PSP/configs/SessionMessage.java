package com.example.PSP.configs;

import java.util.Date;

public class SessionMessage {
    private String messageId;
    private String JWTToken;
    private Date messageDate;
    private Long sessionId;
    private String tpyeOfOutput;

    public SessionMessage() {
    }

    public SessionMessage(String messageId, String JWTToken, Date messageDate, Long sessionId, String tpyeOfOutput) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.messageDate = messageDate;
        this.sessionId = sessionId;
        this.tpyeOfOutput = tpyeOfOutput;
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

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTpyeOfOutput() {
        return tpyeOfOutput;
    }

    public void setTpyeOfOutput(String tpyeOfOutput) {
        this.tpyeOfOutput = tpyeOfOutput;
    }
}
