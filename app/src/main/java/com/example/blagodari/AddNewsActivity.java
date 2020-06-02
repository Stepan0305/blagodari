package com.example.blagodari;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.News;
import com.example.blagodari.Models.Request;
import com.example.blagodari.Models.User;

import java.io.IOException;

public class AddNewsActivity extends AppCompatActivity {
    EditText title, text;
    ImageButton back;
    Button save;
    ImageView photo;
    DBhelper dBhelper;
    private static final int PICK_IMG = 1;
    Uri imageUri;
    Bitmap photoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        text = findViewById(R.id.inputTextOnAddNews);
        title = findViewById(R.id.inputTitleOnAddNews);
        back = findViewById(R.id.btnBackOnAddNews);
        save = findViewById(R.id.btnSaveOnAddNews);
        photo = findViewById(R.id.imgOnAddNews);
        dBhelper = new DBhelper(this);
        title.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void onAddNews(View v) {
        if (v.getId() == photo.getId()) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
        } else if (v.getId() == save.getId()) {
            if (title.getText().toString().length() < 1 || title.getText().toString().length() > 77 ||
                    text.getText().toString().length() < 10) {
                Toast toast = Toast.makeText(this, "Некорректно введены данные", Toast.LENGTH_LONG);
                toast.show();
            } else {
                if (photoBitmap == null) {
                    photo.setImageResource(R.drawable.nophoto);
                }
                Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
                String strtitle = title.getText().toString();
                String strtext = text.getText().toString();
                long time = System.currentTimeMillis() / 1000;
                News news=new News(strtitle, strtext, bitmap, time);
                dBhelper.addNews(news);
                Toast toast = Toast.makeText(this, "Новость добавлена", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        } else if (v.getId()==back.getId()){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            imageUri = data.getData(); //получить uri
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                photo.setImageBitmap(bitmap);
                photoBitmap=bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
