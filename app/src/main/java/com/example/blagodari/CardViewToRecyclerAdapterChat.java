package com.example.blagodari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Message;
import com.example.blagodari.Models.User;

import java.util.List;

public class CardViewToRecyclerAdapterChat extends RecyclerView.Adapter<CardViewToRecyclerAdapterChat.ViewHolder> {
    private LayoutInflater inflater;
    private List<Message> messages;
    private Context context;
    private DBhelper dBhelper;
    public static final int LEFT_SIDE = 0;
    public static final int RIGHT_SIDE = 1;

    CardViewToRecyclerAdapterChat(Context context, List<Message> messages) {
        this.inflater = LayoutInflater.from(context);
        this.messages = messages;
        this.context = context;
        this.dBhelper = new DBhelper(context);
    }
    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getUserFrom().getId()==dBhelper.getCurrentUser().getId()) {
            return RIGHT_SIDE;
        } else {
            return LEFT_SIDE;
        }
    }
    @NonNull
    @Override
    public CardViewToRecyclerAdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_SIDE) {
            view = inflater.inflate(R.layout.message_right, parent, false);
        } else {
            view = inflater.inflate(R.layout.message_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewToRecyclerAdapterChat.ViewHolder holder, int position) {
        Message message=messages.get(position);
        holder.text.setText(message.getText());
        holder.time.setText(Pomogator.convertSecondsToStringDateTime(message.getTime_created()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.messageText);
            time=itemView.findViewById(R.id.MessageTime);
        }
    }

}
