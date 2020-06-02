package com.example.blagodari;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blagodari.Models.AddScreenCard;

import java.util.List;

public class CardViewToRecyclerAdapterAdd extends RecyclerView.Adapter<CardViewToRecyclerAdapterAdd.ViewHolder> {
    private LayoutInflater inflater;
    private List<AddScreenCard> cards;

    CardViewToRecyclerAdapterAdd(Context context, List<AddScreenCard> cards) {
        this.inflater = LayoutInflater.from(context);
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewToRecyclerAdapterAdd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cardview_add_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewToRecyclerAdapterAdd.ViewHolder holder, int position) {
        AddScreenCard addScreenCard=cards.get(position);
        holder.textView.setText(addScreenCard.getText());
        holder.img.setImageResource(addScreenCard.getPhoto());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView img;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //Добавление переменных из карточки
            textView=itemView.findViewById(R.id.txtOnCardAddScreen);
            img=itemView.findViewById(R.id.imgOnCardAddScreen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getText().toString().equals("Создать запрос")){
                        Intent intent=new Intent(itemView.getContext(), AddNewRequestActivity.class);
                        itemView.getContext().startActivity(intent);
                    }else if (textView.getText().toString().equals("Управление")){
                        Intent intent=new Intent(itemView.getContext(), EditActivity.class);
                        itemView.getContext().startActivity(intent);
                    }else if (textView.getText().toString().equals("Новость")){
                        Intent intent=new Intent(itemView.getContext(), AddNewsActivity.class);
                        itemView.getContext().startActivity(intent);
                    } else if (textView.getText().toString().equals("Мои чаты")){
                        Intent intent=new Intent(itemView.getContext(), AllUserChatsActivity.class);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
