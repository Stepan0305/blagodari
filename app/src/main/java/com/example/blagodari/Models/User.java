package com.example.blagodari.Models;

public class User {
    private int id;
    private String firstName;
    private String surname;
    private String password;
    private String email;
    private long date_created;

    public User(int id, String firstName, String surname, String password, String email, long date_created) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.date_created = date_created;
    }

    public User(String firstName, String surname, String password, String email, long date_created) {
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.date_created = date_created;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }
}
