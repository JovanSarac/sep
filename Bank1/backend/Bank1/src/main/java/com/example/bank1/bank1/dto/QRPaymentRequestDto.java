package com.example.bank1.bank1.dto;

import com.example.bank1.bank1.model.QRPaymentRequestState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRPaymentRequestDto {
    public UUID paymentId;
    public String paymentUrl;
    public QRPaymentRequestState qrPaymentRequestState;
}
