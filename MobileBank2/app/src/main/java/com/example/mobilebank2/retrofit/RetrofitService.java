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
                .hostnameVerifier((hostname, session) -> {
                    // Allow connections to your specific IP
                    return hostname.equals("192.168.100.222");
                })
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
//    private void initializeRetrofit() {
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//                .setLenient()
//                .create();
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(300, TimeUnit.SECONDS)
//                .readTimeout(300, TimeUnit.SECONDS)
//                .writeTimeout(300, TimeUnit.SECONDS)
//                .hostnameVerifier((hostname, session) -> {
//                    // Allow connections to any IP in your local network range
//                    return hostname.startsWith("192.168.100.");
//                })
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl) // assuming you're passing baseUrl to constructor
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }

//    private void initializeRetrofit() {
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//                .setLenient()
//                .create();
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(300, TimeUnit.SECONDS)
//                .readTimeout(300, TimeUnit.SECONDS)
//                .writeTimeout(300, TimeUnit.SECONDS)
//                .hostnameVerifier((hostname, session) -> {
//                    // Allow connections to your specific IP
//                    return hostname.equals("192.168.100.222");
//                })
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://192.168.100.222:8091/")
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
