package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding b;
    private DBHelper db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new DBHelper(this);

        b.btnRegister.setOnClickListener(v -> {
            String name = b.etName.getText().toString().trim();
            String email = b.etEmail.getText().toString().trim();
            String phone = b.etPhone.getText().toString().trim();
            String pass = b.etPassword.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "يرجى تعبئة كل الحقول", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.phoneExists(phone)) {
                Toast.makeText(this, "رقم الهاتف مسجل مسبقًا", Toast.LENGTH_SHORT).show();
                return;
            }

            User u = new User(name, email, phone, pass, 0, 1); // غير مفعّل، زمن افتراضي 10 د
            boolean ok = db.insertUser(u);
            if (ok) {
                Intent i = new Intent(this, VerificationActivity.class);
                i.putExtra("phone", phone);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "فشل إنشاء الحساب", Toast.LENGTH_SHORT).show();
            }
        });

        b.tvGoLogin.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }
}
