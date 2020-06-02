package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
TextView vkLink;
ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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