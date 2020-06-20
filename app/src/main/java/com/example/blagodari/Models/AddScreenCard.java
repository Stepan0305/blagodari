package com.example.blagodari.Models;
/**
 * Это модель для карточек на экране опций
 * Её нет в базе данных, она нужна просто для удобства
 * */
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
