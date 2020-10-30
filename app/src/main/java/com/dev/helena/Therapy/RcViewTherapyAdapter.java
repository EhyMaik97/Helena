package com.dev.helena.Therapy;

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

import java.util.List;

public class RcViewTherapyAdapter extends RecyclerView.Adapter<RcViewTherapyAdapter.ViewHolder> {
    Context context;
    List<Therapy> cp;
    private OnItemClickListener mListener;

    public RcViewTherapyAdapter(Context context, List<Therapy> TempList) {
        this.cp = TempList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_therapy, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Set dei dati interessati
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Therapy coupon = cp.get(position);
        holder.NameCpTextView.setText(coupon.getName());
        holder.CodeCpTextView.setText(coupon.getDrugName());
        holder.descCpTextView.setText(coupon.getEndTime());
        holder.PercentDiscountTextView.setText(String.valueOf(coupon.getDosage()));
    }

    @Override
    public int getItemCount() {
        return cp.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView NameCpTextView, PercentDiscountTextView, CodeCpTextView, descCpTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            NameCpTextView = (TextView) itemView.findViewById(R.id.ShowCpName);
            CodeCpTextView = (TextView) itemView.findViewById(R.id.ShowCpCode);
            descCpTextView = (TextView) itemView.findViewById(R.id.ShowDescCp);
            PercentDiscountTextView = (TextView) itemView.findViewById(R.id.ShowPercentCoupon);
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
            contextMenu.setHeaderTitle("Seleziona opazione");
            MenuItem deleteItem = contextMenu.add(Menu.NONE, 1, 1, "Rimuovi");
            deleteItem.setOnMenuItemClickListener(this);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteItemClick(int position);
    }
    public void setOnItemClickListener(ListTherapy listener) {
        mListener = (OnItemClickListener) listener;
    }
}
