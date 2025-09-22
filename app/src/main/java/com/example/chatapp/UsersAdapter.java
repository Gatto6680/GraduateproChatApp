package com.example.chatapp;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemUserBinding;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.VH> {

    // واجهات التحكم
    public interface OnDelete {
        void onDelete(String phone);
    }

    public interface OnUpdateDuration {
        void onUpdateDuration(String phone, int minutes);
    }

    private final OnDelete onDelete;
    private final OnUpdateDuration onUpdateDuration;
    private final List<User> data = new ArrayList<>();

    // المُنشئ الذي يستقبل واجهتين
    public UsersAdapter(OnDelete onDelete, OnUpdateDuration onUpdateDuration) {
        this.onDelete = onDelete;
        this.onUpdateDuration = onUpdateDuration;
    }

    // تحديث القائمة
    public void submitList(List<User> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        User user = data.get(position);

        // عرض البيانات
        holder.b.tvName.setText(user.getName());
        holder.b.tvPhone.setText(user.getPhone());
        holder.b.tvVerified.setText(user.getVerified() == 1 ? "مفعّل" : "غير مفعّل");
        holder.b.etDuration.setText(String.valueOf(user.getChatDuration()));

        // زر الحذف
        holder.b.btnDelete.setOnClickListener(v -> {
            if (onDelete != null) {
                onDelete.onDelete(user.getPhone());
            }
        });

        // زر تحديث الزمن
        holder.b.btnUpdateDuration.setOnClickListener(v -> {
            String input = holder.b.etDuration.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(holder.itemView.getContext(), "يرجى إدخال زمن الدردشة", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int minutes = Integer.parseInt(input);
                if (minutes < 1) {
                    Toast.makeText(holder.itemView.getContext(), "الزمن يجب أن يكون أكبر من صفر", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onUpdateDuration != null) {
                    onUpdateDuration.onUpdateDuration(user.getPhone(), minutes);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(holder.itemView.getContext(), "قيمة غير صالحة", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // ViewHolder
    static class VH extends RecyclerView.ViewHolder {
        ItemUserBinding b;
        VH(ItemUserBinding binding) {
            super(binding.getRoot());
            b = binding;
        }
    }
}
