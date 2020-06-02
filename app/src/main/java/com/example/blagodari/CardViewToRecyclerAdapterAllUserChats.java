package com.example.blagodari;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blagodari.Models.Chat;
import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.User;

import java.util.List;

public class CardViewToRecyclerAdapterAllUserChats extends RecyclerView.Adapter<CardViewToRecyclerAdapterAllUserChats.ViewHolder> {
    private LayoutInflater inflater;
    private List<Chat> data;
    Context context;
    DBhelper dBhelper;

    CardViewToRecyclerAdapterAllUserChats(Context context, List<Chat> data){
        this.inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cardview_all_user_chats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final Chat chat=data.get(position);
       User userTo;
       if (chat.getUser1().getId()==dBhelper.getCurrentUser().getId()){
           userTo=chat.getUser2();
       } else {
           userTo=chat.getUser1();
       }
       holder.name.setText(userTo.getFirstName()+" "+userTo.getSurname());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(context, ChatActivity.class);
               i.putExtra("chat", chat.getId());
               context.startActivity(i);
           }
       });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView name;
       View itemView;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.txtNameOnAllUserChats);
            this.itemView=itemView;
            dBhelper=new DBhelper(context);
        }
    }
}


