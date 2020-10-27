package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllDataAdapter extends RecyclerView.Adapter<AllDataAdapter.ViewHolder>{

    private static final String TAG =RequestListMenuAdapter.class.getSimpleName() ;
    private Context mContext;
    private ArrayList<String> MenuId;
    private ArrayList<String> MenuName;
    private ArrayList<String> MenuImage;
    private ArrayList<String> Items;
    //String Type;


    public AllDataAdapter(Context context, ArrayList<String> menuId, ArrayList<String> menuName,
                                  ArrayList<String> menuImage, ArrayList<String> items) {
        mContext=context;
        MenuId=menuId;
        MenuName=menuName;
        MenuImage=menuImage;
        Items=items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor","SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtMenuName.setText(MenuName.get(position));
        JSONArray productArray = new JSONArray(Items);
        String[] item_id = new String[productArray.length()];
        String[] menu_id = new String[productArray.length()];
        String[] item_name = new String[productArray.length()];
        String[] item_cost = new String[productArray.length()];
        String[] item_type_id = new String[productArray.length()];
        String[] item_image_name = new String[productArray.length()];
        String[] item_description = new String[productArray.length()];
        String[] status = new String[productArray.length()];
        String[] is_most_selling_item = new String[productArray.length()];
        String[] product_id = new String[productArray.length()];
        JSONObject json_data = new JSONObject();
        try {
            for (int i = 0; i <Items.size(); i++) {
                json_data = productArray.getJSONObject(i);
                item_id[i] = json_data.getString("item_id");
                menu_id[i] = json_data.getString("menu_id");
                item_name[i] = json_data.getString("item_name");
                item_cost[i] = json_data.getString("item_cost");
                item_type_id[i] = json_data.getString("item_type_id") /*+ " /-"*/;
                item_image_name[i] = json_data.getString("item_image_name");
                item_description[i] = /*"\u20B9" + */json_data.getString("item_description") /*+ " /-"*/;
                status[i] = json_data.getString("status") /*+ " /-"*/;
                is_most_selling_item[i] = json_data.getString("is_most_selling_item");
                product_id[i] = json_data.getString("product_id");
               /* double p_mrp = Double.parseDouble(json_data.getString("mrp"));
                double p_sp = Double.parseDouble(json_data.getString("selling_price"));
                double p_dp = (p_mrp - p_sp) / (p_mrp / 100);
                int p_dp_i = (int) p_dp;
                product_dps[i] = String.valueOf(p_dp_i);*/

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.requestList.setNestedScrollingEnabled(false);
        holder.requestList.setLayoutManager(new LinearLayoutManager(mContext));
     /*   holder.requestList.setAdapter(new Recent_Products_Adapter(item_id, menu_id, item_name, item_cost, item_type_id,
                item_image_name, item_description, status,is_most_selling_item,product_id, mContext));
*/
        MenuItemsAdapter.getMenuName(MenuName.get(position));
    }

    @Override
    public int getItemCount() {
        return MenuId.size();
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
