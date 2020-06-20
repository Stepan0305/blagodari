package com.example.blagodari.Models;
/**
 * Модель сообщения, отправленного пользователем в каком-либо чате.
 * Состоит из модели чата, двух пользователей (кто отправил и кому отправили), текста и времени отправления.
 * */
public class Message {
    private Chat chat;
    private User userFrom;
    private User userTo;
    private String text;
    private long time_created;

    public Message(Chat chat, User userFrom, User userTo, String text, long time_created) {
        this.chat = chat;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
        this.time_created = time_created;
    }

    public Chat getChat() {
        return chat;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public String getText() {
        return text;
    }

    public long getTime_created() {
        return time_created;
    }
}
