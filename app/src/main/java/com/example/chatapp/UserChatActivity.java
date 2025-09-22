package com.example.chatapp;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.chatapp.databinding.ActivityUserChatBinding;

public class UserChatActivity extends AppCompatActivity {

    private ActivityUserChatBinding b;
    private DBHelper db;
    private SessionManager sm;
    private MessagesAdapter adapter;

    private CountDownTimer countDownTimer;
    private long allowedMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityUserChatBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db = new DBHelper(this);
        sm = new SessionManager(this);

        adapter = new MessagesAdapter();
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);

        // مؤقت بصري
        int allowedMinutes = db.getChatDuration(sm.getPhone());
        allowedMillis = Math.max(1, allowedMinutes) * 60L * 1000L;
        startTimer(allowedMillis);

        loadMessages();
        b.recycler.scrollToPosition(Math.max(0, adapter.getItemCount() - 1));

        b.btnSend.setOnClickListener(v -> {
            String txt = b.etMessage.getText().toString().trim();
            if (TextUtils.isEmpty(txt)) return;

            String phone = sm.getPhone();
            long now = System.currentTimeMillis();

            // حفظ رسالة المستخدم
            db.insertMessage(phone, txt, now);
            b.etMessage.setText("");
            loadMessages();
            b.recycler.scrollToPosition(Math.max(0, adapter.getItemCount() - 1));

            // الرد التلقائي بعد 1.5 ثانية
            b.recycler.postDelayed(() -> {
                String botReply = generateBotReply(txt);
                db.insertMessage("Bot", botReply, System.currentTimeMillis());
                loadMessages();
                b.recycler.scrollToPosition(Math.max(0, adapter.getItemCount() - 1));
            }, 1500);
        });

        b.btnLogout.setOnClickListener(v -> {
            sm.logout();
            finish();
        });
    }

    private void startTimer(long durationMillis) {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long min = seconds / 60;
                long sec = seconds % 60;
                b.tvTimer.setText("الزمن المتبقي: " + String.format("%02d:%02d", min, sec));
            }

            public void onFinish() {
                b.tvTimer.setText("انتهى الزمن");
                b.etMessage.setEnabled(false);
                b.btnSend.setEnabled(false);

                new androidx.appcompat.app.AlertDialog.Builder(UserChatActivity.this)
                        .setTitle("انتهاء الزمن ⏰")
                        .setMessage("انتهى زمن الدردشة المسموح به لك.\nاضغط \"حسنًا\" لإغلاق التطبيق.")
                        .setCancelable(false)
                        .setPositiveButton("حسنًا", (dialog, which) -> {
                            sm.logout(); // تسجيل الخروج
                            finish();    // إغلاق الشاشة الحالية
                        })
                        .show();
            }

        }.start();
    }

    private void loadMessages() {
        Cursor c = db.getAllMessages();
        adapter.submitCursor(c);
    }

    private String generateBotReply(String userMessage) {
        userMessage = userMessage.toLowerCase();
        if (userMessage.contains("مرحبا") || userMessage.contains("السلام")) {
            return "أهلاً بك! كيف يمكنني مساعدتك؟ 🤖";
        } else if (userMessage.contains("اسمك")) {
            return "أنا بوت مساعدك الذكي في هذا التطبيق.";
        } else if (userMessage.contains("شكرا")) {
            return "على الرحب والسعة! 😊";
        } else {
            return "تم استلام رسالتك، هل ترغب في معرفة شيء محدد؟";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
