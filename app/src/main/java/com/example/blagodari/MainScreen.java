package com.example.blagodari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.History;
import com.example.blagodari.Models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
/**
 * Самый главный экран.
 * Состоит из 4 фрагментов, кастомного toolbar, левого меню, нижнего меню
 * Тут происходит больше всего багов ;(
 * */
public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //создание переменных
    //ImageButton btnSearch;
    ImageButton btnLeftSide;
    TextView txtTitle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    View header;
    TextView nameOnHeader;
    ImageView avatarOnHeader;
    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
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
        dBhelper = new DBhelper(this);
        User user = dBhelper.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        Toolbar mActionBar = findViewById(R.id.myActionBar);
        setSupportActionBar(mActionBar);
        mActionBar.getMenu();
        //инициализация переменных
        txtTitle = mActionBar.findViewById(R.id.txtActionbarTitle);
        btnLeftSide = mActionBar.findViewById(R.id.btnLeftSideMenu);
        // btnSearch = mActionBar.findViewById(R.id.btnSearch);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawer();
        txtTitle.setText("Главная страница");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.Bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(MainScreenFragment.newInstance());    //загрузка первого фрагмента
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        User user = dBhelper.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            header = navigationView.getHeaderView(0);
            nameOnHeader = (TextView) header.findViewById(R.id.txtNameSurnameOnHeader);
            nameOnHeader.setText(dBhelper.getCurrentUser().getFirstName() + " " + dBhelper.getCurrentUser().getSurname() + "");
            avatarOnHeader=header.findViewById(R.id.imgAvatarOnHeader);
            Bitmap ava=dBhelper.getCurrentUser().getAvatar();
                if (ava!=null) {
                avatarOnHeader.setImageBitmap(ava);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.btnMain:
                    loadFragment(MainScreenFragment.newInstance());
                    txtTitle.setText("Главная страница");
                    return true;
                case R.id.btnNews:
                    loadFragment(NewsScreenFragment.newInstance());
                    txtTitle.setText("Новости");
                    return true;
                case R.id.btnAdd:
                    loadFragment(AddScreenFragment.newInstance());
                    txtTitle.setText("Опции");
                    return true;
                case R.id.btnProfile:
                    loadFragment(UserProfileFragment.newInstance());
                    txtTitle.setText("Мой профиль");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.ContainerForCardsOnMainScreen, fragment);
        ft.commit();
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view);
        btnLeftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnHistory:
                Intent i = new Intent(this, HistoryActivity.class);
                startActivity(i);
                break;
            case R.id.btnShare:
                try {
                    Intent ii = new Intent(Intent.ACTION_SEND);
                    ii.setType("text/plain");
                    ii.putExtra(Intent.EXTRA_SUBJECT, "БлагоДари");
                    String sAux = "\n БлагоДари - дари людям добро! Портал для помощи людям.\n\n";
                    sAux = sAux + "Скачать Android-приложение: \n https://blagodari.herokuapp.com/download.html \n\n" +
                            "Веб-сайт: \n https://blagodari.herokuapp.com/ ";
                    ii.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(ii, "Поделиться с помощью..."));
                } catch (Exception e) {
                }
                break;
            case R.id.btnAbout:
                Intent intentt=new Intent(this, AboutUs.class);
                startActivity(intentt);
                break;
            case R.id.btnExit:
                SharedPreferences pref = getSharedPreferences(DBhelper.LOGGED_USER_ID, 0);
                pref.edit().clear();
                pref.edit().commit();
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnSite:
                Intent oy=new Intent(Intent.ACTION_VIEW, Uri.parse("https://blagodari.herokuapp.com/"));
                startActivity(oy);
        }
        return false;
    }
}
