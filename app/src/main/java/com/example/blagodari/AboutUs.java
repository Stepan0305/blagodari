package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * Экран, содержащий информацию о разработчике.
 * */
public class AboutUs extends AppCompatActivity {
TextView vkLink;
ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else connected=false;
        if (!connected){
            Intent i=new Intent(this, NoConnection.class);
            startActivity(i);
            finish();
        }
        vkLink=findViewById(R.id.vk);
        back=findViewById(R.id.btnBackOnAbout);
    }
    public void aboutUs(View v){
        if (v.getId()==vkLink.getId()){
            Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/windof"));
            startActivity(i);
        } else if (v.getId()==back.getId()){
            finish();
        }
    }
}