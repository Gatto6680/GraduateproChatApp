package com.example.chatapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    public static final long SESSION_MILLIS = 1 * 60 * 1000; // 3 دقائق

    private TextView contactNameText, timerText;
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView chatRecycler;

    private String contactName;
  //  private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private ChatDatabaseHelper db;

    private CountDownTimer timer;

    // ✅ نقل المتغيرات إلى مستوى الكلاس
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean sessionActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
/*
        contactName = getIntent().getStringExtra("contactName");
        db = new ChatDatabaseHelper(this);

        contactNameText = findViewById(R.id.contactNameText);
        timerText = findViewById(R.id.timerText);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatRecycler = findViewById(R.id.chatRecycler);

        contactNameText.setText(getString(R.string.chat_with, contactName));

        // إعداد القائمة
        messages.addAll(db.getMessages(contactName));
        adapter = new MessageAdapter(messages);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        chatRecycler.setLayoutManager(lm);
        chatRecycler.setAdapter(adapter);

        sendButton.setOnClickListener(v -> sendUserMessage());

        // مؤقت الجلسة
        timer = new CountDownTimer(SESSION_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerText.setText(getString(R.string.time_remaining, seconds));
            }

            @Override
            public void onFinish() {
                sessionActive = false;
                timerText.setText(getString(R.string.session_ended));
                messageInput.setEnabled(false);
                sendButton.setEnabled(false);
                Toast.makeText(ChatActivity.this, "تم إغلاق الدردشة لانتهاء الوقت", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    private void sendUserMessage() {
        if (!sessionActive) return;

        String txt = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(txt)) return;

        long now = System.currentTimeMillis();
        db.insertMessage(contactName, txt, true, now);
        Message m = new Message(-1, contactName, txt, now, true);
        messages.add(m);
        adapter.notifyItemInserted(messages.size() - 1);
        chatRecycler.scrollToPosition(messages.size() - 1);
        messageInput.setText("");

        // رد البوت
        String reply = generateBotReply(txt);
        scheduleBotReply(reply);
    }

    private String generateBotReply(String userText) {
        String t = userText.toLowerCase();

        if (t.contains("مرحبا") || t.contains("السلام") || t.contains("هلا")) {
            return "أهلًا وسهلًا! كيف أقدر أساعدك؟";
        }
        if (t.contains("كيفك") || t.contains("اخبارك") || t.contains("خبرك")) {
            return "أنا بخير، شكرًا لسؤالك! وأنت؟";
        }
        if (t.contains("شكرا") || t.contains("شكرًا") || t.contains("thanks")) {
            return "على الرحب والسعة 😊";
        }
        if (t.contains("اسمك") || t.contains("من انت")) {
            return "أنا بوت دردشة بسيط داخل التطبيق.";
        }

        // ردود عشوائية
        String[] replies = {
                "فكرة ممتازة! احكي لي أكثر.",
                "جميل جدًا، هل لديك مثال؟",
                "هل ترغب في تجربة شيء جديد؟",
                "موافق، لنبدأ!",
                "هل تريد أن أشرح أكثر؟"
        };
        return replies[random.nextInt(replies.length)];
    }

    private void scheduleBotReply(String replyText) {
        if (!sessionActive) return;

        long delay = 800 + random.nextInt(1200); // تأخير بين 0.8 إلى 2 ثانية
        handler.postDelayed(() -> {
            if (!sessionActive) return;

            long now = System.currentTimeMillis();
            db.insertMessage(contactName, replyText, false, now);
            Message botMsg = new Message(-1, contactName, replyText, now, false);
            messages.add(botMsg);
            adapter.notifyItemInserted(messages.size() - 1);
            chatRecycler.scrollToPosition(messages.size() - 1);
        }, delay);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        handler.removeCallbacksAndMessages(null);
    }

 */
    }
}
