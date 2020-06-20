package com.example.blagodari;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Класс, содержащий разные полезные функции, используемые в проекте.
 * */
public class Pomogator {
    public static String convertSecondsToString(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time*1000);
        return sdf.format(calendar.getTime());
    }
    public static String convertSecondsToStringDateTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'в' HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time*1000);
        return sdf.format(calendar.getTime());
    }
    public static boolean checkInputSignUp(String name, String surname, String email, String password){
        if (name.length() > 128 || name.length() < 1 ||
                surname.length() > 128 || surname.length() < 1 ||
                email.length() > 128 || email.length() < 1 ||
                password.length() > 50 || password.length() < 6||!email.matches("^[.a-zA-Z0-9]+@[a-zA-Z0-9]+(.[a-zA-Z]{2,})$")){
            return false;
        }else return true;
    }
}
