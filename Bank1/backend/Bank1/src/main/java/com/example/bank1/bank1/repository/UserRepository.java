package com.example.bank1.bank1.repository;

import com.example.bank1.bank1.model.Account;
import com.example.bank1.bank1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
