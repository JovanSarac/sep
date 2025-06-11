package com.pcc.PCC.service;

import com.pcc.PCC.dto.QRCodeRequestDto;
import com.pcc.PCC.model.QRCodeRequest;
import com.pcc.PCC.repository.QRCodeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRCodeRequestService {
    @Autowired
    QRCodeRequestRepository qrCodeRequestRepository;

    public QRCodeRequestDto create(QRCodeRequestDto qrCodeRequestDto){
        QRCodeRequest qrCodeRequest = new QRCodeRequest(
                qrCodeRequestDto.buyerAccountNumber,
                qrCodeRequestDto.amount,
                qrCodeRequestDto.acquirerOrderId,
                qrCodeRequestDto.acquirerTimestamp);

        qrCodeRequestRepository.save(qrCodeRequest);
        return qrCodeRequestDto;
    }

    public Boolean isBank1(String accountNumber) {
        return accountNumber.startsWith("123");
    }
}
