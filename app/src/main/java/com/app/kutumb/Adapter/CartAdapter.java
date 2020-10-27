package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Database.ModelDB.Cart;
import com.app.kutumb.R;

import java.util.List;

import static com.app.kutumb.Activity.MainActivity.mostSellingloadListAdapter;
import static com.app.kutumb.Activity.MainActivity.requestListAdapter;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {
    private Context mCtx;
    private List<Cart> carts;
    private ProgressDialog progressDialog;

    public CartAdapter(Context mCtx, List<Cart> taskList) {
        this.mCtx = mCtx;
        this.carts = taskList;
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_layout_cart, parent, false);
        return new CartAdapterViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        Cart t = carts.get(position);
        progressDialog=new ProgressDialog(mCtx);
        holder.txtCartItemName.setText(t.getItem_name()+" ");
        holder.txtQuantity.setText(t.quantity);

        if (t.getItem_type_id().equalsIgnoreCase("1")){
            holder.imgVegNonVeg.setImageResource(R.drawable.veg);
        }else if (t.getItem_type_id().equalsIgnoreCase("41")){
            holder.imgVegNonVeg.setImageResource(R.drawable.nonveg);
        }else {
            holder.imgVegNonVeg.setImageResource(R.drawable.veg);
        }

        holder.txtPrice.setText("₹"+t.getItem_cost());

        holder.btnMinus.setOnClickListener(v -> {
           /* progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);*/
            int count= Integer.parseInt(holder.txtQuantity.getText().toString());
            if (count>0)
                count--;
            holder.txtQuantity.setText(String.valueOf(count));
            String itemCost= String.valueOf(Integer.valueOf(holder.txtQuantity.getText().toString())*Float.valueOf(t.getItem_cost()));
            holder.txtPrice.setText("₹"+itemCost);
           // MenuItemsAdapter.MyViewHolder.txtQuantity.setText(String.valueOf(count));
            if (holder.txtQuantity.getText().toString().equals("0")){
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                Common.cartRepository.deleteByitem_id(t.getItem_id());
                deleteItem(position,progressDialog);
                holder.lnPlusMinus.setVisibility(View.GONE);
            }else{
                Common.cartRepository.update(holder.txtQuantity.getText().toString(),itemCost, t.getItem_id());
            }
            String sum=Common.cartRepository.getCost();
            String item= Common.cartRepository.getQuantity();
            if (TextUtils.isEmpty(sum)||sum.equalsIgnoreCase("null")){
                progressDialog.show();
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                MainActivity.requestListData.setAdapter(requestListAdapter);
                MainActivity.recyclerViewMostSelling.setAdapter(mostSellingloadListAdapter);
                requestListAdapter.notifyDataSetChanged();
                mostSellingloadListAdapter.notifyDataSetChanged();
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                MainActivity.rlCart.setVisibility(View.GONE);
                /*Intent intent = new Intent(mCtx,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);*/
                //recyclerViewOffer,cartRequestList;
                MainActivity.closeCart(progressDialog);
            }else {
                MainActivity.txtItemCount.setText(item+" Item");
                MainActivity.txtItemsPrice.setText("₹"+sum);
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                MainActivity.rlCart.setVisibility(View.VISIBLE);
                MainActivity.txtItemTotal.setText("₹"+Common.cartRepository.getCost());
                MainActivity.txtPay.setText("₹"+Common.cartRepository.getCost());
                MainActivity.txtTotalAmount.setText("₹"+Common.cartRepository.getCost());
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int count= Integer.parseInt(holder.txtQuantity.getText().toString());
            if (count<4)
                count++;
            holder.txtQuantity.setText(String.valueOf(count));
            // cartItem.quantity=holder.txtQuantity.getText().toString();
            String itemCost= String.valueOf(Integer.valueOf(holder.txtQuantity.getText().toString())*Float.valueOf(t.getItem_cost()));
            holder.txtPrice.setText("₹"+itemCost);
            if (holder.txtQuantity.getText().toString().equals("0")){
                Common.cartRepository.deleteByitem_id(t.item_id);
                notifyDataSetChanged();
            }else{
                Common.cartRepository.update(holder.txtQuantity.getText().toString(),itemCost,t.getItem_id());
            }
            String sum=Common.cartRepository.getCost();
            String item=Common.cartRepository.getQuantity();
            if (!sum.equals("0") && !item.equals("0")){
                MainActivity.txtItemCount.setText(item+" Item");
                MainActivity.txtItemsPrice.setText("₹"+sum);
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                MainActivity.rlCart.setVisibility(View.VISIBLE);
                MainActivity.txtItemTotal.setText("₹"+Common.cartRepository.getCost());
                MainActivity.txtPay.setText("₹"+Common.cartRepository.getCost());
                MainActivity.txtTotalAmount.setText("₹"+Common.cartRepository.getCost());
            }else {
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                MainActivity.rlCart.setVisibility(View.GONE);
            }
        });

    }

    private void deleteItem(int position,/*, CartAdapterViewHolder holder*/ProgressDialog progressDialog) {
      //  notifyDataSetChanged();
        deleteList(position,progressDialog);

       // holder.itemView.setVisibility(View.GONE);
    }

    private void deleteList(int position, ProgressDialog progressDialog) {
        carts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, carts.size());
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class CartAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgVegNonVeg;
        TextView txtCartItemName,txtQuantity,txtPrice;
        Button btnMinus,btnPlus;
        LinearLayout lnPlusMinus;

        CartAdapterViewHolder(View itemView) {
            super(itemView);
            imgVegNonVeg=itemView.findViewById(R.id.img_veg_nonveg);
            txtCartItemName=itemView.findViewById(R.id.txt_cartitem_name);
            btnMinus=itemView.findViewById(R.id.btn_minus);
            txtQuantity=itemView.findViewById(R.id.txt_quantity);
            btnPlus=itemView.findViewById(R.id.btn_plus);
            txtPrice=itemView.findViewById(R.id.txt_cartitem_price);
            lnPlusMinus=itemView.findViewById(R.id.ln_cart_plusminus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           /* Cart task = carts.get(getAdapterPosition());
            Intent intent = new Intent(mCtx,MainActivity.class);
           // intent.putExtra("task", task);
            SplashActivity.savePreferences(Constant.ADDRESS, String.valueOf(task));
            mCtx.startActivity(intent);*/
        }
    }

/*
    private void setupProgress() {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

       */
/* mostSellingloadListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressDialog.dismiss();
                mostSellingloadListAdapter.unregisterAdapterDataObserver(this);
            }
        });
        requestListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressDialog.dismiss();
                requestListAdapter.unregisterAdapterDataObserver(this);
            }
        });*//*

    }
*/
}
