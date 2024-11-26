package com.example.PSP.services;

import com.example.PSP.exceptions.ResourceNotFoundException;
import com.example.PSP.models.ApiKey;
import com.example.PSP.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Optional;
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

    public ApiKey create(Long webShopId){
        ApiKey apiKey = new ApiKey();
        apiKey.setMerchantId(UUID.randomUUID());
        apiKey.setMerchantPassword(RandomStringUtils.randomAlphanumeric(15));
        apiKey.setWebShopId(webShopId);

        return apiKeyRepository.save(apiKey);
    }

    public ApiKey findByWebShopId(Long id){
        Optional<ApiKey> optionalApiKey = apiKeyRepository.findByWebShopId(id);

        if(!optionalApiKey.isPresent())
            throw new ResourceNotFoundException("There is no api-key for webshop with id " + id);

        return optionalApiKey.get();
    }

    public ApiKey findByMerchantId(UUID id){
        Optional<ApiKey> optionalApiKey = apiKeyRepository.findByMerchantId(id);

        if(!optionalApiKey.isPresent())
            throw new ResourceNotFoundException("There is no api-key with merchantId " + id);

        return optionalApiKey.get();
    }
}
