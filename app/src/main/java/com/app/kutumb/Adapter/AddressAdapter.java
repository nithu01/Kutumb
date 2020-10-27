package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.AddressActivity;
import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Activity.SplashActivity;
import com.app.kutumb.Model.AddressModel;
import com.app.kutumb.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder>
        implements Filterable {


    private Context context;
    private List<AddressModel> loadList;
    private List<AddressModel> loadListFiltered;
    private AddressAdapterListener listener;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPincode, txtAddressLineOne, txtAddressLineTwo,txtCity,txtState;
        Button btnSelect;
        ImageView btnDelete;
        CardView cardView;
        MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_address_name);
            txtPincode = view.findViewById(R.id.txt_pincode);
            txtAddressLineOne = view.findViewById(R.id.txt_address_line_one);
            txtAddressLineTwo = view.findViewById(R.id.txt_address_line_two);
            txtCity = view.findViewById(R.id.txt_city_address);
            txtState = view.findViewById(R.id.txt_state_address);
            btnSelect=view.findViewById(R.id.btn_select_deliver_address);
            btnDelete=view.findViewById(R.id.btn_delete_deliver_address);
            cardView=view.findViewById(R.id.cardview_booking);
        }
    }


    public AddressAdapter(Context context, List<AddressModel> rechargeList) {
        this.context = context;

        this.loadList = rechargeList;
        this.loadListFiltered = rechargeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final AddressModel contact = loadListFiltered.get(position);
        holder.txtName.setVisibility(View.GONE);
        holder.txtPincode.setText("Pincode: "+contact.getPincode());
        holder.txtAddressLineOne.setText(contact.getHouse_no()+", "+contact.getStreet()+", "+contact.getCity());
        holder.txtAddressLineTwo.setText("Near By: "+contact.getLandmark());
        holder.txtState.setText(contact.getLocality());
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.savePreferences("city",contact.getCity());
                SplashActivity.savePreferences("pincode",contact.getPincode());
                SplashActivity.savePreferences("house",contact.getHouse_no());
                SplashActivity.savePreferences("street",contact.getStreet());
                context.startActivity(new Intent(context, MainActivity.class).putExtra("address","address"));
            }
        });

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
                    List<AddressModel> filteredList = new ArrayList<>();
                    for (AddressModel row : loadList) {

                        if (row.getLandmark().toLowerCase().contains(charString.toLowerCase())
                                ||row.getHouse_no().toLowerCase().contains(charString.toLowerCase())
                                ||row.getPincode().toLowerCase().contains(charString.toLowerCase())
                                ||row.getStreet().toLowerCase().contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<AddressModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AddressAdapterListener {
        void onAddressAdapterSelected(AddressModel addressModel);
    }
}
