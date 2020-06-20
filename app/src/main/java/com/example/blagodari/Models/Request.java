package com.example.blagodari.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
/**
 * Модель запроса.
 * Состоит из id, заголовка, текста, фото, модели создавшего пользователя и времени создания.
 * */
public class Request {
    private int id;
    private User user;
    private String title;
    private String text;
    private String photo;
    private long time_created;

    public Request(int id, User user, String title, String text, Bitmap photo, long time_created) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.text = text;
        this.photo = bitmapToString(photo);
        this.time_created = time_created;
    }
    public Request(User user, String title, String text, Bitmap photo, long time_created) {
        this.user = user;
        this.title = title;
        this.text = text;
        this.photo = bitmapToString(photo);
        this.time_created = time_created;
    }
    public Request(int id, User user, String title, String text, long time_created) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.text = text;
        this.time_created = time_created;
    }

    public Request(User user, String title, String text, long time_created) {
        this.user = user;
        this.title = title;
        this.text = text;
        this.time_created = time_created;
    }

    public Request(int id, User user, String title, String text, String photo, long time_created) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.text = text;
        this.photo = photo;
        this.time_created = time_created;
    }

    public User getUser() {
        return user;
    }

    public long getTime_created() {
        return time_created;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float prefWidth=width;
        float prefHeight=height;
        while (prefHeight+prefWidth>800){
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

