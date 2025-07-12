package com.pcc.PCC.service;

import com.pcc.PCC.dto.QRCodeRequestDto;
import com.pcc.PCC.model.QRCodeRequest;
import com.pcc.PCC.repository.QRCodeRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRCodeRequestService {
    @Autowired
    QRCodeRequestRepository qrCodeRequestRepository;

    private final static Logger logger = LoggerFactory.getLogger(QRCodeRequestService.class);

    public QRCodeRequestDto create(QRCodeRequestDto qrCodeRequestDto){
        QRCodeRequest qrCodeRequest = new QRCodeRequest(
                qrCodeRequestDto.buyerAccountNumber,
                qrCodeRequestDto.amount,
                qrCodeRequestDto.acquirerOrderId,
                qrCodeRequestDto.acquirerTimestamp);

        QRCodeRequest savedQrCodeRequest = qrCodeRequestRepository.save(qrCodeRequest);
        logger.info("Saved new QR code request " + savedQrCodeRequest.getId());

        return qrCodeRequestDto;
    }

    public Boolean isBank1(String accountNumber) {
        return accountNumber.startsWith("123");
    }
}
