package com.bank2.Bank2.repository;

import com.bank2.Bank2.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    //Account getByPAN(Long PAN);
}
