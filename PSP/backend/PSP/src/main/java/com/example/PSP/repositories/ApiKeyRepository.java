package com.example.PSP.repositories;

import com.example.PSP.models.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByWebShopId(Long id);
    Optional<ApiKey> findByMerchantId(UUID id);
}
