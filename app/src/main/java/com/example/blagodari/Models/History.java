package com.example.blagodari.Models;
/**
 * Модель истории.
 * Состоит из id, пользователя, который открыл определенный пост и модели самого поста.
 * В самом классе History лежит и News, и Request.
 * На самом деле, при создании объекта этого класса, можно положить либо News, либо Request.
 * Чтоб отличить News от Request, в коде используются проверки,
 * А в самой базе данных лежит int eventType, который показывает, какой класс созержит в себе History.
 * */
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
