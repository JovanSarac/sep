package com.bank2.Bank2.service;

import com.bank2.Bank2.model.MobileBankUser;
import com.bank2.Bank2.repository.MobileBankUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class MobileBankUserService {
    @Autowired
    private MobileBankUserRepository mobileBankUserRepository;

    private static final Logger logger = LoggerFactory.getLogger(MobileBankUserService.class);

    public MobileBankUser findUserByPin(String pin) {
        logger.info("Retrieving user by pin " + Base64.getEncoder().encodeToString(pin.getBytes()));
        MobileBankUser mobileBankUser = mobileBankUserRepository.findByPin(pin).get();
        return mobileBankUser;
    }
}
