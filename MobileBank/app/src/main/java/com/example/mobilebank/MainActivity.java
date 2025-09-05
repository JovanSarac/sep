package com.example.mobilebank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilebank.activities.LoginActivity;
import com.example.mobilebank.activities.PaymentResultActivity;
import com.example.mobilebank.dto.MobileBankUserDto;
import com.example.mobilebank.dto.QRPaymentDto;
import com.example.mobilebank.dto.QRPaymentIdDto;
import com.example.mobilebank.qrcode.QRCodeValidator;
import com.example.mobilebank.retrofit.QRCodeApi;
import com.example.mobilebank.retrofit.RetrofitService;
import com.example.mobilebank.session.SessionManager;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.UUID;

//import io.opentelemetry.api.common.AttributeKey;
//import io.opentelemetry.api.common.Attributes;
//import io.opentelemetry.api.trace.Span;
//import io.opentelemetry.api.trace.Tracer;
//import io.opentelemetry.context.Scope;
//import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
//import io.opentelemetry.sdk.OpenTelemetrySdk;
//import io.opentelemetry.sdk.resources.Resource;
//import io.opentelemetry.sdk.trace.SdkTracerProvider;
//import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button scan_btn, payBtn, logoutButton;

    TextView textView;
    String sellerName;
    String sellerAccountNumber;
    String buyerAccountNumber;
    String purposeOfPayment;
    String qrPaymentId;
    Double paymentAmount;
    Integer paymentCode;
    //Tracer tracer;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
//                .setEndpoint("http://10.0.2.2:4317") // Android → Host
//                .build();
//
//        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
//                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
//                .setResource(Resource.create(Attributes.of(AttributeKey.stringKey("service.name"), "MOBILEBANK")))
//                .build();
//
//        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
//                .setTracerProvider(tracerProvider)
//                .buildAndRegisterGlobal();
//
//        tracer = openTelemetry.getTracer("MOBILEBANK");
//
//        Span span = tracer.spanBuilder("MobileBankSpan").startSpan();

        MobileBankUserDto mobileBankUserDto = SessionManager.getUser(this);
        if (mobileBankUserDto == null) {
            // Nije prijavljen → nazad na login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        scan_btn = findViewById(R.id.scanner);
        textView = findViewById(R.id.text);
        payBtn = findViewById(R.id.pay_button);
        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(v -> {
            SessionManager.logout(this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        //try (Scope scope = span.makeCurrent()) {
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

            RetrofitService retrofitService = new RetrofitService();
            QRCodeApi qrCodeApi = retrofitService.getRetrofit().create(QRCodeApi.class);

            payBtn.setOnClickListener(v -> {
                QRPaymentDto qrPaymentDto = new QRPaymentDto();
                qrPaymentDto.sellerAccountNumber = sellerAccountNumber;
                qrPaymentDto.name = sellerName;
                qrPaymentDto.amount = paymentAmount;
                qrPaymentDto.paymentCode = paymentCode;
                qrPaymentDto.purposeOfPayment = purposeOfPayment;
                qrPaymentDto.buyerAccountNumber = mobileBankUserDto.accountNumber;
                qrPaymentDto.buyerName = mobileBankUserDto.name;
                QRPaymentIdDto qrPaymentIdDto = new QRPaymentIdDto();
                qrPaymentIdDto.paymentId = UUID.fromString(qrPaymentId);
                qrCodeApi.validateQRData(qrPaymentDto)
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Intent resultIntent = new Intent(MainActivity.this, PaymentResultActivity.class);
                                if (response.isSuccessful()) {

                                    resultIntent.putExtra("success", true);
                                    resultIntent.putExtra("message", response.body());
                                    Log.d("DEBUG_DTO", new Gson().toJson(qrPaymentDto));

                                    Toast.makeText(MainActivity.this, "Odgovor: " + response.body(), Toast.LENGTH_LONG).show();
                                    qrCodeApi.changeQRRequestState(qrPaymentIdDto)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    //Toast.makeText(MainActivity.this, "Odgovor: " + response.body(), Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(MainActivity.this, "Greška: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                                    Log.d("DEBUG_DTO_DRUGI_API", new Gson().toJson(qrPaymentDto));
                                                    Log.d("DEBUG_DTO_DRUGI_API", t.getMessage());
                                                }
                                            });
                                } else {
                                    Log.d("DEBUG_DTO", new Gson().toJson(qrPaymentDto));
                                    Log.e("RETROFIT_ERROR", "Error: " + response.code());
                                    Toast.makeText(MainActivity.this, "Greška: " + response.code(), Toast.LENGTH_LONG).show();
                                    String errorBody = null;
                                    try {
                                        errorBody = response.errorBody() != null ? response.errorBody().string() : "Nema detalja";
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Log.e("RETROFIT_ERROR", "Greška: " + response.code() + ", detalji: " + errorBody);

                                    resultIntent.putExtra("success", false);
                                    resultIntent.putExtra("message", errorBody);
                                }

                                startActivity(resultIntent);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("RETROFIT_ERROR", "Error: " + t.getMessage(), t);
                                Toast.makeText(MainActivity.this, "Greška: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                Intent resultIntent = new Intent(MainActivity.this, PaymentResultActivity.class);
                                resultIntent.putExtra("success", false);
                                resultIntent.putExtra("message", "Došlo je do greške: " + t.getMessage());

                                startActivity(resultIntent);
                            }
                        });
            });
        //} finally {
        //    span.end();
        //}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        Span span = tracer.spanBuilder("MobileBank2Span").startSpan();
//        try (Scope scope = span.makeCurrent()) {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                String contents = intentResult.getContents();
                //ovde treba dodati api poziv ka backendu da proveri da li je dobar format qr koda ili da se u mobilnoj to samo proveri
                String isValid = QRCodeValidator.isQRCodeValid(contents);
                if (!isValid.equals("Ispravan QR kod")) {
                    textView.setText(isValid);
                } else {
                    String[] lines = contents.split("\\|");
                    String account = "", name = "", currency = "", amount = "", sf = "", amountUSD = "";

                    for (String line : lines) {
                        if (line.startsWith("R:")) {
                            account = line.substring(2);
                            sellerAccountNumber = account;
                        } else if (line.startsWith("N:")) {
                            name = line.substring(2);
                            sellerName = name;
                        } else if (line.startsWith("I:")) {
                            currency = line.substring(2, 5); // "RSD"
                            amount = line.substring(5);      // "1500"
                            paymentAmount = Double.parseDouble(amount.replace(',', '.'));
                        } else if (line.startsWith("SF:")) {
                            paymentCode = Integer.parseInt(line.substring(3));
                        } else if (line.startsWith("S:")) {
                            purposeOfPayment = line.substring(2);
                            qrPaymentId = purposeOfPayment.replace("Plaćanje narudžbine #", "").split(" \\(USD")[0];
                            amountUSD = purposeOfPayment.replace("Plaćanje narudžbine #", "").split(" \\(USD")[1].replace("(USD", "");
                            amountUSD = amountUSD.replace(")", "");
                        }
                    }
                    // Prikaz u TextView
                    String displayText = "Naziv primaoca: " + name + "\n"
                            + "Broj računa: " + account + "\n"
                            + "Valuta: " + currency + "\n"
                            + "Iznos: " + amount + "RSD" + "\n"
                            + "IznosUSD: " + amountUSD + "USD";
                    textView.setText(displayText);

                    // Prikaz dugmeta "Plati"
                    Button payBtn = findViewById(R.id.pay_button);
                    payBtn.setVisibility(View.VISIBLE);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        //} finally {
        //    span.end();
        //}
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (intent.getBooleanExtra("reset_ui", false)) {
            textView.setText("Scan a code to see data");
            payBtn.setVisibility(View.GONE);
        }
    }
}