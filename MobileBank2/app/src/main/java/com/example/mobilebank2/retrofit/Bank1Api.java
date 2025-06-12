package com.example.mobilebank2.retrofit;

import com.example.mobilebank2.dto.QRPaymentDto;
import com.example.mobilebank2.dto.QRPaymentIdDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Bank1Api {
    @Headers("Content-Type: application/json")
    @POST("/api/bank1/accounts/validateQRData")
    Call<String> validateQRData(@Body QRPaymentDto QRPaymentDto);

    @Headers("Content-Type: application/json")
    @POST("/api/bank1/transactions/changeQRRequestState")
    Call<ResponseBody> changeQRRequestState(@Body QRPaymentIdDto qrPaymentId);
}
