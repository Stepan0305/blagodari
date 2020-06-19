package com.example.blagodari;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.History;
import com.example.blagodari.Models.News;
import com.example.blagodari.Models.Request;

import java.util.List;

public class CardViewToRecyclerAdapterHistory extends RecyclerView.Adapter<CardViewToRecyclerAdapterHistory.ViewHolder> {
    private LayoutInflater inflater;
    private List<History> data;
    Context context;
    DBhelper dBhelper;
    ProgressDialog pd;
    CardViewToRecyclerAdapterHistory(Context context, List<History> data){
        this.inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cardview_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history=data.get(position);
        final News news;
        final Request request;
        if (history.getNews()!=null){
            news=history.getNews();
            holder.title.setText(news.getTitle());
            holder.text.setText(news.getText());
            holder.photo.setImageBitmap(news.getPhoto());
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, NewsOpenedActivity.class);
                    intent.putExtra("id", news.getId());
                    context.startActivity(intent);
                }
            });
        } else if (history.getRequest()!=null){
            request=history.getRequest();
            holder.title.setText(request.getTitle());
            holder.text.setText(request.getText());
            holder.photo.setImageBitmap(request.getPhoto());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, RequestOpenedActivity.class);
                    intent.putExtra("id", request.getId());
                    context.startActivity(intent);
                }
            });
        } else {
           holder.itemView.setVisibility(View.GONE);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView text, title;
        View itemView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            photo=itemView.findViewById(R.id.imgPhotoOnHistory);
            text=itemView.findViewById(R.id.txtTextOnHistory);
            title=itemView.findViewById(R.id.txtTitleOnHistoryScreen);
            this.itemView=itemView;
            cardView=itemView.findViewById(R.id.cardHistory);
            dBhelper=new DBhelper(context);
        }
    }
}
