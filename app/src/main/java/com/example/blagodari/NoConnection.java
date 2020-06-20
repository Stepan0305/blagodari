package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * Экран, который видет пользователь при отсутствии интернета.
 * */
public class NoConnection extends AppCompatActivity {
Button repeat, exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        exit=findViewById(R.id.exit);
        repeat=findViewById(R.id.repeat);
    }
    public void bom(View v){
        if (v.getId()==exit.getId()){
            System.exit(1);
        } else if (v.getId()==repeat.getId()){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }
}
