package com.bank2.Bank2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="Users")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    private UUID id;
    private String name;
    private String surname;

    @OneToOne
    @JoinColumn(name = "accountNumber", nullable = false)
    private Account account;
    private String email;

    public User(UUID id, String name, String surname, Account account, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.account = account;
        this.email = email;
    }

    public User() {

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }

    public String getEmail() {
        return email;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
