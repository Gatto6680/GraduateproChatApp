package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager sm = new SessionManager(this);
        if (sm.isLoggedIn()) {
            if (sm.isAdmin()) startActivity(new Intent(this, AdminDashboardActivity.class));
            else startActivity(new Intent(this, UserChatActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
