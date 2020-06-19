package com.example.blagodari;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;
import com.example.blagodari.Models.User;

import java.io.IOException;

public class AddNewRequestActivity extends AppCompatActivity {
    ImageView photo;
    TextView title, text;
    Button save;
    ImageButton back;
    DBhelper dBhelper;
    private static final int PICK_IMG = 1;
    Uri imageUri;
    Bitmap photoBitmap;
    ConstraintLayout constraintLayout;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_request);
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
        photo = findViewById(R.id.imgOnAddRequest);
        title = findViewById(R.id.txtRequestTitleAdd);
        text = findViewById(R.id.txtTextOfRequestAdd);
        save = findViewById(R.id.btnConfirmAdd);
        back = findViewById(R.id.btnBackOnRequestAdd);
        constraintLayout = findViewById(R.id.constraintOnNewRequest);
        dBhelper = new DBhelper(this);
    }

    public void onClickRequestAdd(View v) {
        if (v.getId() == save.getId()) {
            if (title.getText().toString().length() < 1 || title.getText().toString().length() > 77 ||
                    text.getText().toString().length() < 10) {
                Toast toast = Toast.makeText(this, "Некорректно введены данные", Toast.LENGTH_LONG);
                toast.show();
            } else {
                if (photoBitmap == null) {
                    photo.setImageDrawable(null);
                }
                Request request;
                String strtitle = title.getText().toString();
                String strtext = text.getText().toString();
                User user = dBhelper.getCurrentUser();
                long time = System.currentTimeMillis() / 1000;
                if (photo.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
                    Bitmap bitmapSmall;
                    if (bitmap.getByteCount() > 350000) {
                        bitmapSmall = Request.resizeBitmap(bitmap);
                    } else {
                        bitmapSmall = bitmap;
                    }
                    request = new Request(user, strtitle, strtext, bitmapSmall, time);
                } else {
                    request = new Request(user, strtitle, strtext, time);
                }
                AddRequestTask task=new AddRequestTask();
                task.execute(request);
                Toast toast = Toast.makeText(this, "Запрос добавлен", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        } else if (v.getId() == photo.getId()) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
        } else if (v.getId() == back.getId()) {
            finish();
        }
    }

    /**
     * метод для получения фотографии из галереи
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            imageUri = data.getData(); //получить uri
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                photo.setImageBitmap(bitmap);
                photoBitmap = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddRequestTask extends AsyncTask<Request, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddNewRequestActivity.this);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Request... params) {
            try {
                dBhelper.addRequest(params[0]);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }
}
