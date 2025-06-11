package com.example.bank1.bank1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="MobileBankUsers")
public class MobileBankUser {
    @Id
    @SequenceGenerator(
            name = "mobile_bank_user_sequence",
            sequenceName = "mobile_bank_user_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mobile_bank_user_sequence"
    )

    private Long id;
    @OneToOne
    @JoinColumn(name = "accountNumber", nullable = false)
    private Account account;

    private String pin;

    public MobileBankUser() {
    }

    public MobileBankUser(Long id, Account account, String pin) {
        this.id = id;
        this.account = account;
        this.pin = pin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
