package com.example.bank1.bank1.controller;

import com.example.bank1.bank1.dto.LoginPinDto;
import com.example.bank1.bank1.dto.MobileBankUserDto;
import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.MobileBankUser;
import com.example.bank1.bank1.model.User;
import com.example.bank1.bank1.repository.UserRepository;
import com.example.bank1.bank1.service.AccountService;
import com.example.bank1.bank1.service.MobileBankUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank1/mobileBankAuth")
public class MobileBankLogin {
    @Autowired
    private MobileBankUserService mobileBankUserService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<MobileBankUserDto> loginWithPin(@RequestBody LoginPinDto loginPinDto) {
        MobileBankUser mobileBankUser = mobileBankUserService.findUserByPin(loginPinDto.pin);
        //treba dodati proveru da li postoji taj user sa tim pinom, ako ne postoji da se vrati:
        //return (ResponseEntity<PaymentDataQRDto>) ResponseEntity.badRequest();
        Account account = mobileBankUser.getAccount();
        User user = userRepository.findUserByAccount_Id(account.getId());
        MobileBankUserDto mobileBankUserDto = new MobileBankUserDto();
        mobileBankUserDto.accountNumber = account.getAccountNumber();
        mobileBankUserDto.PAN = account.getPAN();
        mobileBankUserDto.balance = account.getBalance();
        mobileBankUserDto.securityCode = account.getSecurityCode();
        mobileBankUserDto.name = user.getName();
        return ResponseEntity.ok(mobileBankUserDto);
    }
}
