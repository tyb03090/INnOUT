package com.example.luv;

public class Notice {
    private String title;
    private String content;

    public Notice() {
        // Default constructor required for calls to DataSnapshot.getValue(Notice.class)
    }

    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

