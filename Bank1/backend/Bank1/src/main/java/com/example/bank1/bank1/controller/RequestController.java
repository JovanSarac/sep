package com.example.bank1.bank1.controller;

import com.example.bank1.bank1.dto.*;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.QRPaymentRequest;
import com.example.bank1.bank1.model.QRPaymentRequestState;
import com.example.bank1.bank1.model.User;
import com.example.bank1.bank1.service.QRPaymentRequestService;
import com.example.bank1.bank1.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank1/requests")
public class RequestController {
    private final RequestService requestService;
    @Autowired
    private QRPaymentRequestService qrPaymentRequestService;
    @PostMapping("/validateRequest")
    public ResponseEntity<PaymentDataDto> validateRequest(@RequestBody RequestDto requestDto) {
        //treba da se vrati payment_url i payment_id, ne url ce mozda da bude na koju se banku odnosi nmp jos
        //ovde proveri podatke u requestDto
        //i ako je sve dobro vrati ok
        Boolean validData = requestService.checkRequestData(requestDto);
        PaymentDataDto paymentDataDto = new PaymentDataDto();
        if (validData) {
            paymentDataDto.paymentId = -1L;
            paymentDataDto.paymentUrl = "https://localhost:4202/";
            return ResponseEntity.ok(paymentDataDto);
        }
        return (ResponseEntity<PaymentDataDto>) ResponseEntity.badRequest();
    }

    //promeniti naziv ove metode dole
    @PostMapping("/validateRequestQRCode")
    public ResponseEntity<PaymentDataQRDto> validateRequestQRCode(@RequestBody RequestDto requestDto) {
        //treba da se vrati payment_url i payment_id, ne url ce mozda da bude na koju se banku odnosi nmp jos
        //ovde proveri podatke u requestDto
        //i ako je sve dobro vrati ok
        Boolean validData = requestService.checkRequestData(requestDto);
        PaymentDataQRDto paymentDataQRDto = new PaymentDataQRDto();
        if (validData) {
            paymentDataQRDto.paymentId = -2L;
            paymentDataQRDto.paymentUrl = "https://localhost:4202/qrCode";
            //za sada su zakucane vrednosti za racun primaoca
            //treba dodati da se vuku podaci iz baze

            UUID qrPaymentId = UUID.randomUUID();
            QRPaymentRequestDto qrPaymentRequestDto = new QRPaymentRequestDto();
            qrPaymentRequestDto.paymentId = qrPaymentId;
            qrPaymentRequestDto.paymentUrl = paymentDataQRDto.paymentUrl;
            qrPaymentRequestDto.qrPaymentRequestState = QRPaymentRequestState.PENDING;
            qrPaymentRequestService.saveQRPaymentRequest(qrPaymentRequestDto);
            paymentDataQRDto.qrPaymentId = qrPaymentId;

            Double amountRSD = requestDto.amount * 101.79;
            amountRSD = amountRSD * 100;
            amountRSD = (double) Math.round(amountRSD);
            amountRSD = amountRSD / 100;
            String amountRSDString = amountRSD.toString();

            if (!amountRSDString.contains(".")) {
                amountRSDString = amountRSDString + ",00";
            } else {
                amountRSDString = amountRSDString.replace(".", ",");
            }

            paymentDataQRDto.qrData = "K:PR|" +
                    "V:01|" +
                    "C:1|" +
                    "R:1234567890123456|" +
                    "N:Webshop d.o.o.|" +
                    "I:RSD" + amountRSDString + "|" +
                    "SF:289|" +
                    "S:Plaćanje narudžbine #" + qrPaymentId + " (USD" + requestDto.amount + ")";
            return ResponseEntity.ok(paymentDataQRDto);
        }
        return (ResponseEntity<PaymentDataQRDto>) ResponseEntity.badRequest();
    }

    @PostMapping("/checkRequestState")
    public ResponseEntity<String> chechQRRequestState(@RequestBody String string) {
        UUID qrPaymentId = UUID.fromString(string);
        QRPaymentRequest qrPaymentRequest = qrPaymentRequestService.findByQRPaymentId(qrPaymentId);

        if (qrPaymentRequest.getQrPaymentRequestState() == QRPaymentRequestState.COMPLETED) {
            return ResponseEntity.ok("COMPLETED");
        } else if (qrPaymentRequest.getQrPaymentRequestState() == QRPaymentRequestState.ERROR) {
            return ResponseEntity.ok("ERROR");
        } else if (qrPaymentRequest.getQrPaymentRequestState() == QRPaymentRequestState.FAILED) {
            return ResponseEntity.ok("FAILED");
        } else {
            return ResponseEntity.ok("PENDING");
        }
    }
}
