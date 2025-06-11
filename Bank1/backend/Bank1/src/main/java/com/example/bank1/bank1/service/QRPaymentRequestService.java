package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.QRPaymentDto;
import com.example.bank1.bank1.dto.QRPaymentRequestDto;
import com.example.bank1.bank1.model.QRPaymentRequest;
import com.example.bank1.bank1.model.QRPaymentRequestState;
import com.example.bank1.bank1.repository.QRPaymentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class QRPaymentRequestService {
    @Autowired
    private QRPaymentRequestRepository qrPaymentRequestRepository;

    public void saveQRPaymentRequest(QRPaymentRequestDto qrPaymentRequestDto) {
        QRPaymentRequest qrPaymentRequest = new QRPaymentRequest();
        qrPaymentRequest.setPaymentId(qrPaymentRequestDto.paymentId);
        qrPaymentRequest.setPaymentUrl(qrPaymentRequestDto.paymentUrl);
        qrPaymentRequest.setQrPaymentRequestState(qrPaymentRequestDto.qrPaymentRequestState);

        qrPaymentRequestRepository.save(qrPaymentRequest);
    }

    public QRPaymentRequest findByQRPaymentId(UUID qrPaymentId) {
        return qrPaymentRequestRepository.findByPaymentId(qrPaymentId).get();
    }

    public void finishQRPaymentTransaction(UUID qrPaymentId) {
        QRPaymentRequest qrPaymentRequest = qrPaymentRequestRepository.findByPaymentId(qrPaymentId).get();
        qrPaymentRequest.setQrPaymentRequestState(QRPaymentRequestState.COMPLETED);
        qrPaymentRequestRepository.save(qrPaymentRequest);
    }
}
