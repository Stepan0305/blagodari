package com.example.blagodari.Models;

public class AddScreenCard {
    private int photo;
    private String text;

    public AddScreenCard(int photo, String text) {
        this.photo = photo;
        this.text = text;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
