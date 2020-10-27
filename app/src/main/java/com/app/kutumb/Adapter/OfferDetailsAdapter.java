package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Model.OfferItemDetail;
import com.app.kutumb.R;

import java.util.ArrayList;
import java.util.List;

public class OfferDetailsAdapter extends RecyclerView.Adapter<OfferDetailsAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<OfferItemDetail> loadList;
    private List<OfferItemDetail> loadListFiltered;
    private OfferDetailsAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOfferName,txtOfferQty;
        MyViewHolder(View view) {
            super(view);
            txtOfferName=view.findViewById(R.id.txt_name_offer);
            txtOfferQty=view.findViewById(R.id.txt_qty_offer);
        }
    }


    OfferDetailsAdapter(Context context, List<OfferItemDetail> rechargeList, OfferDetailsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = rechargeList;
        this.loadListFiltered = rechargeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OfferItemDetail contact = loadListFiltered.get(position);
        holder.txtOfferName.setText(contact.getItem_name());
        holder.txtOfferQty.setText("Qty: "+contact.getQuantity());
    }

    @Override
    public int getItemCount() {
        return loadListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    loadListFiltered = loadList;
                } else {
                    List<OfferItemDetail> filteredList = new ArrayList<>();
                    for (OfferItemDetail row : loadList) {

                        if (row.getMenu_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getItem_name().contains(charSequence)||row.getQuantity().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    loadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = loadListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                loadListFiltered = (ArrayList<OfferItemDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OfferDetailsAdapterListener {
        void onOfferDetailsAdapterSelected(OfferItemDetail offerItemDetail);
    }
}
