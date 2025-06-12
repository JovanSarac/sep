package com.example.mobilebank2.retrofit;

import com.example.mobilebank2.dto.LoginPinDto;
import com.example.mobilebank2.dto.MobileBankUserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Bank2Api {
    @Headers("Content-Type: application/json")
    @POST("/api/bank2/mobileBankAuth/login")
    Call<MobileBankUserDto> loginWithPin(@Body LoginPinDto loginPinDto);
}
