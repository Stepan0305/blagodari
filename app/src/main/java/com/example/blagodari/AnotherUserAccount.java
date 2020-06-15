package com.example.blagodari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blagodari.Models.Chat;
import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Message;
import com.example.blagodari.Models.User;

public class AnotherUserAccount extends AppCompatActivity {
    ImageView avatar, like;
    ImageButton back;
    Button connect, complain;
    TextView name, countLikes;
    DBhelper dBhelper;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_account);
        avatar = findViewById(R.id.imgAvatarOnUserOpened);
        back = findViewById(R.id.btnBackOnUserOpened);
        connect = findViewById(R.id.btnConnectOnUserOpened);
        complain = findViewById(R.id.btnComplainOnUser);
        name = findViewById(R.id.txtNameOnUserOpened);
        like = findViewById(R.id.btnLikeUser);
        countLikes = findViewById(R.id.txtLikeCountOnUserOpened);
        dBhelper = new DBhelper(this);
        Intent i = getIntent();
        int userId = i.getIntExtra("userId", 0);
        currentUser = dBhelper.getUserById(userId);
        if (currentUser.getAvatar() != null) {
            avatar.setImageBitmap(currentUser.getAvatar());
        }
        if (dBhelper.checkIfAlreadyLiked(dBhelper.getCurrentUser(), currentUser)) {
            like.setImageResource(R.drawable.like_active);
        }
        name.setText(currentUser.getFirstName() + " " + currentUser.getSurname());
        countLikes.setText(dBhelper.likeCount(currentUser) + "");
        registerForContextMenu(complain);
    }

    public void onUserAccount(View v) {
        if (v.getId() == connect.getId()) {
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra("UserTo", currentUser.getId());
            startActivity(i);
        } else if (v.getId() == like.getId()) {
            if (dBhelper.getCurrentUser().getId() == currentUser.getId()) {
                like.setVisibility(View.GONE);
            } else {
                if (!dBhelper.checkIfAlreadyLiked(dBhelper.getCurrentUser(), currentUser)) { //если еще не лайкнул
                    dBhelper.addLike(dBhelper.getCurrentUser(), currentUser);
                    like.setImageResource(R.drawable.like_active);
                    countLikes.setText(dBhelper.likeCount(currentUser) + "");
                } else {
                    dBhelper.deleteLike(dBhelper.getCurrentUser(), currentUser);
                    like.setImageResource(R.drawable.like_non_active);
                    countLikes.setText(dBhelper.likeCount(currentUser) + "");
                }
            }
        } else if (v.getId() == back.getId()) {
            finish();
        } else if (v.getId() == complain.getId()) {
            openContextMenu(complain);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "Агрессивное поведение");
        menu.add(0, 2, 0, "Спам");
        menu.add(0, 3, 0, "Мошенничество");
        menu.add(0, 4, 0, "Другое");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        User admin = dBhelper.getUserById(DBhelper.ADMIN_ID);
        User current = dBhelper.getCurrentUser();
        Chat chat;
        long time = System.currentTimeMillis() / 1000;
        if (dBhelper.getChatByTwoUsers(admin, current) != null) { //проверка, есть ли уже такой чат
            chat = dBhelper.getChatByTwoUsers(admin, current);
        } else {
            dBhelper.createNewChat(admin, current);
            chat = dBhelper.getChatByTwoUsers(admin, current);
        }
        Message m;
        switch (item.getItemId()) {
            case 1:
                m = new Message(chat, current, admin, "Пользователь с id " + currentUser.getId() + " ведет" +
                        " себя агрессивно!", time);
                dBhelper.createNewMessage(m);
                break;
            case 2:
                m = new Message(chat, current, admin, "Пользователь с id " + currentUser.getId() + " занимается" +
                        " спамом!", time);
                dBhelper.createNewMessage(m);
                break;
            case 3:
                m = new Message(chat, current, admin, "Пользователь с id " + currentUser.getId() + " - мошенник!", time);
                dBhelper.createNewMessage(m);
                break;
            case 4:
                m = new Message(chat, current, admin, "Пользователь с id " + currentUser.getId() + " ведет себя" +
                        " неприемлимо и нарушает правила проекта!", time);
                dBhelper.createNewMessage(m);
                break;
        }
        Toast.makeText(this, "Ваша жалоба успешно отправлена!", Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);
    }
}
