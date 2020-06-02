package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

            if (name.getText().toString().length() > 128 || name.getText().toString().length() < 1 ||
                    surname.getText().toString().length() > 128 || surname.getText().toString().length() < 1 ||
                   // !(email.getText().toString().matches("[a-zA-Z]")) || email.getText().toString().matches("[йцукенгшщзхъэждлорпавыфячсмитьбюЮБЬТИМСЧЯФЫВАПРОЛДЖЭЪХЗЩШГНЕКУЦЙ]")||
            email.getText().toString().length() > 128 || email.getText().toString().length() < 1 ||
                    password.getText().toString().length() > 50 || password.getText().toString().length() < 6)
            {
                Toast toast = Toast.makeText(this, "Данныe введены некорректно", Toast.LENGTH_LONG);
                toast.show();
            } else{
                DBhelper db = new DBhelper(this);
                long time = System.currentTimeMillis() / 1000;
                User user = new User(name.getText().toString(), surname.getText().toString()+"", password.getText().toString()
                        , email.getText().toString(), time);
                db.addUser(user);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
