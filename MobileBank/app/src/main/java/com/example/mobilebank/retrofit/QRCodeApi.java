package com.example.mobilebank.retrofit;

import com.example.mobilebank.dto.QRPaymentDto;
import com.example.mobilebank.dto.QRPaymentIdDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface QRCodeApi {
    @Headers("Content-Type: application/json")
    @POST("/api/bank1/accounts/validateQRData")
    Call<String> validateQRData(@Body QRPaymentDto QRPaymentDto);

    @Headers("Content-Type: application/json")
    @POST("/api/bank1/transactions/changeQRRequestState")
    Call<String> changeQRRequestState(@Body QRPaymentIdDto qrPaymentId);
}
