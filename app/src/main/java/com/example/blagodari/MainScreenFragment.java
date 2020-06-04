package com.example.blagodari;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;

import java.util.ArrayList;

public class MainScreenFragment extends Fragment {
    RecyclerView recyclerView;
    CardViewToRecyclerAdapterMain recyclerAdapter;
    ImageButton btnSearch;
    ArrayList<Request> items;

    public static Fragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.search_btn);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setPadding(0,10, 0, 0);
        searchView.setQueryHint("Поиск...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DBhelper dBhelper = new DBhelper(getActivity());
        View view = inflater.inflate(R.layout.fragment_on_main_screen, container, false);
        items = dBhelper.getAllRequests();
        recyclerView = view.findViewById(R.id.recyclerViewMainScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new CardViewToRecyclerAdapterMain(getActivity(), items);
        recyclerView.setAdapter(recyclerAdapter);
        //btnSearch=getActivity().findViewById(R.id.btnSearch);
        return view;
    }
}