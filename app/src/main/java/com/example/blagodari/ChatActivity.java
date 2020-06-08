package com.example.blagodari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.blagodari.Models.Chat;
import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Message;
import com.example.blagodari.Models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    ImageButton back, refresh, send;
    TextView name;
    RecyclerView recyclerView;
    EditText input;
    DBhelper dBhelper;
    User userTo;
    User current;
    Chat chat;
    ArrayList<Message> messages;
    CardViewToRecyclerAdapterChat adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        back = findViewById(R.id.btnBackOnChat);
        refresh = findViewById(R.id.btnRefreshOnChat);
        send = findViewById(R.id.btnSendOnChat);
        name = findViewById(R.id.txtNameOnChat);
        input = findViewById(R.id.inputMessageOnChat);
        dBhelper = new DBhelper(this);
        recyclerView = findViewById(R.id.recyclerViewChat);
        Intent i = getIntent();
        int userToId=i.getIntExtra("UserTo", 0);
        int chatId=i.getIntExtra("chat", 0);
        current = dBhelper.getCurrentUser();
        if (userToId!=0) {          //если с открытого запроса или профиля юзера
            userTo = dBhelper.getUserById(userToId);
            if (dBhelper.getChatByTwoUsers(userTo, current) != null) { //проверка, есть ли уже такой чат
                chat = dBhelper.getChatByTwoUsers(userTo, current);
            } else {
                dBhelper.createNewChat(userTo, current);
                chat = dBhelper.getChatByTwoUsers(userTo, current);
            }
        } else{                                              //если со списка чатов юзера
            chat=dBhelper.getChatById(chatId);
            if (chat.getUser1().getId()==current.getId()){
                userTo=chat.getUser2();
            } else {
                userTo=chat.getUser1();
            }
        }
        messages = dBhelper.getAllChatMessages(chat);
        adapter = new CardViewToRecyclerAdapterChat(this, messages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        String strname=userTo.getFirstName()+" "+userTo.getSurname();
        if (strname.length()>25){
            name.setTextSize(13);
        } else if (strname.length()>35){
            name.setTextSize(10);
        }
        name.setText(strname);
    }

    public void onChatClick(View v) {
        if (v.getId() == send.getId()) {
            if (input.getText().toString().length() > 0) {
                String strText = input.getText().toString();
                long time=System.currentTimeMillis()/1000;
                Message message=new Message(chat, current, userTo, strText, time);
                dBhelper.createNewMessage(message);
                messages=dBhelper.getAllChatMessages(chat);
                adapter=new CardViewToRecyclerAdapterChat(this, messages);
                recyclerView.setAdapter(adapter);
                input.setText("");
            }
        } else if (v.getId()==back.getId()){
            finish();
        }else if (v.getId()==refresh.getId()){
            messages=dBhelper.getAllChatMessages(chat);
            adapter=new CardViewToRecyclerAdapterChat(this, messages);
            recyclerView.setAdapter(adapter);
        } else if(v.getId()==name.getId()){
            Intent i=new Intent(this, AnotherUserAccount.class);
            i.putExtra("userId", userTo.getId());
            startActivity(i);
        }
    }

}
