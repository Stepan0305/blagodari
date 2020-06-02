package com.example.blagodari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.History;
import com.example.blagodari.Models.News;

import java.util.ArrayList;
import java.util.List;

public class CardViewToRecyclerAdapterNews extends RecyclerView.Adapter<CardViewToRecyclerAdapterNews.ViewHolder> implements Filterable {
    LayoutInflater inflater;
    List<News> news;
    List<News> newsFull;
    Context context;
    DBhelper dBhelper;

    CardViewToRecyclerAdapterNews(Context context, List<News> news) {
        this.inflater = LayoutInflater.from(context);
        this.news = news;
        newsFull = new ArrayList<>(news);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cardview_news_screen, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        dBhelper = new DBhelper(context);
        final News n = news.get(position);
        holder.title.setText(n.getTitle());
        holder.text.setText(n.getText());
        // holder.photo.setImageResource(R.drawable.header1);
        holder.photo.setImageBitmap(n.getPhoto());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Уверен?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dBhelper.deleteNews(n);
                        news.remove(n);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = n.getId();
                dBhelper.addToHistory(new History(dBhelper.getCurrentUser(), n));
                Intent i = new Intent(context, NewsOpenedActivity.class);
                i.putExtra("id", id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView text, title;
        Button open, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imgOnCardNewsScreen);
            text = itemView.findViewById(R.id.txtTextOnCardNewsScreen);
            title = itemView.findViewById(R.id.txtTitleOnCardNewsScreen);
            open = itemView.findViewById(R.id.btnOpenOnCardNewsScreen);
            delete = itemView.findViewById(R.id.btnDeleteOnCardNewsScreen);
            dBhelper = new DBhelper(context);
            if (dBhelper.getCurrentUser().getId() != DBhelper.ADMIN_ID) {
                delete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<News> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (News r : newsFull) {
                    if (r.getTitle().toLowerCase().contains(filterPattern) || r.getText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(r);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            news.clear();
            news.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

