package com.example.mobilebank.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    public RetrofitService() {
        initializeRetrofit();
    }

//    private void initializeRetrofit() {
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//                .setLenient()
//                .create();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(300, TimeUnit.SECONDS) // vreme za povezivanje
//                .readTimeout(300, TimeUnit.SECONDS)    // vreme čekanja odgovora
//                .writeTimeout(300, TimeUnit.SECONDS)   // vreme za slanje podataka
//                .build();
////        retrofit = new Retrofit.Builder()
////                .baseUrl("https://192.168.100.222:8091/")
////                .client(okHttpClient)
////                .addConverterFactory(GsonConverterFactory.create(gson))
////                .build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://localhost:8091/")
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }

    private void initializeRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> {
                    // Allow connections to your specific IP
                    return hostname.equals("192.168.100.222");
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://192.168.100.222:8091/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
