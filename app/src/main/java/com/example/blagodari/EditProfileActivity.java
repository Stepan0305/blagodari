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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.User;

import java.io.IOException;
/**
 * Экран для редактирования профиля пользователем.
 * */
public class EditProfileActivity extends AppCompatActivity {
    ImageView avatar;
    TextView name, surname, email;
    Button save;
    User user;
    ProgressDialog pd;
    DBhelper dBhelper;
    private static final int PICK_IMG = 1;
    Uri imageUri;
    Bitmap photoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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
        avatar = findViewById(R.id.imgAvatarOnEditUser);
        name = findViewById(R.id.txtNameOnEditUser);
        surname = findViewById(R.id.txtSurnameOnEditUser);
        email = findViewById(R.id.txtEmailOnEditUser);
        save = findViewById(R.id.btnSaveOnEditUser);
        dBhelper = new DBhelper(this);
        user = dBhelper.getCurrentUser();
        if (user.getAvatar() != null) {
            avatar.setImageBitmap(user.getAvatar());
        }
        name.setText(user.getFirstName());
        surname.setText(user.getSurname());
        email.setText(user.getEmail());
    }

    public void onEditProfile(View v) {
        if (v.getId() == save.getId()) {
            if (Pomogator.checkInputSignUp(name.getText().toString(), surname.getText().toString(), email.getText().toString(),
                    user.getPassword())) {

                if (photoBitmap == null) {
                    avatar.setImageDrawable(null);
                }
                User u;
                String strName = name.getText().toString();
                String strSurname = surname.getText().toString();
                String strEmail = email.getText().toString();
                if (avatar.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                    Bitmap bitmapSmall;
                    if (bitmap.getByteCount() > 200000) {
                        bitmapSmall = User.resizeBitmap(bitmap);
                    } else {
                        bitmapSmall = bitmap;
                    }
                    u = new User(user.getId(), strName, strSurname,
                            user.getPassword(), strEmail, user.getDate_created(), bitmapSmall);
                } else {
                    u = new User(user.getId(), strName, strSurname,
                            user.getPassword(), strEmail, user.getDate_created());
                }
                new UpdateUserTask().execute(u);
                Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Данные введены некорректно", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.btnBackOnEditUser) {
            finish();
        } else if (v.getId() == avatar.getId()) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
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

    private class UpdateUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(EditProfileActivity.this);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(User... params) {
            try {
                dBhelper.updateUserProfile(params[0]);
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
