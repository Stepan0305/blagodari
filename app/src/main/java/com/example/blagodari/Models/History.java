package com.example.blagodari.Models;

public class History {
    private int id;
    private User user_opened;
    private Request request;
    private News news;

    public History(int id, User user_opened, Request request) {
        this.id = id;
        this.user_opened = user_opened;
        this.request = request;
    }

    public History(int id, User user_opened, News news) {
        this.id = id;
        this.user_opened = user_opened;
        this.news = news;
    }

    public History(User user_opened, Request request) {
        this.user_opened = user_opened;
        this.request = request;
    }

    public History(User user_opened, News news) {
        this.user_opened = user_opened;
        this.news = news;
    }

    public int getId() {
        return id;
    }

    public User getUser_opened() {
        return user_opened;
    }

    public Request getRequest() {
        return request;
    }

    public News getNews() {
        return news;
    }
}
