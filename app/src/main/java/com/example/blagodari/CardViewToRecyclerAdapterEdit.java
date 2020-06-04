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

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.Request;

import java.util.List;

public class CardViewToRecyclerAdapterEdit extends RecyclerView.Adapter<CardViewToRecyclerAdapterEdit.ViewHolder> {
    private LayoutInflater inflater;
    private List<Request> data;
    private Context context;

    CardViewToRecyclerAdapterEdit(Context context, List<Request> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewToRecyclerAdapterEdit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_on_edit_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewToRecyclerAdapterEdit.ViewHolder holder, final int position) {
        final Request request = data.get(position);
        holder.title.setText(request.getTitle());
        holder.text.setText(request.getText());
        //holder.photo.setImageResource(request.getPhoto_address());
        long time = request.getTime_created();
        String strTime = Pomogator.convertSecondsToString(time);
        holder.time_created.setText(strTime);
        holder.photo.setImageBitmap(request.getPhoto());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper dBhelper = new DBhelper(context);
                dBhelper.deleteRequest(request);
                data.remove(request);
                notifyItemRemoved(position);
                Toast toast = Toast.makeText(context, "Запрос удален", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper dBhelper = new DBhelper(context);
                Intent intent = new Intent(context, EditRequestActivity.class);
                intent.putExtra("id", request.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title, text, time_created;
        Button delete, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imgPhotoOnEditScreen);
            title = itemView.findViewById(R.id.txtTitleOnEditScreen);
            text = itemView.findViewById(R.id.txtTextOnEditScreen);
            time_created = itemView.findViewById(R.id.txtTimeOnEditScreen);
            delete = itemView.findViewById(R.id.btnDeleteOnEditScreen);
            edit = itemView.findViewById(R.id.btnEditOnEditScreen);
        }
    }
}