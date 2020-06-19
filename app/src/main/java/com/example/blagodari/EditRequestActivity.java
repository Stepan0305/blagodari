package com.example.blagodari;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;
import com.example.blagodari.Models.User;

import java.io.IOException;

public class EditRequestActivity extends AppCompatActivity {
    ImageButton back;
    EditText title, text;
    ImageView image;
    Button save;
    private static final int PICK_IMG=1;
    Uri imageUri;
    DBhelper dBhelper;
    int id;
    Bitmap photoBitmap;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);
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
        back=findViewById(R.id.btnBackOnRequestEdit);
        title=findViewById(R.id.txtRequestTitleEdit);
        text=findViewById(R.id.txtTextOfRequestEdit);
        image=findViewById(R.id.imgOnRequestEdit);
        save=findViewById(R.id.btnConfirm);
        dBhelper=new DBhelper(this);
        Intent intent=getIntent();
        id=intent.getIntExtra("id", -1);
        if (id!=-1){
            Request request=dBhelper.getRequestById(id);
            title.setText(request.getTitle());
            text.setText(request.getText());
            image.setImageBitmap(request.getPhoto());
        }
    }
    public void confirm(View v){
        if (v.getId()==image.getId()){
            Intent gallery=new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);

        } else if (v.getId()==save.getId()){
            if (title.getText().toString().length() < 1 || title.getText().toString().length() > 77 ||
                    text.getText().toString().length() < 10) {
                Toast toast = Toast.makeText(this, "Некорректно введены данные", Toast.LENGTH_LONG);
                toast.show();
            }else {
            String strtitle=title.getText().toString();
            String strtext=text.getText().toString();
            Request r=dBhelper.getRequestById(id);
            User user=r.getUser();
            long time=r.getTime_created();
            Request request;
                if (photoBitmap != null) {
                   // Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    Bitmap bitmap=photoBitmap;
                    Bitmap bitmapSmall;
                    if (bitmap.getByteCount() > 350000) {
                        bitmapSmall = Request.resizeBitmap(bitmap);
                    } else {
                        bitmapSmall = bitmap;
                    }
                    request = new Request(r.getId(),user, strtitle, strtext, bitmapSmall, time);
                } else {
                    if (r.getPhotoAsString() != null) {
                        request= new Request(r.getId(),user, strtitle, strtext, r.getPhoto(), time);
                    } else{
                    request=new Request(r.getId(),user, strtitle, strtext, time);}
                }
            new UpdateRequestTask().execute(request);
            Toast toast=Toast.makeText(this, "Запрос обновлен", Toast.LENGTH_LONG);
            toast.show();
            Intent intent=new Intent(this, EditActivity.class);
            startActivity(intent);}
        } else if (v.getId()==back.getId()){
            finish();
        } else if (v.getId()==R.id.btnDeletePhoto){
            image.setImageResource(R.drawable.addphoto);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMG && resultCode==RESULT_OK){
            imageUri=data.getData(); //получить uri
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
                photoBitmap = bitmap;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private class UpdateRequestTask extends AsyncTask<Request, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(EditRequestActivity.this);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Request... params) {
            try {
                dBhelper.updateRequest(params[0]);
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
