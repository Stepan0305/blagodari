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

import com.example.blagodari.Models.Chat;
import com.example.blagodari.Models.DBhelper;

import java.util.ArrayList;

public class AllUserChatsActivity extends AppCompatActivity {
ImageButton back;
RecyclerView recyclerView;
CardViewToRecyclerAdapterAllUserChats adapter;
ArrayList<Chat> chats;
DBhelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_chats);
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
        back=findViewById(R.id.btnBackOnUserChats);
        recyclerView=findViewById(R.id.recyclerViewUserChats);
        dBhelper=new DBhelper(this);
        chats=dBhelper.getAllCurrentUserChats();
        adapter=new CardViewToRecyclerAdapterAllUserChats(this, chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
