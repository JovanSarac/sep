package com.bank2.Bank2.service;

import com.bank2.Bank2.dto.RequestDto;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    public Boolean checkRequestData(RequestDto requestDto) {
        if (requestDto.getMerchantId().equals(null)) {
            return false;
        }
        if (requestDto.getMerchantPassword().equals(null)) {
            return false;
        }
        if (requestDto.getAmount().equals(null)) {
            return false;
        }
        if (requestDto.getMerchantOrderId().equals(null)) {
            return false;
        }
        if (requestDto.getTimestamp().equals(null)) {
            return false;
        }
        if (requestDto.getSuccessUrl().equals(null)) {
            return false;
        }
        if (requestDto.getErrorUrl().equals(null)) {
            return false;
        }
        if (requestDto.getFailedUrl().equals(null)) {
            return false;
        }
        return true;
    }
}
