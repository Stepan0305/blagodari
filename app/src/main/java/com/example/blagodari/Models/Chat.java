package com.example.blagodari.Models;
/**
 * Модель чатов.
 * Состоит из id и двух пользователей, между которыми идет диалог
 * */
public class Chat {
    private int id;
    private User user1;
    private User user2;

    public Chat(int id, User user1, User user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public int getId() {
        return id;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }
}
