package com.example.mobilebank.services;

import com.example.mobilebank.dto.UserIdentificationDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiService {
    @POST("/api/bank1/accounts/validateData")
    public Call<String> validateData(@Body UserIdentificationDto data);
}
