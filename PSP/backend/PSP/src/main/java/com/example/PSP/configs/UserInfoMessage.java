package com.example.PSP.configs;

import java.util.Date;

public class UserInfoMessage {
    private String messageId;
    private String JWTToken;
    private Date messageDate;
    private Long userId;
    private String userType;

    public UserInfoMessage() {
    }

    public UserInfoMessage(String messageId, String JWTToken, Date messageDate, Long userId) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.messageDate = messageDate;
        this.userId = userId;
    }

    public UserInfoMessage(String messageId, String JWTToken, Date messageDate, Long userId, String userType) {
        this.messageId = messageId;
        this.JWTToken = JWTToken;
        this.messageDate = messageDate;
        this.userId = userId;
        this.userType = userType;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
