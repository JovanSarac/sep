package com.bank2.Bank2.controller;

import com.bank2.Bank2.dto.LoginPinDto;
import com.bank2.Bank2.dto.MobileBankUserDto;
import com.bank2.Bank2.model.Account;
import com.bank2.Bank2.model.MobileBankUser;
import com.bank2.Bank2.model.User;
import com.bank2.Bank2.repository.UserRepository;
import com.bank2.Bank2.service.AccountService;
import com.bank2.Bank2.service.MobileBankUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank2/mobileBankAuth")
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
        if (mobileBankUser == null) {
            return (ResponseEntity<MobileBankUserDto>) ResponseEntity.badRequest();
        }
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
