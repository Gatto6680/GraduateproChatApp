package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    RecyclerView contactsRecycler;
    List<String> contacts = Arrays.asList("سارة", "أحمد", "ليلى", "يوسف", "نورا");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        contactsRecycler = findViewById(R.id.contactsRecycler);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(this));
        ContactAdapter adapter = new ContactAdapter(contacts, (name) -> {
            Intent i = new Intent(ContactsActivity.this, ChatActivity.class);
            i.putExtra("contactName", name);
            startActivity(i);
        });
        contactsRecycler.setAdapter(adapter);
    }
}
