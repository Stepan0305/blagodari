package com.example.blagodari;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.User;

import java.io.IOException;

public class AddAvatar extends AppCompatActivity {
    ImageView avatar;
    Button signUp;
    long time;
    String name, surname, passwd, email;
    private static final int PICK_IMG = 1;
    Uri imageUri;
    Bitmap photoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_avatar);
        avatar = findViewById(R.id.imgAddAvatar);
        signUp = findViewById(R.id.btnFinishSignUp);
        Intent i = getIntent();
        time = i.getLongExtra("time", 0);
        name = i.getStringExtra("name");
        surname = i.getStringExtra("surname");
        passwd = i.getStringExtra("passwd");
        email = i.getStringExtra("email");
    }

    public void finishSignUp(View v) {
        if (v.getId() == avatar.getId()) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
        } else if (v.getId()==signUp.getId()){
            if (photoBitmap==null){
                avatar.setImageResource(R.drawable.nophoto);
            }
            Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
            Bitmap bitmapSmall;
            if (bitmap.getByteCount()>250000){
                bitmapSmall=User.resizeBitmap(bitmap);
            } else{
                bitmapSmall=bitmap;
            }
            User u=new User(name, surname, passwd, email, time, bitmapSmall);
            DBhelper dBhelper=new DBhelper(this);
            System.out.println(bitmap.getByteCount());
            System.out.println(bitmapSmall.getByteCount());
            dBhelper.addUser(u);
            Intent i=new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMG && resultCode==RESULT_OK){
            imageUri=data.getData(); //получить uri
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                avatar.setImageBitmap(bitmap);
                photoBitmap=bitmap;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
