package com.example.mobilebank.retrofit;

import com.example.mobilebank.dto.LoginPinDto;
import com.example.mobilebank.dto.MobileBankUserDto;
import com.example.mobilebank.dto.QRPaymentDto;
import com.example.mobilebank.dto.QRPaymentIdDto;

import okhttp3.ResponseBody;
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
    Call<ResponseBody> changeQRRequestState(@Body QRPaymentIdDto qrPaymentId);

    @Headers("Content-Type: application/json")
    @POST("/api/bank1/mobileBankAuth/login")
    Call<MobileBankUserDto> loginWithPin(@Body LoginPinDto loginPinDto);
}
