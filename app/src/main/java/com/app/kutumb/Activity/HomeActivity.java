package com.app.kutumb.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Adapter.AllDataAdapter;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private TextView txtCurrentLocation,txtMostSelling,txtItemCart,txtTotalPriceCart,txtViewCart;
    private RecyclerView offerRecyclerView,allDataRecyclerView,mostSellingRecyclerView;
    private ImageView imgShare;
    private SwipeRefreshLayout swipeRefreshMain;
    private Switch switchVegNonVeg;
    private RelativeLayout rlFilterSearch,rlBottomCartDetail;
    private BottomNavigationView bottomNavigationBar;
    private ProgressDialog progressDialog;
    String type="";

    @SuppressLint("StaticFieldLeak")
    public static AllDataAdapter requestListAdapter;
    ArrayList<String> menu_id= new ArrayList<>();
    ArrayList<String> menu_name= new ArrayList<>();
    ArrayList<String> menu_image_name= new ArrayList<>();
    ArrayList<String> items= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressDialog=new ProgressDialog(HomeActivity.this);

        txtCurrentLocation=findViewById(R.id.txt_location);
        allDataRecyclerView=findViewById(R.id.recyclerView_data);
        offerRecyclerView=findViewById(R.id.recyclerview_offer);

        imgShare=findViewById(R.id.img_share_app);
        swipeRefreshMain=findViewById(R.id.swiperefresh_layout);
        switchVegNonVeg=findViewById(R.id.switch_veg_nonveg);

        rlFilterSearch=findViewById(R.id.rl_filter_search);
        txtMostSelling=findViewById(R.id.txt_recommended);
        mostSellingRecyclerView=findViewById(R.id.recyclerview_most_selling);

        bottomNavigationBar=findViewById(R.id.bottomBar);
        rlBottomCartDetail=findViewById(R.id.rl_cart_item);
        txtItemCart=findViewById(R.id.txt_item_count);

        txtTotalPriceCart=findViewById(R.id.txt_item_price);
        txtViewCart=findViewById(R.id.txt_view_cart);

        switchVegNonVeg.setChecked(false);
        switchVegNonVeg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchVegNonVeg.isChecked()){
                type="1";
                getAllData(type);
            }else {
                type="";
                getAllData(type);
            }
        });

        if (Configuration.hasNetworkConnection(HomeActivity.this)){
          //  getOffers();
            getAllData(type);
          //  getMostSelling();
        }else {
            Configuration.openPopupUpDown(HomeActivity.this,R.style.Dialod_UpDown,"internetError",
                    "No Internet Connectivity1"+
                            ", Thanks");
        }

    }

    private void getAllData(String type) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String tag_string_req = "fund_req";
        menu_id.clear();
        menu_image_name.clear();
        menu_name.clear();
        items.clear();

        Configuration.showDialog("Please wait...",progressDialog);
        try{Configuration.hideKeyboardFrom(HomeActivity.this);}catch (Exception e){e.printStackTrace();}



        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.GET_MENU,
                response -> {
                    progressDialog.dismiss();
                    Log.d(TAG,"RESPONSED"+response+"\n");
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");
                        if(responseCode.equals("0")) {
                            //  rlNoData.setVisibility(View.GONE);
                            allDataRecyclerView.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayObject = jsonObject.getJSONArray("data");
                            if (jsonArrayObject.isNull(0)) {
                               /* rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");*/
                                allDataRecyclerView.setVisibility(View.GONE);
                            } else {
                                //  rlNoData.setVisibility(View.GONE);
                                allDataRecyclerView.setVisibility(View.VISIBLE);
                                if (HomeActivity.this.type.equals("1")){
                                    mostSellingRecyclerView.setVisibility(View.GONE);
                                    allDataRecyclerView.setVisibility(View.VISIBLE);
                                    txtMostSelling.setVisibility(View.GONE);
                                }else {
                                    mostSellingRecyclerView.setVisibility(View.VISIBLE);
                                    allDataRecyclerView.setVisibility(View.VISIBLE);
                                    txtMostSelling.setVisibility(View.VISIBLE);
                                }
                                for (int i = 0; i < jsonArrayObject.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayObject.getJSONObject(i);
                                    if (jsonObject1.has("items")) {
                                        menu_id.add(jsonObject1.getString("menu_id"));
                                        menu_name.add(jsonObject1.getString("menu_name"));
                                        menu_image_name.add(jsonObject1.getString("menu_image_name"));
                                        items.add(jsonObject1. getString("items"));
                                    }
                                    //  System.out.println("size-->" + menu_id.size() + "Name--->" + menu_name.get(i) + "items--->" + items.get(i));
                                }

                                requestListAdapter = new AllDataAdapter(HomeActivity.this,
                                        menu_id, menu_name,menu_image_name, items);
                                // LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                                allDataRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                                        RecyclerView.VERTICAL, true));
                                // requestListData.setLayoutManager(mLayoutManager);
                                allDataRecyclerView.setAdapter(requestListAdapter);
                                requestListAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                           /*rlNoData.setVisibility(View.VISIBLE);
                            imgData.setImageResource(R.drawable.norequest);
                            txtData.setText("No Transaction");*/
                            allDataRecyclerView.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                       /* rlNoData.setVisibility(View.VISIBLE);
                        imgData.setImageResource(R.drawable.norequest);
                        txtData.setText("No Transaction");*/
                        allDataRecyclerView.setVisibility(View.GONE);
                        Configuration.openPopupUpDown(HomeActivity.this,R.style.Dialod_UpDown,"internetError",
                                "No Internet connectivity3"+
                                        ", Thanks");
                    }
                    progressDialog.dismiss();
                },
                error -> {
                    progressDialog.dismiss();
                    Log.e("PASS ERROR","ERROR--->"+error.toString());
                    Configuration.openPopupUpDown(HomeActivity.this,R.style.Dialod_UpDown,"internetError",
                            "No Internet connectivity2"+
                                    ", Thanks");
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.ITEM_TYPE, HomeActivity.this.type);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

}
