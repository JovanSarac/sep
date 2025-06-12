package com.example.mobilebank2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilebank2.MainActivity;
import com.example.mobilebank2.R;


public class PaymentResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        TextView resultMessage = findViewById(R.id.result_message);

        String message = getIntent().getStringExtra("message");
        resultMessage.setText(message != null ? message : "Nema poruke");

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(PaymentResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("reset_ui", true);
            startActivity(intent);
            finish();
        }, 5000);
    }
}
