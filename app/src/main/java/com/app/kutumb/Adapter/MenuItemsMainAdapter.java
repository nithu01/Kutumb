package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.MenuDetailsActivity;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Database.DataSource.CartRepository;
import com.app.kutumb.Database.Local.CartDataSource;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Database.ModelDB.Cart;
import com.app.kutumb.Model.MenuItems;
import com.app.kutumb.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsMainAdapter extends RecyclerView.Adapter<MenuItemsMainAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<MenuItems> loadList;
    private List<MenuItems> loadListFiltered;
    private MenuItemsMainAdapterListner listener;
    SessionManager session;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItems;
        TextView txtName,txtPrice,txtDescription;
        ProgressBar progressBar;
        Button btnAdd;

        MyViewHolder(View view) {
            super(view);
            imgItems=view.findViewById(R.id.img_items);
            txtName=view.findViewById(R.id.txt_name_item);
            txtPrice=view.findViewById(R.id.txt_price_item);
            txtDescription=view.findViewById(R.id.txt_description_item);
            progressBar=view.findViewById(R.id.progress);
            btnAdd=view.findViewById(R.id.btn_add_item);
        }
    }
    public MenuItemsMainAdapter(Context context, List<MenuItems> contactList, MenuItemsMainAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = contactList;
        this.loadListFiltered = contactList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_main, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MenuItems contact = loadListFiltered.get(position);
        session=new SessionManager(context);
        holder.txtName.setText(contact.getItem_name());
        holder.txtPrice.setText((Html.fromHtml("<font color='#000000'> " +  "Price (Qty:1) :" +
                "</font>"+"<b><font color='#E15616'>" + " ₹"+contact.getItem_cost() + "</font></b>")));
        holder.txtDescription.setText(contact.getItem_description());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_foodie)
                .error(R.drawable.logo);

        Glide.with(context)
                .load(contact.getItem_image_name())
                .apply(options)
                .into(holder.imgItems);
        Common.cartDatabase = CartDatabase.getInstance(context);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
        holder.btnAdd.setOnClickListener(v -> {
            if (session.isLoggedIn()){

                if (Common.cartRepository.checkCart(contact.getItem_id())!=null
                        &&Common.cartRepository.checkCart(contact.getItem_id()).equals(contact.getItem_id())){
                    Toast.makeText(context, "Already in Cart", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Cart cartItem = new Cart();
                        cartItem.item_id = contact.getItem_id();
                        cartItem.menu_id = contact.getMenu_id();
                        cartItem.item_name = contact.getItem_name();
                        cartItem.item_cost = contact.getItem_cost();
                        cartItem.item_type_id = contact.getItem_type_id();
                        cartItem.item_image_name = contact.getItem_image_name();
                        cartItem.item_description = contact.getItem_description();
                        cartItem.status = contact.getStatus();
                        cartItem.is_most_selling_item = contact.getIs_most_selling_item();
                        cartItem.product_id = contact.getProduct_id();
                        Common.cartRepository.insertToCart(cartItem);
                        Log.d("CART DATA", new Gson().toJson(cartItem)+"\n"+Common.cartRepository.countCartItems());
                        MenuDetailsActivity.txtCart.setText(String.valueOf(Common.cartRepository.countCartItems()));

                        Toast.makeText(context, "Save item to cart success", Toast.LENGTH_LONG).show();
                    }catch (Exception ex) {
                        Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
                        Log.e("ERROR DATABASE","ERROR--->"+ex.getMessage());
                    }
                }
            }else {
                Configuration.openPopupUpDownBack(context,R.style.Dialod_UpDown,"login","error",
                        "Please login/signup first to get order at your door","");
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
                    List<MenuItems> filteredList = new ArrayList<>();
                    for (MenuItems row : loadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getItem_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getItem_cost().contains(charSequence)||row.getIs_most_selling_item().contains(charSequence)) {
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
                loadListFiltered = (ArrayList<MenuItems>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MenuItemsMainAdapterListner {
        void MenuItemsMainAdapterSelcted(MenuItems menuItems);
    }
}
