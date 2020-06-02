package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
        back=findViewById(R.id.btnBackOnUserChats);
        recyclerView=findViewById(R.id.recyclerViewUserChats);
        dBhelper=new DBhelper(this);
        chats=dBhelper.getAllCurrentUserChats();
        adapter=new CardViewToRecyclerAdapterAllUserChats(this, chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
