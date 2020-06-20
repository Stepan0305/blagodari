package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.News;
/**
 * Страница с открытой новостью
 * */
public class NewsOpenedActivity extends AppCompatActivity {
    TextView text, title, date;
    ImageView photo;
    ImageButton back;
    DBhelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_opened);
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
        text=findViewById(R.id.txtTextOnNewsOpened);
        title=findViewById(R.id.txtTitleOnNewsOpened);
        photo=findViewById(R.id.imgOnNewsOpened);
        back=findViewById(R.id.btnBackOnNewsOpened);
        date=findViewById(R.id.txtDateOnNewsOpened);
        dBhelper=new DBhelper(this);
        Intent i=getIntent();
        int id=i.getIntExtra("id", 0);
        News news=dBhelper.getNewsById(id);
        title.setText(news.getTitle());
        text.setText(news.getText());
        photo.setImageBitmap(news.getPhoto());
        date.setText(Pomogator.convertSecondsToString(news.getTime_created()));
    }
    public void back(View v){
        finish();
    }
}
