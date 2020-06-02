package com.example.blagodari;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}
