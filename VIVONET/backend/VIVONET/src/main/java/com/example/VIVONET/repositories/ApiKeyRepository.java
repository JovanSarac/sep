package com.example.VIVONET.repositories;

import com.example.VIVONET.models.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    public Optional<ApiKey> findByPaymentType(Long id);
}
