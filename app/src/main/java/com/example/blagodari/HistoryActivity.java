package com.example.blagodari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ImageButton back;
    RecyclerView recyclerView;
    ArrayList<History>histories;
    DBhelper dBhelper;
    CardViewToRecyclerAdapterHistory adapterHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
        dBhelper=new DBhelper(this);
        back=findViewById(R.id.btnBackOnHistory);
        histories=dBhelper.getAllUserHistory();
        recyclerView=findViewById(R.id.recyclerViewHistoryScreen);
        adapterHistory=new CardViewToRecyclerAdapterHistory(this, histories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterHistory);
    }
    public void onHistory (View v){
        finish();
    }
}
