package com.example.mobilebank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilebank.MainActivity;
import com.example.mobilebank.R;
import com.example.mobilebank.dto.LoginPinDto;
import com.example.mobilebank.dto.MobileBankUserDto;
import com.example.mobilebank.retrofit.QRCodeApi;
import com.example.mobilebank.retrofit.RetrofitService;
import com.example.mobilebank.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText pinInput;
    Button loginButton;
    TextView errorMessage;

    private final String correctPin = "1234";

    RetrofitService retrofitService = new RetrofitService();
    QRCodeApi qrCodeApi = retrofitService.getRetrofit().create(QRCodeApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pinInput = findViewById(R.id.pin_input);
        loginButton = findViewById(R.id.login_button);
        errorMessage = findViewById(R.id.error_message);

        // Ograničavanje dužine na 4 cifre
        pinInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        loginButton.setOnClickListener(v -> {
            String enteredPin = pinInput.getText().toString();

            LoginPinDto loginPinDto = new LoginPinDto();
            loginPinDto.pin = pinInput.getText().toString();

            qrCodeApi.loginWithPin(loginPinDto).enqueue(new Callback<MobileBankUserDto>() {
                @Override
                public void onResponse(Call<MobileBankUserDto> call, Response<MobileBankUserDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MobileBankUserDto mobileBankUserDto = response.body();
                        SessionManager.saveUser(LoginActivity.this, mobileBankUserDto);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        errorMessage.setText("Neispravan PIN.");
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<MobileBankUserDto> call, Throwable t) {
                    errorMessage.setText("Greška u komunikaciji.");
                    errorMessage.setVisibility(View.VISIBLE);
                }
            });
        });
    }

}
