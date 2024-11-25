package com.example.PSP.repositories;

import com.example.PSP.models.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByWebShopId(Long id);
}
