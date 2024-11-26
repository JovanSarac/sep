package com.pcc.PCC.service;

import com.pcc.PCC.dto.RequestDto;
import com.pcc.PCC.model.Request;
import com.pcc.PCC.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public RequestDto create(RequestDto requestDto){
        Request request = new Request(requestDto.merchantId,
                requestDto.merchantPassword,
                requestDto.acquirerOrderId,
                requestDto.acquirerTimestamp,
                requestDto.amount,
                requestDto.merchantOrderId,
                requestDto.timestamp,
                requestDto.successUrl,
                requestDto.failedUrl,
                requestDto.errorUrl);

        requestRepository.save(request);
        return requestDto;
    }
}
