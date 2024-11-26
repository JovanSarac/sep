package com.pcc.PCC.service;

import com.pcc.PCC.dto.RequestDto;
import com.pcc.PCC.model.Request;
import com.pcc.PCC.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public RequestDto create(RequestDto requestDto){
        Request request = new Request(requestDto.PAN,
                requestDto.securityCode,
                requestDto.cardHolderName,
                requestDto.cardExpirationDate,
                requestDto.amount,
                requestDto.acquirerOrderId,
                requestDto.acquirerTimestamp);

        requestRepository.save(request);
        return requestDto;
    }

    public Boolean isDataValid(RequestDto requestDto) {
        //check pan
        //check security code
        //check which bank to route to
        Integer digitNumOfPAN = String.valueOf(Math.abs(requestDto.PAN)).length();

        if (digitNumOfPAN < 13 || digitNumOfPAN > 16) {
            return false;
        }

        Date today = new Date();
        if (requestDto.cardExpirationDate.before(today)) {
            return false;
        }

        if (validatePAN(requestDto.PAN) % 10 != 0) {
            return false;
        }

        if(!isBank1(requestDto.PAN) && (requestDto.acquirerOrderId == null || requestDto.acquirerTimestamp == null)) {
            return false;
        }

        return true;
    }

    public Long validatePAN(Long PAN) {
        List<Long> numbers = new ArrayList<>();

        Long sum = 0L;
        Long panTemp = PAN;
        Integer counter = 0;

        while (panTemp > 0) {
            Long digit = panTemp % 10;
            if (counter % 2 == 1) {
                numbers.add(digit*2);
            } else {
                sum += digit;
            }
            counter++;
            panTemp /= 10;
        }

        for (Long number : numbers) {
            if (number < 10) {
                sum += number;
            } else {
                sum += (number % 10);
                sum += (number/10);
            }
        }

        return sum;
    }

    public Boolean isBank1(Long PAN) {
        List<Long> numbers = new ArrayList<>();
        Long panTemp = PAN;

        while (panTemp > 0) {
            Long digit = panTemp % 10;
            numbers.add(digit);
            panTemp /= 10;
        }

        List<Long> bankDigits = numbers.subList(Math.max(numbers.size() - 6, 0), numbers.size() - 2);

        //cifre prve banke su 11111
        for (Long digit : bankDigits) {
            if (digit != 1) {
                return false;
            }
        }

        return true;
    }
}
