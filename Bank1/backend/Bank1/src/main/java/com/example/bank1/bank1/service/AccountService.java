package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.TransactionDto;
import com.example.bank1.bank1.dto.UserIdentificationDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.CardType;
import com.example.bank1.bank1.model.User;
import com.example.bank1.bank1.repository.AccountRepository;
import com.example.bank1.bank1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Boolean validateData(UserIdentificationDto userIdentificationDto, Account account) {
        //check pan
        //check security code
        //check if bank is the same
        if (userIdentificationDto.getPAN() != account.getPAN()) {
            return false;
        }

        if (userIdentificationDto.getCardExpirationDate() != account.getCardExpirationDate()) {
            return false;
        }

        Date today = new Date();
        if (account.getCardExpirationDate().before(today)) {
            return false;
        }

        if (validatePAN(userIdentificationDto.getPAN()) % 10 != 0) {
            return false;
        }

        return true;
    }

    public Boolean validateName(String userName, String cardHolderName) {
        if (userName.equals(cardHolderName)) {
            return true;
        }
        return false;
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

    public Boolean checkBanks(TransactionDto transactionDto) {
        List<Long> numbers = new ArrayList<>();
        Long panTemp = transactionDto.getPAN();

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


    public CardType checkCardType(Long PAN) {
        List<Long> numbers = new ArrayList<>();
        Long panTemp = PAN;

        while (panTemp > 0) {
            Long digit = panTemp % 10;
            numbers.add(digit);
            panTemp /= 10;
        }

        List<Long> cardDigits = numbers.subList(Math.max(numbers.size() - 2, 0), numbers.size());

        if (cardDigits.get(1) == 5 && cardDigits.get(0) > 0 && cardDigits.get(0) <= 5) {
            return CardType.CREDIT;
        }

        if (cardDigits.get(1) == 4) {
            return CardType.DEBIT;
        }

        return null;
    }
}
