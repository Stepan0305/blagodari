package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.blagodari.Models.DBhelper;

public class LoginActivity extends AppCompatActivity {
    TextView email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.inputName2);
        password = findViewById(R.id.inputPassword2);
    }

    public void tap2(View v) {
        if (v.getId() == R.id.wannaCreate) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.btnSignIn) {
            DBhelper db = new DBhelper(this);
            boolean b = db.checkIfUserExists(email.getText().toString(), password.getText().toString()+"");
            if (b) {
                db.setCurrentUser(db.getUserByEmailAndPassword(email.getText().toString(), password.getText().toString()));
                Intent intent = new Intent(this, MainScreen.class);
                startActivity(intent);

            } else {
                Toast toast = Toast.makeText(this, "Email или пароль введен неверно", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
