package com.example.chatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "chat_app2.db";
    public static final int DB_VERSION = 2;


    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_PASSWORD = "password";
    public static final String COL_VERIFIED = "verified";
    public static final String COL_CHAT_DURATION = "chat_duration"; // minutes

    public static final String TABLE_MESSAGES = "messages";
    public static final String COL_MSG_ID = "msg_id";
    public static final String COL_MSG_SENDER = "sender_phone";
    public static final String COL_MSG_TEXT = "text";
    public static final String COL_MSG_TIME = "created_at";

    public DBHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_PHONE + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT," +
                COL_VERIFIED + " INTEGER DEFAULT 0," +
                COL_CHAT_DURATION + " INTEGER DEFAULT 10" +
                ");";

        String createMessages = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COL_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_MSG_SENDER + " TEXT," +
                COL_MSG_TEXT + " TEXT," +
                COL_MSG_TIME + " INTEGER" +
                ");";
        String createBlockedUsers = "CREATE TABLE IF NOT EXISTS BlockedUsers (" +
                "phone TEXT PRIMARY KEY" +
                ");";

        db.execSQL(createUsers);
        db.execSQL(createMessages);
        db.execSQL(createBlockedUsers); // ✅ أضف هذا السطر هنا أيضًا

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS BlockedUsers"); // ✅ تأكد من حذف الجدول القديم
        onCreate(db);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, user.getName());
        cv.put(COL_EMAIL, user.getEmail());
        cv.put(COL_PHONE, user.getPhone());
        cv.put(COL_PASSWORD, user.getPassword());
        cv.put(COL_VERIFIED, user.getVerified());
        cv.put(COL_CHAT_DURATION, user.getChatDuration());
        long res = db.insert(TABLE_USERS, null, cv);
        return res != -1;
    }

    public boolean setUserVerified(String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_VERIFIED, 1);
        int rows = db.update(TABLE_USERS, cv, COL_PHONE + "=?", new String[]{phone});
        return rows > 0;
    }

    public boolean phoneExists(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{COL_ID}, COL_PHONE + "=?", new String[]{phone}, null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public boolean isValidUser(String phone, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_PHONE + "=? AND " + COL_PASSWORD + "=? AND " + COL_VERIFIED + "=1",
                new String[]{phone, password}, null, null, null);
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    public int getUsersCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.query(TABLE_USERS, null, null, null, null, null, COL_ID + " DESC");
            while (c.moveToNext()) {
                User u = new User();
                u.setId(c.getLong(c.getColumnIndexOrThrow(COL_ID)));
                u.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
                u.setEmail(c.getString(c.getColumnIndexOrThrow(COL_EMAIL)));
                u.setPhone(c.getString(c.getColumnIndexOrThrow(COL_PHONE)));
                u.setPassword(c.getString(c.getColumnIndexOrThrow(COL_PASSWORD)));
                u.setVerified(c.getInt(c.getColumnIndexOrThrow(COL_VERIFIED)));
                // إذا عندك عمود chat_duration
                // u.setChatDuration(c.getInt(c.getColumnIndexOrThrow(COL_CHAT_DURATION)));
                list.add(u);

        }
        c.close();
        return list;
    }


    public boolean updateChatDuration(String phone, int minutes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CHAT_DURATION, minutes);
        int rows = db.update(TABLE_USERS, cv, COL_PHONE + "=?", new String[]{phone});
        return rows > 0;
    }
    public void blockUser(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("phone", phone);
        db.insertWithOnConflict("BlockedUsers", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean isUserBlocked(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT phone FROM BlockedUsers WHERE phone = ?", new String[]{phone});
        boolean blocked = cursor.moveToFirst();
        cursor.close();
        return blocked;
    }




    public int getChatDuration(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{COL_CHAT_DURATION}, COL_PHONE + "=?", new String[]{phone}, null, null, null);
        int duration = 10;
        if (c.moveToFirst()) {
            duration = c.getInt(0);
        }
        c.close();
        return duration;
    }


    public void insertMessage(String senderPhone, String text, long createdAt) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MSG_SENDER, senderPhone);
        cv.put(COL_MSG_TEXT, text);
        cv.put(COL_MSG_TIME, createdAt);
        db.insert(TABLE_MESSAGES, null, cv);
    }

    public Cursor getAllMessages() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_MESSAGES, null, null, null, null, null, COL_MSG_ID + " ASC");
    }

    public boolean deleteUserByPhone(Object phone) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_USERS, COL_PHONE + "=?", new String[]{(String) phone});
        return rows > 0;
    }


}
