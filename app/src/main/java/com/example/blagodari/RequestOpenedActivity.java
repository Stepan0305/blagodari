package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
/**
 * Экран с открытым запросом.
 * */
public class RequestOpenedActivity extends AppCompatActivity {
    ImageView photo;
    ImageButton back;
    TextView title, text, date, creator;
    Button connect;
    DBhelper dBhelper;
    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_opened);
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
        photo = findViewById(R.id.imgOnRequestOpened);
        back = findViewById(R.id.btnBackOnRequest);
        title = findViewById(R.id.txtRequestTitle);
        text = findViewById(R.id.txtTextOfRequest);
        date = findViewById(R.id.txtDateOfRequest);
        creator = findViewById(R.id.txtNameOfRequestCreator);
        connect = findViewById(R.id.btnConnect);
        dBhelper = new DBhelper(this);
        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);
        request = dBhelper.getRequestById(id);
        if (request.getPhoto() != null) {
            photo.setImageBitmap(request.getPhoto());
        }
        title.setText(request.getTitle());
        text.setText(request.getText());
        creator.setText(request.getUser().getFirstName() + " " + request.getUser().getSurname());
        long time = request.getTime_created();
        String d = Pomogator.convertSecondsToString(time);
        date.setText(d);
        if (request.getUser().getId() == dBhelper.getCurrentUser().getId()) {
            connect.setVisibility(View.GONE);
        }
    }

    public void clickOnRequestScreen(View v) {
        if (v.getId() == back.getId()) {
            Intent i = new Intent(this, MainScreen.class);
            startActivity(i);
        } else if (v.getId() == connect.getId()) {
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra("UserTo", request.getUser().getId());
            startActivity(i);
        }
    }
}
