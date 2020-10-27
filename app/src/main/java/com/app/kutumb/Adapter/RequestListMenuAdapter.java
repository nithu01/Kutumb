package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Model.MenuItems;
import com.app.kutumb.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RequestListMenuAdapter extends RecyclerView.Adapter<RequestListMenuAdapter.ViewHolder>
        implements MenuItemsAdapter.MenuItemsAdapterListner {

    private static final String TAG =RequestListMenuAdapter.class.getSimpleName() ;
    private Context mContext;
    private ArrayList<String> MenuId;
    private ArrayList<String> MenuName;
    private ArrayList<String> MenuImage;
    private ArrayList<String> Items;
    //String Type;

    private static List<MenuItems> listData;
    @SuppressLint("StaticFieldLeak")
    private static MenuItemsAdapter loadListAdapter;

    public RequestListMenuAdapter(Context context, ArrayList<String> menuId, ArrayList<String> menuName,
                                  ArrayList<String> menuImage, ArrayList<String> items) {
        mContext=context;
        MenuId=menuId;
        MenuName=menuName;
        MenuImage=menuImage;
        Items=items;
      //  Type=type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor","SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RequestListMenuAdapter.ViewHolder holder, final int position) {

           listData = new ArrayList<>();
           loadListAdapter = new MenuItemsAdapter(mContext, listData, this);
           holder.txtMenuName.setText(MenuName.get(position));
           List<MenuItems> items = new Gson().fromJson(Items.get(position),
                   new TypeToken<List<MenuItems>>() {
                   }.getType());
           listData.clear();
           listData.addAll(items);
           loadListAdapter.notifyDataSetChanged();

           holder.requestList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true));
           holder.requestList.setItemAnimator(new DefaultItemAnimator());
           holder.requestList.setAdapter(loadListAdapter);
           loadListAdapter.notifyDataSetChanged();
           MenuItemsAdapter.getMenuName(MenuName.get(position));

    }

    @Override
    public int getItemCount() {
        return MenuId.size();
    }

    @Override
    public void onMenuItemsAdapterSelcted(MenuItems menuItems) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMenuName;
        Button btnMore;
       // CardView cardView;
        RecyclerView requestList;

        ViewHolder(View itemView) {
            super(itemView);
            txtMenuName=itemView.findViewById(R.id.txt_menu_name);
            btnMore=itemView.findViewById(R.id.btn_more);
            requestList=itemView.findViewById(R.id.recyclerview_menu_item);
           // cardView=itemView.findViewById(R.id.cardview_menu);
           // cardViewMore=itemView.findViewById(R.id.cardview_more);
        }
    }
}
