package com.example.blagodari;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.blagodari.Models.Request;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CardViewToRecyclerAdapterMain extends RecyclerView.Adapter<CardViewToRecyclerAdapterMain.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Request> data;
    private List<Request> dataFull;
    int currentUserId;
    Context context;
    DBhelper dBhelper;
    ProgressDialog pd;
    CardViewToRecyclerAdapterMain(Context context, List<Request> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        dataFull = new ArrayList<>(data);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_main_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        dBhelper = new DBhelper(context);
        currentUserId = dBhelper.getCurrentUser().getId();
        final Request request = data.get(position);
        holder.title.setText(request.getTitle());
        holder.name.setText(request.getUser().getFirstName() + " " + request.getUser().getSurname());
        holder.text.setText(request.getText());
        if (request.getPhoto() != null) {
            holder.photo.setImageBitmap(request.getPhoto());
        }
        if (request.getUser().getAvatar() != null) {
            holder.avatar.setImageBitmap(request.getUser().getAvatar());
        }
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(), AnotherUserAccount.class);
                i.putExtra("userId", request.getUser().getId());
                v.getContext().startActivity(i);
            }
        });
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RequestOpenedActivity.class);
                int id = request.getId();
                new AddToHistoryTask().execute(new History(dBhelper.getCurrentUser(), request));
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Уверен?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteRequestTask().execute(request);
                        data.remove(request);
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, text;
        ImageView photo, avatar;
        Button open, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Добавление переменных из карточки
            name = itemView.findViewById(R.id.txtUserNameOnMainScreenCardView);
            title = itemView.findViewById(R.id.txtTitleOnMainScreenCardView);
            text = itemView.findViewById(R.id.txtLongTextOnMainScreenCardView);
            photo = itemView.findViewById(R.id.imgPhotoOnMainScreenCardView);
            open = itemView.findViewById(R.id.btnOpenOnMainScreenCardView);
            delete = itemView.findViewById(R.id.btnForAdminMain);
            avatar = itemView.findViewById(R.id.imgAvatarOnCard);
            dBhelper = new DBhelper(context);
            currentUserId = dBhelper.getCurrentUser().getId();
            if (currentUserId != DBhelper.ADMIN_ID) {
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
            List<Request> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Request r : dataFull) {
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
            data.clear();
            data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    private class AddToHistoryTask extends AsyncTask<History, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(History... params) {
            try {
                dBhelper.addToHistory(params[0]);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if ((pd!= null) && pd.isShowing()) {
                    pd.dismiss();
                }
            }  catch (final Exception e) {
            } finally {
                pd = null;
            }
        }
    }
    private class DeleteRequestTask extends AsyncTask<Request, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Request... params) {
            try {
                dBhelper.deleteRequest(params[0]);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if ((pd!= null) && pd.isShowing()) {
                    pd.dismiss();
                }
            }  catch (final Exception e) {
            } finally {
                pd = null;
            }
        }
    }
   /* private class DeleteFromHistoryTask extends AsyncTask<History, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Пожалуйста, подождите");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(History... params) {
            try {
                dBhelper.deleteFromHistory(params[0]);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if ((pd!= null) && pd.isShowing()) {
                    pd.dismiss();
                }
            }  catch (final Exception e) {
            } finally {
                pd = null;
            }
        }
    }*/
}
