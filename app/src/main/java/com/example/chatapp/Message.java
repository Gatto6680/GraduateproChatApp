package com.example.chatapp;

public class Message {
    private long id;
    private String contact;
    private String text;
    private long timestamp;
    private boolean fromMe;


    public Message(long id, String contact, String text, long timestamp, boolean fromMe) {
        this.id = id;
        this.contact = contact;
        this.text = text;
        this.timestamp = timestamp;
        this.fromMe = fromMe;
    }

    public long getId() { return id; }
    public String getContact() { return contact; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
    public boolean isFromMe() { return fromMe; }
}

