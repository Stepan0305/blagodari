package com.example.blagodari.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class News {
    private int id;
    private String title;
    private String text;
    private String photo;
    private long time_created;

    public News(String title, String text, String photo, long time_created) {
        this.title = title;
        this.text = text;
        this.photo = photo;
        this.time_created = time_created;
    }

    public News(int id, String title, String text, String photo, long time_created) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.photo = photo;
        this.time_created = time_created;
    }
    public News(String title, String text, Bitmap photo, long time_created){
        this.title = title;
        this.text = text;
        this.photo = bitmapToString(photo);
        this.time_created = time_created;
    }
    public News(int id, String title, String text, Bitmap photo, long time_created){
        this.id = id;
        this.title = title;
        this.text = text;
        this.photo = bitmapToString(photo);
        this.time_created = time_created;
    }

    public News(String title, String text, long time_created) {
        this.title = title;
        this.text = text;
        this.time_created = time_created;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return text;
    }

    public long getTime_created() {
        return time_created;
    }
    public Bitmap getPhoto() {
        return stringToBitmap(this.photo);
    }
    public String getPhotoAsString(){
        return this.photo;
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
}
