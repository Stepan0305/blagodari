package com.example.blagodari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blagodari.Models.AddScreenCard;
import com.example.blagodari.Models.DBhelper;

import java.util.ArrayList;

/**
 * Фрагмент, на котором отображается меню опций.
 */
public class AddScreenFragment extends Fragment {
    RecyclerView recyclerView;
    CardViewToRecyclerAdapterAdd recyclerAdapter;
    ArrayList<AddScreenCard> cards;
    DBhelper dBhelper;
    public static Fragment newInstance(){
        return new AddScreenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_on_add_screen, container, false);
        dBhelper=new DBhelper(getContext());
        cards=new ArrayList<>();
        cards.add(new AddScreenCard(R.drawable.imgneedhelp, "Создать запрос"));
        cards.add(new AddScreenCard(R.drawable.editrequest, "Управление"));
        cards.add(new AddScreenCard(R.drawable.imgchats, "Мои чаты"));
        if (dBhelper.getCurrentUser().getId()==DBhelper.ADMIN_ID){
            cards.add(new AddScreenCard(R.drawable.newnews, "Новость"));
        }
        recyclerView=view.findViewById(R.id.recyclerViewAddScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter=new CardViewToRecyclerAdapterAdd(getActivity(),cards);
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }
}
