package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Database.DataSource.CartRepository;
import com.app.kutumb.Database.Local.CartDataSource;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Database.ModelDB.Cart;
import com.app.kutumb.Model.AddOnList;
import com.app.kutumb.Model.MenuItems;
import com.app.kutumb.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static com.app.kutumb.Activity.MainActivity.txtItemTotal;
import static com.app.kutumb.Activity.MainActivity.txtPay;
import static com.app.kutumb.Activity.MainActivity.txtTotalAmount;

public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AddOnList> loadList;
    private List<AddOnList> loadListFiltered;
    private AddOnAdapterListner listener;
    ProgressDialog progressDialog;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView txtName,txtPrice;
        RelativeLayout rlAddItem;

        MyViewHolder(View view) {
            super(view);
            imgItem=view.findViewById(R.id.img_item_most);
            rlAddItem=view.findViewById(R.id.rl_add_addons);
            imgItem=view.findViewById(R.id.img_add_on);
            txtPrice=view.findViewById(R.id.txt_price_item);
            txtName=view.findViewById(R.id.txt_name_addon);
            txtPrice=view.findViewById(R.id.txt_price_addon);
        }
    }
    public AddOnAdapter(Context context, List<AddOnList> contactList, AddOnAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = contactList;
        this.loadListFiltered = contactList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_on, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final AddOnList contact = loadListFiltered.get(position);
        progressDialog=new ProgressDialog(context);
        Common.cartDatabase = CartDatabase.getInstance(context);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
        holder.txtPrice.setText("₹"+contact.getPrice());
        holder.txtName.setText(contact.getAddon_name());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.logo);
        Glide.with(context)
                .load(contact.getImage_url())
                .apply(options)
                .into(holder.imgItem);
        holder.rlAddItem.setOnClickListener(view -> {
            if (Common.cartRepository.checkCart(contact.getAddon_name())!=null
                    &&Common.cartRepository.checkCart(contact.getAddon_name()).equals(contact.getAddon_name())){
                Toast.makeText(context, "Allready in Cart", Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                try {
                    Cart cartItem = new Cart();
                    cartItem.item_id = contact.getAddon_name();
                    cartItem.menu_id = "";
                    cartItem.item_name = contact.getAddon_name();
                    cartItem.item_cost = contact.getPrice();
                    cartItem.item_type_id = "";
                    cartItem.item_image_name = contact.getImage_url();
                    cartItem.item_description = contact.getDescription();
                    cartItem.status = contact.getStatus();
                    cartItem.is_most_selling_item = "";
                    cartItem.product_id = "";
                    cartItem.quantity = "1";

                    Common.cartRepository.insertToCart(cartItem);
                    MainActivity.getCartData();

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        progressDialog.dismiss();
                        txtItemTotal.setText("₹"+Common.cartRepository.getCost());
                        txtPay.setText("₹"+Float.valueOf(Common.cartRepository.getCost()));
                        txtTotalAmount.setText(txtPay.getText().toString());
                    },3000);
                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("ERROR DATABASE", "ERROR--->" + ex.getMessage());
                }
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
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
                    List<AddOnList> filteredList = new ArrayList<>();
                    for (AddOnList row : loadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAddon_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getPrice().toLowerCase().contains(charString.toLowerCase())
                                ||row.getStatus().toLowerCase().contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<AddOnList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AddOnAdapterListner {
        void onAddOnAdapterSelcted(MenuItems menuItems);
    }
}
