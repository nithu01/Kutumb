package com.app.kutumb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Model.CouponStatus;
import com.app.kutumb.Model.Datum;
import com.app.kutumb.R;

import java.util.ArrayList;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.Holder> {
    List<Datum> list;
    Context context;

    public CouponAdapter(Context applicationContext, List<Datum> arrayList) {
                this.context=applicationContext;
                this.list=arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon_layout, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.coupon_details.setText(list.get(position).getCode());
        holder.coupon.setText(list.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView coupon,coupon_details;
        public Holder(@NonNull View itemView) {
            super(itemView);
            coupon=itemView.findViewById(R.id.coupon_code);
            coupon_details=itemView.findViewById(R.id.desc);
        }
    }
}
