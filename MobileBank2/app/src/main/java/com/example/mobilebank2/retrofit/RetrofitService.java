package com.example.mobilebank2.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    public RetrofitService(String baseUrl) {
        initializeRetrofit(baseUrl);
    }

    private void initializeRetrofit(String baseUrl) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS) // vreme za povezivanje
                .readTimeout(300, TimeUnit.SECONDS)    // vreme čekanja odgovora
                .writeTimeout(300, TimeUnit.SECONDS)   // vreme za slanje podataka
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
