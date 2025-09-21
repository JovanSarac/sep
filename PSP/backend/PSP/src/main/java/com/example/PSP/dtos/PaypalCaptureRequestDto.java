package com.example.PSP.dtos;

public class PaypalCaptureRequestDto {
    private String orderId;
    private String payerId;

    public PaypalCaptureRequestDto() {
    }

    public PaypalCaptureRequestDto(String orderId, String payerId) {
        this.orderId = orderId;
        this.payerId = payerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    @Override
    public String toString() {
        return "PaypalCaptureRequestDto{" +
                "orderId='" + orderId + '\'' +
                ", payerId='" + payerId + '\'' +
                '}';
    }
}
