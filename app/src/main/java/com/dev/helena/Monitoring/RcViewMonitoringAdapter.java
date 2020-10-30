package com.dev.helena.Monitoring;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.helena.R;
import com.dev.helena.Monitoring.ListMonitoringActivity;
import com.dev.helena.Monitoring.Monitoring;

import java.util.List;

public class RcViewMonitoringAdapter extends RecyclerView.Adapter<RcViewMonitoringAdapter.ViewHolder> {
    Context context;
    List<Monitoring> cp;
    private OnItemClickListener mListener;

    public RcViewMonitoringAdapter(Context context, List<Monitoring> TempList) {
        this.cp = TempList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_monitoring, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Set dei dati interessati
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Monitoring monitoring = cp.get(position);
        holder.NameMonitoringTextView.setText(monitoring.getNameMonitoring());
        holder.DateMonitoringTextView.setText(monitoring.getDate());
    }

    @Override
    public int getItemCount() {
        return cp.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView NameMonitoringTextView, DateMonitoringTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            NameMonitoringTextView = (TextView) itemView.findViewById(R.id.ShowMonitoringName);
            DateMonitoringTextView = (TextView) itemView.findViewById(R.id.ShowMonitoringDate);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        // Richiamo il metodo per l'eliminazione dell'item
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onDeleteItemClick(position);
                    notifyDataSetChanged();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        // Creazione del menu onPressed
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Seleziona opzione");
            MenuItem deleteItem = contextMenu.add(Menu.NONE, 1, 1, "Rimuovi");
            deleteItem.setOnMenuItemClickListener(this);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteItemClick(int position);
    }
    public void setOnItemClickListener(ListMonitoringActivity listener) {
        mListener = (OnItemClickListener) listener;
    }
}
