package com.example.VIVONET.configs;

import com.example.VIVONET.models.ApiKey;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Data
@ToString
public class ApiKeyResponseMessage {
    private String messageId;
    private String merchantId;
    private String merchantPassword;
    private Date messageDate;

    public ApiKeyResponseMessage() {
    }

    public ApiKeyResponseMessage(String messageId, String merchantId, String merchantPassword, Date messageDate) {
        this.messageId = messageId;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.messageDate = messageDate;
    }

    public ApiKeyResponseMessage(String string, String merchantId, String merchantPassword) {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
