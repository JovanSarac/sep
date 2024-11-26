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
        Request request = new Request(requestDto.PAN,
                requestDto.securityCode,
                requestDto.cardHolderName,
                requestDto.cardExpirationDate,
                requestDto.acquirerOrderId,
                requestDto.acquirerTimestamp);

        requestRepository.save(request);
        return requestDto;
    }
}
