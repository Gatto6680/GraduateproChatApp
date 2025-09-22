package com.example.chatapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapp.databinding.ActivityAdminDashboardBinding;
import java.util.List;
public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding b;
    private DBHelper db;
    private UsersAdapter usersAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db = new DBHelper(this);

        usersAdapter = new UsersAdapter(
                phone -> {
                    boolean ok = db.deleteUserByPhone(phone);
                    Toast.makeText(this, ok ? "تم حذف المستخدم" : "فشل الحذف", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    loadStats();
                },
                (phone, minutes) -> {
                    boolean ok = db.updateChatDuration(phone, Math.max(1, minutes));
                    Toast.makeText(this, ok ? "تم تحديث الزمن" : "فشل التحديث", Toast.LENGTH_SHORT).show();
                    loadUsers();
                }
        );


        b.recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerUsers.setAdapter(usersAdapter);

        loadStats();
        loadUsers();

        b.btnLogout.setOnClickListener(v -> {
            new SessionManager(this).logout();
            finish();
        });
        b.btnRefresh.setOnClickListener(v -> {
            loadStats();
            loadUsers();
        });
    }

    private void loadStats() {
        int count = db.getUsersCount();
        b.tvUsersCount.setText("عدد المستخدمين: " + count);

        Cursor c = db.getAllMessages();
        int msgs = c.getCount();
        c.close();
        b.tvMessagesCount.setText("عدد الرسائل: " + msgs);
    }

    private void loadUsers() {
        List<User> list = db.getAllUsers();
        android.util.Log.d("Admin", "Users loaded: " + (list == null ? -1 : list.size()));
        usersAdapter.submitList(list);
    }
}
