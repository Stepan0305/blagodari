package com.example.blagodari.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class User {
    private int id;
    private String firstName;
    private String surname;
    private String password;
    private String email;
    private long date_created;
    private String avatar;

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

    public User(int id, String firstName, String surname, String password, String email, long date_created, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.date_created = date_created;
        this.avatar = avatar;
    }

    public User(String firstName, String surname, String password, String email, long date_created, String avatar) {
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.date_created = date_created;
        this.avatar = avatar;
    }
    public User(int id, String firstName, String surname, String password, String email, long date_created, Bitmap avatar) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.date_created = date_created;
        this.avatar = bitmapToString(avatar);
    }

    public User(String firstName, String surname, String password, String email, long date_created, Bitmap avatar) {
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.date_created = date_created;
        this.avatar = bitmapToString(avatar);
    }
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public long getDate_created() {
        return date_created;
    }

    public String getAvatarAsString() {
        return avatar;
    }
    public Bitmap getAvatar(){
        return stringToBitmap(avatar);
    }
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float prefWidth=width;
        float prefHeight=height;
        while (prefHeight+prefWidth>300){
            prefHeight=prefHeight/1.5f;
            prefWidth=prefWidth/1.5f;
        }
        float scaleWidth = prefWidth / width;
        float scaleHeight = prefHeight / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }
}
