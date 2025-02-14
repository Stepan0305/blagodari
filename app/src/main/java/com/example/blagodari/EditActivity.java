package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;

import java.util.ArrayList;
/**
 * Здесь отображаются все запросы, выложенные пользователем.
 * Удалить их можно прям отсюда.
 * Если же захочется отредактировать запрос, то вас перекинет на EditRequestActivity.
 * */
public class EditActivity extends AppCompatActivity {
ImageButton back;
RecyclerView recyclerView;
CardViewToRecyclerAdapterEdit adapterEdit;
ArrayList<Request> requests;
DBhelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
        dBhelper=new DBhelper(this);
        back=findViewById(R.id.btnBackOnEdit);
        requests=dBhelper.getAllUserRequests(dBhelper.getCurrentUser());
        adapterEdit=new CardViewToRecyclerAdapterEdit(this, requests);
        recyclerView=findViewById(R.id.recyclerViewEditScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterEdit);
    }
    public void onClick(View v){
        Intent intent=new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}
