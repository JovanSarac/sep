package com.example.mobilebank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilebank.dto.UserIdentificationDto;
import com.example.mobilebank.services.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button scan_btn, payBtn;

    TextView textView;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan_btn = findViewById(R.id.scanner);
        textView = findViewById(R.id.text);
        payBtn = findViewById(R.id.pay_button);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        payBtn.setOnClickListener(v -> {

            int cvc = 123;
            Date expirationDate;
            try {
                expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2025-09-25");
            } catch (ParseException e) {
                Toast.makeText(this, "Neispravan datum!", Toast.LENGTH_SHORT).show();
                return;
            }

            UserIdentificationDto dto = new UserIdentificationDto();
            dto.setPAN(4111111111111111L);
            dto.setSecurityCode(cvc);
            dto.setCardHolderName("Leopoldina Djanic");
            dto.setCardExpirationDate(expirationDate);
            dto.setAmount(15000.0);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.156:8091/") // emulator -> localhost
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            apiService.validateData(dto).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Odgovor: " + response.body(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Greška: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.e("RETROFIT_ERROR", "Error: " + t.getMessage(), t);
                    Toast.makeText(MainActivity.this, "Greška: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null){
                // Parsiranje QR sadržaja
                String[] lines = contents.split("\n");
                String account = "", name = "", currency = "", amount = "";

                for (String line : lines) {
                    if (line.startsWith("R|")) {
                        account = line.substring(2);
                    } else if (line.startsWith("N|")) {
                        name = line.substring(2);
                    } else if (line.startsWith("I|")) {
                        currency = line.substring(2, 5); // "RSD"
                        amount = line.substring(5);      // "1500"
                    }
                }

                // Prikaz u TextView
                String displayText = "Naziv primaoca: " + name + "\n"
                        + "Broj računa: " + account + "\n"
                        + "Valuta: " + currency + "\n"
                        + "Iznos: " + amount;
                textView.setText(displayText);

                // Prikaz dugmeta "Plati"
                Button payBtn = findViewById(R.id.pay_button);
                payBtn.setVisibility(View.VISIBLE);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}