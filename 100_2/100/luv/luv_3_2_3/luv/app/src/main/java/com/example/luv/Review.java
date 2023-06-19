package com.example.luv;

import android.graphics.Bitmap;

public class Review {
    private String title;
    private String content;
    private int score;
    private String lockerNumber;
    private String author;
    private Bitmap photo;

    public Review(String title, String content, int score, String lockerNumber, String author, Bitmap photo) {
        this.title = title;
        this.content = content;
        this.score = score;
        this.lockerNumber = lockerNumber;
        this.author = author;
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public String getLockerNumber() {
        return lockerNumber;
    }

    public String getAuthor() {
        return author;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
