package com.example.blagodari;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;
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
    ProgressDialog pd;
    DBhelper dBhelper;
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
        dBhelper = new DBhelper(this);
    }

    public void finishSignUp(View v) {
        if (v.getId() == avatar.getId()) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
        } else if (v.getId() == signUp.getId()) {
            if (photoBitmap == null) {
                avatar.setImageDrawable(null);
            }
            User u;
            if (avatar.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                Bitmap bitmapSmall;
                if (bitmap.getByteCount() > 200000) {
                    bitmapSmall = User.resizeBitmap(bitmap);
                } else {
                    bitmapSmall = bitmap;
                }
                 u = new User(name, surname, passwd, email, time, bitmapSmall);
            } else {
                u=new User(name, surname, passwd, email, time);
            }
            new AddUserTask().execute(u);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            imageUri = data.getData(); //получить uri
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                avatar.setImageBitmap(bitmap);
                photoBitmap = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class AddUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddAvatar.this);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(User... params) {
            try {
                dBhelper.addUser(params[0]);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if ((pd!= null) && pd.isShowing()) {
                    pd.dismiss();
                }
            }  catch (final Exception e) {
            } finally {
                pd = null;
            }
        }
    }
}
