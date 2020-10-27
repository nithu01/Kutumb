package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.MainActivity;
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

import java.util.ArrayList;
import java.util.List;

public class MostSellingItemsAdapter extends RecyclerView.Adapter<MostSellingItemsAdapter.MyViewHolder>
        implements Filterable {

    private static final String TAG = MostSellingItemsAdapter.class.getSimpleName();
    private Context context;
    private List<MenuItems> loadList;
    private List<MenuItems> loadListFiltered;
    private MostSellingItemsAdapterListner listener;
    private static String menu;
    private List<Cart> carts;
    private String array;
    SessionManager session;

    static void getMenuName(String menuName) {
        menu=menuName;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem,imgItemType;
        TextView txtName,txtPrice,txtQuantity;
        ProgressBar progressBar;
        Button btnMinus,btnPlus,btnAdd;
        CardView cardView;
        LinearLayout lnCart;

        MyViewHolder(View view) {
            super(view);
            imgItem=view.findViewById(R.id.img_item_most);
            imgItemType=view.findViewById(R.id.img_item_type);
            txtName=view.findViewById(R.id.txt_name_item);
            txtPrice=view.findViewById(R.id.txt_price_item);
            progressBar=view.findViewById(R.id.progress);
            cardView=view.findViewById(R.id.cardview_menu);
            lnCart=view.findViewById(R.id.ln_cart_plusminus);
            btnMinus=view.findViewById(R.id.btn_minus);
            txtQuantity=view.findViewById(R.id.txt_quantity);
            btnPlus=view.findViewById(R.id.btn_plus);
            btnAdd=view.findViewById(R.id.btn_add_most);

            //1=veg,3=other,41=nonveg
        }
    }
    public MostSellingItemsAdapter(Context context, List<MenuItems> contactList, MostSellingItemsAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = contactList;
        this.loadListFiltered = contactList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_most_selling, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MenuItems contact = loadListFiltered.get(position);
        session=new SessionManager(context);
        holder.txtName.setText(contact.getItem_name());
        holder.txtPrice.setText(" ₹ " + contact.getItem_cost());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_foodie)
                .error(R.drawable.logo);
        // holder.txtDescription.setText(contact.getItem_description());
        if (contact.getItem_type_id().equalsIgnoreCase("1")) {
            holder.imgItemType.setImageResource(R.drawable.veg);
        } else if (contact.getItem_type_id().equalsIgnoreCase("41")) {
            holder.imgItemType.setImageResource(R.drawable.nonveg);
        } else {
            holder.imgItemType.setImageResource(R.drawable.veg);
        }
        Common.cartDatabase = CartDatabase.getInstance(context);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
        Common.cartRepository.emptyCart();
        Glide.with(context)
                .load(contact.getItem_image_name())
                .apply(options)
                .into(holder.imgItem);

        holder.btnMinus.setOnClickListener(v -> {
            int count= Integer.parseInt(holder.txtQuantity.getText().toString());
            if (count>0)
                count--;
            holder.txtQuantity.setText(String.valueOf(count));
            String itemCost= String.valueOf(Integer.valueOf(holder.txtQuantity.getText().toString())*Float.valueOf(contact.getItem_cost()));
            Common.cartRepository.update(holder.txtQuantity.getText().toString(),itemCost, contact.getItem_id());

            String sum=Common.cartRepository.getCost();
            String item= Common.cartRepository.getQuantity();
//            Toast.makeText(context, "Item\n"+holder.txtQuantity.getText().toString()+"\n"+contact.getItem_cost()+
//                    "\n"+itemCost+"\n"+contact.getItem_id()+"\nSum="+sum+"\nItem="+item, Toast.LENGTH_SHORT).show();
            if (holder.txtQuantity.getText().toString().equalsIgnoreCase("0")){
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.lnCart.setVisibility(View.GONE);
                Common.cartRepository.deleteByitem_id(contact.getItem_id());
            }else {
                holder.btnAdd.setVisibility(View.GONE);
                holder.lnCart.setVisibility(View.VISIBLE);
            }
            if (!sum.equals("0") && !item.equals("0")){
                MainActivity.txtItemCount.setText(item+" Item");
                MainActivity.txtItemsPrice.setText("₹"+sum);
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                MainActivity.rlCart.setVisibility(View.VISIBLE);
            }else {
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                MainActivity.rlCart.setVisibility(View.GONE);
            }
        });
        holder.btnPlus.setOnClickListener(v -> {
            int count= Integer.parseInt(holder.txtQuantity.getText().toString());
            if (count<4)
                count++;
            holder.txtQuantity.setText(String.valueOf(count));
            // cartItem.quantity=holder.txtQuantity.getText().toString();
            String itemCost= String.valueOf(Integer.valueOf(holder.txtQuantity.getText().toString())*Float.valueOf(contact.getItem_cost()));
            Common.cartRepository.update(holder.txtQuantity.getText().toString(),itemCost, contact.getItem_id());
            String sum = Common.cartRepository.getCost();
            String item = Common.cartRepository.getQuantity();
            Toast.makeText(context, ""+Common.cartRepository.getQuantity(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "Item\n"+holder.txtQuantity.getText().toString()+"\n"+contact.getItem_cost()+
//                    "\n"+itemCost+"\n"+contact.getItem_id()+"\nSum="+sum+"\nItem="+item, Toast.LENGTH_SHORT).show();
            if (holder.txtQuantity.getText().toString().equalsIgnoreCase("0")){
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.lnCart.setVisibility(View.GONE);
            }else {
                holder.btnAdd.setVisibility(View.GONE);
                holder.lnCart.setVisibility(View.VISIBLE);
            }
            if (!sum.equals("0") && !item.equals("0")){
                MainActivity.txtItemCount.setText(item+" Item");
                MainActivity.txtItemsPrice.setText("₹"+sum);
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                MainActivity.rlCart.setVisibility(View.VISIBLE);
            }else {
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                MainActivity.rlCart.setVisibility(View.GONE);
            }

        });
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
                        cartItem.quantity = "1";
                        holder.txtQuantity.setText("1");
                        //Add to DB
                        Common.cartRepository.insertToCart(cartItem);
                        holder.lnCart.setVisibility(View.VISIBLE);
                        holder.btnAdd.setVisibility(View.GONE);
                        String sum = Common.cartRepository.getCost();
                        String item = Common.cartRepository.getQuantity();
                        if (!sum.equals("0") && !item.equals("0")) {
                            MainActivity.txtItemCount.setText(item + " Item");
                            MainActivity.txtItemsPrice.setText("₹" + sum);
                            MainActivity.bottomNavigationView.setVisibility(View.GONE);
                            MainActivity.rlCart.setVisibility(View.VISIBLE);
                        } else {
                            MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                            MainActivity.rlCart.setVisibility(View.GONE);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR DATABASE", "ERROR--->" + ex.getMessage());
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

    public interface MostSellingItemsAdapterListner {
        void onMenuItemsAdapterSelcted(MenuItems menuItems);
    }
}
