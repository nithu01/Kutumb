package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Database.DataSource.CartRepository;
import com.app.kutumb.Database.Local.CartDataSource;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Database.ModelDB.Cart;
import com.app.kutumb.Model.OfferItem;
import com.app.kutumb.Model.OfferItemDetail;
import com.app.kutumb.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferItemsAdapter extends RecyclerView.Adapter<OfferItemsAdapter.MyViewHolder>
        implements Filterable, OfferDetailsAdapter.OfferDetailsAdapterListener {

    private static final String TAG = OfferItemsAdapter.class.getSimpleName();
    private Context context;
    private List<OfferItem> loadList;
    private List<OfferItem> loadListFiltered;
    private OfferItemsAdapterListener listener;
    private ProgressDialog progressDialog;
    SessionManager session;

    @Override
    public void onOfferDetailsAdapterSelected(OfferItemDetail offerItemDetail) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgViewIcon;
        MyViewHolder(View view) {
            super(view);
            imgViewIcon=view.findViewById(R.id.img_offer);
        }
    }


    public OfferItemsAdapter(Context context, List<OfferItem> rechargeList, OfferItemsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = rechargeList;
        this.loadListFiltered = rechargeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final OfferItem contact = loadListFiltered.get(position);
        session=new SessionManager(context);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.logo);

        Glide.with(context)
                .load(contact.getOffer_image_name())
                .apply(options)
                .into(holder.imgViewIcon);

        holder.imgViewIcon.setOnClickListener(v -> {
            progressDialog=new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Loading offers");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            final Handler handler = new Handler();
            handler.postDelayed(() ->
                    getOfferDetails(contact.getOffer_id(),contact.getOffer_cost(),contact.getOffer_name()), 2000);

        });
    }

    @SuppressLint("SetTextI18n")
    private void getOfferDetails(String offer_id, String offer_cost, String offer_name) {
        final Dialog dialog=new Dialog(context);
        Common.cartDatabase = CartDatabase.getInstance(context);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
        Common.cartRepository.emptyCart();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_offer_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        List<OfferItemDetail> listData=new ArrayList<>();
        OfferDetailsAdapter loadListAdapter=new OfferDetailsAdapter(context, listData, this);
        TextView txtName=dialog.findViewById(R.id.txt_offer_name);
        TextView txtPrice=dialog.findViewById(R.id.txt_price_offer);
        Button btnAddcart=dialog.findViewById(R.id.btn_add_cart);
        LinearLayout lnClose=dialog.findViewById(R.id.ln_close);
        RecyclerView recyclerView=dialog.findViewById(R.id.recycler_view_offer_details);
        txtName.setText(offer_name);
        txtPrice.setText(offer_cost+" OFF");
        btnAddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<listData.size();i++){
                    Toast.makeText(context, ""+listData.get(i).getItem_id(), Toast.LENGTH_SHORT).show();
                    Cart cartItem = new Cart();
                    cartItem.item_id = listData.get(i).getItem_id();
                    cartItem.menu_id = listData.get(i).getMenu_id();
                    cartItem.item_name = listData.get(i).getItem_name();
                    cartItem.item_cost = "";
                    cartItem.item_type_id = "";
                    cartItem.item_image_name = "";
                    cartItem.item_description = "";
                    cartItem.status = "";
                    cartItem.is_most_selling_item = "";
                    cartItem.product_id ="";
                    cartItem.quantity = listData.get(i).getQuantity();
                    Common.cartRepository.insertToCart(cartItem);
                    dialog.dismiss();
                }
                String item = Common.cartRepository.getQuantity();
                if (!item.equals("0")) {
                    MainActivity.txtItemCount.setText(item + " Item");
                    MainActivity.bottomNavigationView.setVisibility(View.GONE);
                    MainActivity.rlCart.setVisibility(View.VISIBLE);
                } else {
                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                    MainActivity.rlCart.setVisibility(View.GONE);
                }
            }
        });
        lnClose.setOnClickListener(v -> dialog.dismiss());

        getOffer(recyclerView,progressDialog,listData,loadListAdapter,offer_id);

//        btnAddcart.setOnClickListener(v -> {
//            if (session.isLoggedIn()){
//                if (Common.cartRepository.checkCart(offer_id)!=null
//                        &&Common.cartRepository.checkCart(offer_id).equals(offer_id)){
//                    Toast.makeText(context, "Allready in Cart", Toast.LENGTH_SHORT).show();
//                }else {
//                    try {
//                        Cart cartItem = new Cart();
//                        cartItem.item_id = offer_id;
//                      //  cartItem.menu_id = contact.getMenu_id();
//                        cartItem.item_name = listData.get(1).getItem_name();
//                        cartItem.item_cost = contact.getItem_cost();
//                        cartItem.item_type_id = contact.getItem_type_id();
//                        cartItem.item_image_name = contact.getItem_image_name();
//                        cartItem.item_description = contact.getItem_description();
//                        cartItem.status = contact.getStatus();
//                        cartItem.is_most_selling_item = contact.getIs_most_selling_item();
//                        cartItem.product_id = contact.getProduct_id();
//                        cartItem.quantity = "1";
//                        holder.txtQuantity.setText("1");
//                        //Add to DB
//                        Common.cartRepository.insertToCart(cartItem);
//                        holder.lnCart.setVisibility(View.VISIBLE);
//                        holder.btnAdd.setVisibility(View.GONE);
//                        String sum = Common.cartRepository.getCost();
//                        String item = Common.cartRepository.getQuantity();
//                        if (!sum.equals("0") && !item.equals("0")) {
//                            MainActivity.txtItemCount.setText(item + " Item");
//                            MainActivity.txtItemsPrice.setText("₹" + sum);
//                            MainActivity.bottomNavigationView.setVisibility(View.GONE);
//                            MainActivity.rlCart.setVisibility(View.VISIBLE);
//                        } else {
//                            MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
//                            MainActivity.rlCart.setVisibility(View.GONE);
//                        }
//
//                    } catch (Exception ex) {
//                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.e("ERROR DATABASE", "ERROR--->" + ex.getMessage());
//                    }
//                }
//            }else {
//                Configuration.openPopupUpDownBack(context,R.style.Dialod_UpDown,"login","error",
//                        "Please login/signup first to get order at your door","");
//            }
//        });

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void getOffer(RecyclerView recyclerView, ProgressDialog progressDialog,
                          List<OfferItemDetail> listData, OfferDetailsAdapter loadListAdapter, String offer_id) {

        String tag_string_req = "offer_details";

        Configuration.showDialog("Please wait...",progressDialog);

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.GET_OFFERS_ITEMS,
                response -> {
                    Log.d(TAG,"OFFER RESPONSED"+response);
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("0")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)){
                                recyclerView.setVisibility(View.GONE);
//                                Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
//                                        "Something went wrong\n Try after sometime"+
//                                                ", Thanks");
                            }else {
                                recyclerView.setVisibility(View.VISIBLE);
                                List<OfferItemDetail> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<OfferItemDetail>>() {
                                        }.getType());
                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(loadListAdapter);
                            }
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                    "Something went wrong\n Try after sometime"+
                                            ", Thanks");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                "Something went wrong\n Try after sometime"+
                                        ", Thanks");
                    }

                    progressDialog.dismiss();
                },
                error -> {

                    progressDialog.dismiss();
                    Toast.makeText(context,"Try again",Toast.LENGTH_SHORT).show();
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.OFFER_ID,offer_id);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

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
                    List<OfferItem> filteredList = new ArrayList<>();
                    for (OfferItem row : loadList) {

                        if (row.getOffer_id().toLowerCase().contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<OfferItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OfferItemsAdapterListener {
        void onOfferItemsAdapterSelected(OfferItem offerItem);
    }
}
