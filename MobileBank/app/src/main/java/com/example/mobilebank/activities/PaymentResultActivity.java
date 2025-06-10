package com.example.mobilebank.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilebank.R;

public class PaymentResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        TextView resultMessage = findViewById(R.id.result_message);

        String message = getIntent().getStringExtra("message");
        resultMessage.setText(message != null ? message : "Nema poruke");
    }
}
