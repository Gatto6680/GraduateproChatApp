package com.example.chatapp;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatapp.databinding.ItemMessageBinding;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.VH> {
    private Cursor cursor;

    public void submitCursor(Cursor c) {
        if (cursor != null && !cursor.isClosed()) cursor.close();
        cursor = c;
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        if (cursor == null || !cursor.moveToPosition(pos)) return;
        String sender = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_MSG_SENDER));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_MSG_TEXT));
        long time = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COL_MSG_TIME));
        h.b.tvSender.setText(sender);
        h.b.tvText.setText(text);
        h.b.tvTime.setText(android.text.format.DateFormat.format("HH:mm", time));
        // ✅ تمييز رسائل البوت
        if ("Bot".equalsIgnoreCase(sender)) {
            h.b.getRoot().setBackgroundColor(0xFFE0F7FA); // لون أزرق فاتح
            h.b.tvText.setTextColor(0xFF00796B);          // لون نص مميز
        } else {
            h.b.getRoot().setBackgroundColor(0xFFFFFFFF); // خلفية بيضاء
            h.b.tvText.setTextColor(0xFF000000);          // نص أسود
        }
    }

    @Override public int getItemCount() { return cursor == null ? 0 : cursor.getCount(); }

    static class VH extends RecyclerView.ViewHolder {
        ItemMessageBinding b;
        VH(ItemMessageBinding binding) { super(binding.getRoot()); b = binding; }
    }

}
