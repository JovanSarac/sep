package com.example.PSP.services;

import com.example.PSP.models.ApiKey;
import com.example.PSP.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

@Service
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    @Autowired
    private PasswordEncoder encoder;

    public ApiKeyService(ApiKeyRepository apiKeyRepository){
        this.apiKeyRepository = apiKeyRepository;
    }

    public ApiKey create(){
        ApiKey apiKey = new ApiKey();
        apiKey.setMerchantId(UUID.randomUUID());
        apiKey.setMerchantPassword(RandomStringUtils.randomAlphanumeric(15));

        return apiKeyRepository.save(apiKey);
    }
}
