package com.example.VIVONET.configs;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ApiKeyMessage {
    private String messageId;
    private Long paymentTypeId;
    private Date messageDate;

    public ApiKeyMessage() {
    }

    public ApiKeyMessage(String messageId, Long paymentTypeId, Date messageDate) {
        this.messageId = messageId;
        this.paymentTypeId = paymentTypeId;
        this.messageDate = messageDate;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
