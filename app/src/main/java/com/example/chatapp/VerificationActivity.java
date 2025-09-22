package com.example.chatapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.chatapp.databinding.ActivityVerificationBinding;

import java.util.Random;

public class VerificationActivity extends AppCompatActivity {

    private ActivityVerificationBinding b;
    private DBHelper db;
    private String phone;
    private String generatedCode;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new DBHelper(this);

        phone = getIntent().getStringExtra("phone");
        generatedCode = String.valueOf(100000 + new Random().nextInt(900000));
        b.tvInfo.setText("تم إرسال كود التحقق إلى رقمك: " + phone);
        b.tvHintCode.setText("الكود التجريبي : " + generatedCode); // لأغراض النسخة التجريبية

        b.btnVerify.setOnClickListener(v -> {
            String code = b.etCode.getText().toString().trim();
            if (code.equals(generatedCode)) {
                boolean ok = db.setUserVerified(phone);
                if (ok) {
                    Toast.makeText(this, "تم التحقق بنجاح", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "خطأ في التحقق", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "الكود غير صحيح", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
