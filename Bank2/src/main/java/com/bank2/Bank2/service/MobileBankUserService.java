package com.bank2.Bank2.service;

import com.bank2.Bank2.model.MobileBankUser;
import com.bank2.Bank2.repository.MobileBankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileBankUserService {
    @Autowired
    private MobileBankUserRepository mobileBankUserRepository;

    public MobileBankUser findUserByPin(String pin) {
        MobileBankUser mobileBankUser = mobileBankUserRepository.findByPin(pin).get();
        return mobileBankUser;
    }
}
