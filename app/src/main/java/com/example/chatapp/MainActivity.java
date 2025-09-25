package com.example.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText mPassword;
    Button sendOtpBtn;
    ActivityMainBinding b;
    private DBHelper db;
    private SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        SharedPreferences prefs = getSharedPreferences("session_pref", MODE_PRIVATE);
        boolean isBlocked = prefs.getBoolean("permanent_block1", false);

        if (isBlocked) {
            new AlertDialog.Builder(this)
                    .setTitle("تم حظرك نهائيًا 🚫")
                    .setMessage("لقد انتهت صلاحية استخدامك للتطبيق.\nلا يمكنك الدخول مرة أخرى.")
                    .setCancelable(false)
                    .setPositiveButton("موافق", (dialog, which) -> finish())
                    .show();
            return;
        }

 */



        mPassword = findViewById(R.id.etPassword);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPassword.setTransformationMethod(new TransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence source, View view) {

                return new Mypass(source);
            }

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

            }
        });
            b = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(b.getRoot());
            db = new DBHelper(this);
            sm = new SessionManager(this);

        b.btnLogin.setOnClickListener(v -> {
            String phone = b.etPhone.getText().toString().trim();
            String pass = b.etPassword.getText().toString();

            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "يرجى إدخال الهاتف وكلمة المرور", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ التحقق من الحظر قبل أي شيء
            if (db.isUserBlocked(phone)) {
                new AlertDialog.Builder(this)
                        .setTitle("🚫 دخول مرفوض")
                        .setMessage("هذا الرقم محظور من استخدام التطبيق.\nلا يمكنك تسجيل الدخول.")
                        .setCancelable(false)
                        .setPositiveButton("موافق", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            // ✅ تحقق من المسؤول
            if (phone.equals("admin") && pass.equals("admin")) {
                sm.loginAsAdmin();
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
                return;
            }

            // ✅ تحقق من المستخدم العادي
            if (db.isValidUser(phone, pass)) {
                sm.loginAsUser(phone);
                startActivity(new Intent(this, UserChatActivity.class));
                finish();
            } else {
                Toast.makeText(this, "بيانات غير صحيحة أو الحساب غير مفعّل", Toast.LENGTH_SHORT).show();
            }
        });


            b.tvGoRegister.setOnClickListener(v ->
                    startActivity(new Intent(this, RegisterActivity.class))
            );
        }
    public class Mypass implements CharSequence {
        private CharSequence m;
        public Mypass (CharSequence source){
            m = source;
        }

        @Override
        public int length() {
            return m.length();
        }

        @Override
        public char charAt(int index) {
            return '*';
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return m.subSequence(start, end);
        }
    }
    private void showBlockDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("تم انتهاء الجلسة")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("موافق", (dialog, which) -> dialog.dismiss())
                .show();
    }


}