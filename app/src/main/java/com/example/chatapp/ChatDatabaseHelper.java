package com.example.chatapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "chat.db";
    private static final int DB_VERSION = 1;

    public ChatDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE messages (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "contact TEXT NOT NULL," +
                        "text TEXT NOT NULL," +
                        "timestamp INTEGER NOT NULL," +
                        "from_me INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS messages");
        onCreate(db);
    }

    public long insertMessage(String contact, String text, boolean fromMe, long timestamp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("contact", contact);
        v.put("text", text);
        v.put("timestamp", timestamp);
        v.put("from_me", fromMe ? 1 : 0);
        return db.insert("messages", null, v);
    }

    public List<Message> getMessages(String contact) {
        List<Message> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, contact, text, timestamp, from_me FROM messages WHERE contact=? ORDER BY timestamp ASC",
                new String[]{contact}
        );
        while (c.moveToNext()) {
            result.add(new Message(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getLong(3),
                    c.getInt(4) == 1
            ));
        }
        c.close();
        return result;
    }
}

