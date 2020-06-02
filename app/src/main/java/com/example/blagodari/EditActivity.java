package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
ImageButton back;
RecyclerView recyclerView;
CardViewToRecyclerAdapterEdit adapterEdit;
ArrayList<Request> requests;
DBhelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dBhelper=new DBhelper(this);
        back=findViewById(R.id.btnBackOnEdit);
        requests=dBhelper.getAllUserRequests(dBhelper.getCurrentUser());
        adapterEdit=new CardViewToRecyclerAdapterEdit(this, requests);
        recyclerView=findViewById(R.id.recyclerViewEditScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterEdit);
    }
    public void onClick(View v){
        Intent intent=new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}
