package com.bank2.Bank2.service;

import com.bank2.Bank2.dto.UserIdentificationDto;
import com.bank2.Bank2.model.Account;
import com.bank2.Bank2.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Boolean isCardValid(UserIdentificationDto userIdentificationDto, Account account){
        boolean validPan = userIdentificationDto.PAN != account.getPAN() || isValidPan(userIdentificationDto.PAN);
        boolean validExpirationDate = userIdentificationDto.cardExpirationDate != account.getCardExpirationDate()
                || userIdentificationDto.cardExpirationDate.after(new Date());

        return validPan || validExpirationDate;
    }

    public Boolean isValidPan(Long PAN){
        int sum = 0;
        while(PAN > 0){
            sum += PAN % 10;
            long number = 2 * ((PAN % 100) / 10);
            if(number / 10 > 0){
                number = number/10 + number%10;
            }
            sum += number;

            PAN /= 100;
        }

        return sum % 10 == 0;
    }
}
