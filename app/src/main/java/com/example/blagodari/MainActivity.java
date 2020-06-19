package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.User;

public class MainActivity extends AppCompatActivity {
    TextView name, surname, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        name = findViewById(R.id.inputName1);
        surname = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        password = findViewById(R.id.inputPassword1);

    }

    public void Tap1(View v) {
        if (v.getId() == R.id.alreadyHave) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.btnSignUp) {

            if (!Pomogator.checkInputSignUp(name.getText().toString(), surname.getText().toString(),
                    email.getText().toString(), password.getText().toString()))
            {
                Toast toast = Toast.makeText(this, "Данныe введены некорректно", Toast.LENGTH_LONG);
                toast.show();
            } else{
                long time = System.currentTimeMillis() / 1000;
                Intent intent = new Intent(this, AddAvatar.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("surname", surname.getText().toString());
                intent.putExtra("passwd",password.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("time", time);
                startActivity(intent);
            }
        }
    }
}
