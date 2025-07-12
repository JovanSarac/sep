package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.QRPaymentDto;
import com.example.bank1.bank1.dto.QRPaymentRequestDto;
import com.example.bank1.bank1.model.QRPaymentRequest;
import com.example.bank1.bank1.model.QRPaymentRequestState;
import com.example.bank1.bank1.repository.QRPaymentRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class QRPaymentRequestService {
    @Autowired
    private QRPaymentRequestRepository qrPaymentRequestRepository;

    private static final Logger logger = LoggerFactory.getLogger(QRPaymentRequestService.class);

    public void saveQRPaymentRequest(QRPaymentRequestDto qrPaymentRequestDto) {
        QRPaymentRequest qrPaymentRequest = new QRPaymentRequest();
        qrPaymentRequest.setPaymentId(qrPaymentRequestDto.paymentId);
        qrPaymentRequest.setPaymentUrl(qrPaymentRequestDto.paymentUrl);
        qrPaymentRequest.setQrPaymentRequestState(qrPaymentRequestDto.qrPaymentRequestState);

        QRPaymentRequest savedQrPaymentRequest = qrPaymentRequestRepository.save(qrPaymentRequest);

        logger.info("New QR payment request saved with id " + savedQrPaymentRequest.getPaymentId());
    }

    public QRPaymentRequest findByQRPaymentId(UUID qrPaymentId) {
        logger.info("Retrieving QR payment request by paymentId " + qrPaymentId);
        return qrPaymentRequestRepository.findByPaymentId(qrPaymentId).get();
    }

    public void finishQRPaymentTransaction(UUID qrPaymentId) {
        QRPaymentRequest qrPaymentRequest = qrPaymentRequestRepository.findByPaymentId(qrPaymentId).get();
        qrPaymentRequest.setQrPaymentRequestState(QRPaymentRequestState.COMPLETED);
        QRPaymentRequest savedQrPaymentRequest = qrPaymentRequestRepository.save(qrPaymentRequest);
        logger.info("Updated QR payment request state to COMPLETED. Id of QR payment request " + savedQrPaymentRequest.getId());
    }
}
