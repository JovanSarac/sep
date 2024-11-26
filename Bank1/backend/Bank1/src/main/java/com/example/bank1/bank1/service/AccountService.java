package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.AnswerPCCDto;
import com.example.bank1.bank1.dto.PCCRequestDto;
import com.example.bank1.bank1.dto.UserIdentificationDto;
import com.example.bank1.bank1.model.*;
import com.example.bank1.bank1.repository.AccountRepository;
import com.example.bank1.bank1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Boolean validateData(UserIdentificationDto userIdentificationDto, Account account) {
        //check pan
        //check security code
        //check if bank is the same
        Long accountPan = account.getPAN();
        if (!userIdentificationDto.PAN.equals(accountPan)) {
            return false;
        }

        Integer digitNumOfPAN = String.valueOf(Math.abs(accountPan)).length();

        if (digitNumOfPAN < 13 || digitNumOfPAN > 16) {
            return false;
        }

        if (userIdentificationDto.cardExpirationDate.getDate() != account.getCardExpirationDate().getDate()) {
            return false;
        }

        Date today = new Date();
        if (account.getCardExpirationDate().before(today)) {
            return false;
        }

        if (validatePAN(userIdentificationDto.PAN) % 10 != 0) {
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

    public Boolean checkBanks(UserIdentificationDto userIdentificationDto) {
        if (isTheBankSame(userIdentificationDto.getPAN())) {
            return true;
        } else {
            return false;
        }
    }

    public PCCRequestDto differentBanks(UserIdentificationDto userIdentificationDto) {
        PCCRequestDto pccRequestDto = new PCCRequestDto();
        pccRequestDto.setPAN(userIdentificationDto.PAN);
        pccRequestDto.setAmount(userIdentificationDto.amount);
        pccRequestDto.setSecurityCode(userIdentificationDto.securityCode);
        pccRequestDto.setCardHolderName(userIdentificationDto.cardHolderName);
        pccRequestDto.setCardExpirationDate(userIdentificationDto.cardExpirationDate);
        pccRequestDto.setAcquirerOrderId(UUID.randomUUID());
        pccRequestDto.setAcquirerTimestamp(new Date().getTime());

        String url = "http://localhost:8094/api/pcc/requests/checkAndRoute";
        HttpHeaders headers = new HttpHeaders();
        var requestEntity = new HttpEntity<>(pccRequestDto, headers);
        var method = HttpMethod.POST;

        try {
            String response = restTemplate().exchange(url, method, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error calling endpoint: " + e.getMessage());
        }

        return pccRequestDto;
    }



    public String sameBanks(UserIdentificationDto userIdentificationDto) {
        try {
            Account account = accountRepository.findByPAN(userIdentificationDto.getPAN()).get();
            if (account.getBalance() - userIdentificationDto.amount > 0) {
                List<Transaction> transactions = transactionRepository.findAllBySourceAccountNumber(account.getAccountNumber());
                List<Transaction> receivedTransactions = transactions.stream()
                        .filter(transaction -> TransactionState.RECEIVED.equals(transaction.getTransactionState()))
                        .collect(Collectors.toList());

                Double lowerLimit = 0.0;
                if (account.getCardType().equals(CardType.CREDIT)) {
                    lowerLimit = -2000.0;
                }

                if (transactions.isEmpty()) {
                    if (account.getBalance() - userIdentificationDto.amount > lowerLimit) {
                        Transaction reserveTransaction = new Transaction();
                        reserveTransaction.setTransactionNumber(UUID.randomUUID());
                        reserveTransaction.setAmount(userIdentificationDto.amount);
                        reserveTransaction.setTransactionType(TransactionType.OUT);
                        reserveTransaction.setTransactionState(TransactionState.RECEIVED);
                        reserveTransaction.setTransactionDate(new Date());
                        reserveTransaction.setSourceAccountNumber(account.getAccountNumber());
                        reserveTransaction.setDestinationAccountNumber("1234567890123456");
                        reserveTransaction.setPayerName(userIdentificationDto.cardHolderName);
                        reserveTransaction.setRecipientName("VivoNet");
                        UUID issuerOrderId = UUID.randomUUID();
                        UUID acquirerOrderId = UUID.randomUUID();
                        reserveTransaction.setIssuerOrderId(issuerOrderId);
                        reserveTransaction.setAcquirerOrderId(acquirerOrderId);
                        transactionRepository.save(reserveTransaction);

                        //poziv pcc-a
                        String url = "http://localhost:8094/api/pcc/requests/checkAndRouteBank1";
                        HttpHeaders headers = new HttpHeaders();
                        var requestEntity = new HttpEntity<>(new AnswerPCCDto(
                                "uspesno",
                                acquirerOrderId,
                                new Date().getTime(),
                                issuerOrderId,
                                new Date().getTime()), headers);
                        var method = HttpMethod.POST;

                        try {
                            String response = restTemplate().exchange(url, method, requestEntity, String.class).getBody();
                        } catch (HttpClientErrorException e) {
                            System.out.println("Error calling endpoint: " + e.getMessage());
                        }

                        return "uspesno";
                    }
                    return "neuspesno";
                }
                Double allReservedMoney = 0.0;

                for (Transaction transaction : receivedTransactions) {
                    allReservedMoney += transaction.getAmount();
                }

                if (account.getBalance() - userIdentificationDto.amount - allReservedMoney > lowerLimit) {
                    Transaction reserveTransaction = new Transaction();
                    reserveTransaction.setTransactionNumber(UUID.randomUUID());
                    reserveTransaction.setAmount(userIdentificationDto.amount);
                    reserveTransaction.setTransactionType(TransactionType.OUT);
                    reserveTransaction.setTransactionState(TransactionState.RECEIVED);
                    reserveTransaction.setTransactionDate(new Date());
                    reserveTransaction.setSourceAccountNumber(account.getAccountNumber());
                    reserveTransaction.setDestinationAccountNumber("1234567890123456");
                    reserveTransaction.setPayerName(userIdentificationDto.cardHolderName);
                    reserveTransaction.setRecipientName("VivoNet");
                    UUID issuerOrderId = UUID.randomUUID();
                    UUID acquirerOrderId = UUID.randomUUID();
                    reserveTransaction.setIssuerOrderId(issuerOrderId);
                    reserveTransaction.setAcquirerOrderId(acquirerOrderId);
                    transactionRepository.save(reserveTransaction);

                    //poziv pcc-a
                    String url = "http://localhost:8094/api/pcc/requests/checkAndRouteBank1";
                    HttpHeaders headers = new HttpHeaders();
                    var requestEntity = new HttpEntity<>(new AnswerPCCDto(
                            "uspesno",
                            acquirerOrderId,
                            new Date().getTime(),
                            issuerOrderId,
                            new Date().getTime()), headers);
                    var method = HttpMethod.POST;

                    try {
                        String response = restTemplate().exchange(url, method, requestEntity, String.class).getBody();
                    } catch (HttpClientErrorException e) {
                        System.out.println("Error calling endpoint: " + e.getMessage());
                    }
                    return "uspesno";
                }
            }
            return "neuspesno";
        } catch (Exception e) {
            return "neuspesno";
        }


    }


    public Boolean isTheBankSame(Long PAN) {
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

    public Account getAccountByPAN(Long PAN) {
        return accountRepository.findByPAN(PAN).get();
    }
}
