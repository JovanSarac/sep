package com.example.VIVONET.services;

import com.example.VIVONET.dtos.ApiKeyDto;
import com.example.VIVONET.exceptions.ResourceNotFoundException;
import com.example.VIVONET.models.ApiKey;
import com.example.VIVONET.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    @Autowired
    private PasswordEncoder encoder;

    public ApiKeyService(ApiKeyRepository apiKeyRepository){
        this.apiKeyRepository = apiKeyRepository;
    }

    public ApiKey create(ApiKeyDto apiKeyDto) {
        ApiKey apiKey = new ApiKey(0L, apiKeyDto.merchantId, apiKeyDto.merchantPassword, apiKeyDto.paymentType);
        return apiKeyRepository.save(apiKey);
    }

    public ApiKey findByPaymentTypeId(Long id){
        Optional<ApiKey> apiKey = apiKeyRepository.findByPaymentType(id);

        if(!apiKey.isPresent()) throw new ResourceNotFoundException("There is no api-key for the webShop for paymetn type " + id);

        return apiKey.get();
    }
}
