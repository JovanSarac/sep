package com.example.bank1.bank1.service;

import com.example.bank1.bank1.dto.MobileBankUserDto;
import com.example.bank1.bank1.model.MobileBankUser;
import com.example.bank1.bank1.repository.MobileBankUserRepository;
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
