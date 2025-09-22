package com.example.chatapp;

public class User {

    private long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int verified;       // 0 = غير مفعّل، 1 = مفعّل
    private int chatDuration;   // زمن الدردشة بالدقائق

    // مُنشئ فارغ (مطلوب عند استخدام SQLite)
    public User() {}

    // مُنشئ كامل (اختياري حسب الاستخدام)
    public User(String name, String email, String phone, String password, int verified, int chatDuration) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.verified = verified;
        this.chatDuration = chatDuration;
    }

    // Getters & Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getChatDuration() {
        return chatDuration;
    }

    public void setChatDuration(int chatDuration) {
        this.chatDuration = chatDuration;
    }
}
