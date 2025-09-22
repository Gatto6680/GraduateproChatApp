package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VH> {

    public interface OnContactClick { void onClick(String name); }

    private final List<String> contacts;
    private final OnContactClick listener;
    private final int[] avatars = new int[]{
            R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3
    };

    public ContactAdapter(List<String> contacts, OnContactClick listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String name = contacts.get(position);
        h.nameText.setText(name);
        int avatarRes = avatars[position % avatars.length];
        h.avatarImage.setImageResource(avatarRes);
        h.itemView.setOnClickListener(v -> listener.onClick(name));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nameText;
        VH(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatarImage);
            nameText = itemView.findViewById(R.id.nameText);
        }
    }

}

