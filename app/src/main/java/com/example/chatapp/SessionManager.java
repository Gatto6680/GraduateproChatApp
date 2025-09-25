package com.example.chatapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF = "session_pref";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IS_ADMIN = "is_admin";
    private static final String KEY_PERMANENT_BLOCK = "permanent_block1";

    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void loginAsUser(String phone) {
        sp.edit().putBoolean(KEY_LOGGED_IN, true)
                .putBoolean(KEY_IS_ADMIN, false)
                .putString(KEY_PHONE, phone)
                .apply();
    }

    public void loginAsAdmin() {
        sp.edit().putBoolean(KEY_LOGGED_IN, true)
                .putBoolean(KEY_IS_ADMIN, true)
                .putString(KEY_PHONE, "admin")
                .apply();
    }

    public void logout(DBHelper db) {
        String phone = getPhone();
        if (phone != null) {
            db.blockUser(phone); // حظر المستخدم في قاعدة البيانات
        }
        sp.edit().clear().apply(); // تسجيل الخروج
    }




    public boolean isLoggedIn() { return sp.getBoolean(KEY_LOGGED_IN, false); }
    public boolean isAdmin() { return sp.getBoolean(KEY_IS_ADMIN, false); }
    public String getPhone() { return sp.getString(KEY_PHONE, null); }
}
