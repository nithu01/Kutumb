package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Adapter.MenuItemsMainAdapter;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Database.DataSource.CartRepository;
import com.app.kutumb.Database.Local.CartDataSource;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Model.MenuItems;
import com.app.kutumb.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDetailsActivity extends AppCompatActivity implements View.OnClickListener, MenuItemsMainAdapter.MenuItemsMainAdapterListner {

    private static final String TAG = MenuDetailsActivity.class.getSimpleName();
    private ImageView imgBack;
    TextView txtTitle;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtCart;
    private RecyclerView requestList;
    RelativeLayout rlNoData,rlCart;
    String menuId="",menuName="";

    private static List<MenuItems> listData;
    @SuppressLint("StaticFieldLeak")
    private static MenuItemsMainAdapter loadListAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);
        pDialog=new ProgressDialog(MenuDetailsActivity.this);
        imgBack=findViewById(R.id.img_back_menudetails);
        txtTitle=findViewById(R.id.txt_title_menudetails);
        rlNoData=findViewById(R.id.rl_nodata_details);
        requestList=findViewById(R.id.recyclerview_details);
        txtCart=findViewById(R.id.txt_cart);
        rlCart=findViewById(R.id.rl_cart);
        rlCart.setOnClickListener(this::onCart);
        imgBack.setOnClickListener(this);
        try{
            menuId=getIntent().getStringExtra(Constant.MENU_ID);
            menuName=getIntent().getStringExtra(Constant.MENU_NAME);
            txtTitle.setText(menuName);
        }catch (Exception e){
         e.printStackTrace();
        }
        if (Configuration.hasNetworkConnection(MenuDetailsActivity.this)){
            getDataList(menuId);
        }else {
            Configuration.openPopupUpDownBack(MenuDetailsActivity.this,R.style.Dialod_UpDown,"main","internetError",
                    "No Internet Connectivity"+
                            ", Thanks","");
        }
        listData = new ArrayList<>();
        loadListAdapter = new MenuItemsMainAdapter(MenuDetailsActivity.this, listData, this);
        initDB();
    }

    private void onCart(View view) {
        Intent intent = new Intent(MenuDetailsActivity.this,CartDetails.class);
        intent.putExtra(Constant.MENU_ID,menuId);
        intent.putExtra(Constant.MENU_NAME,menuName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        // overridePendingTransition(R.anim.slide_in_top,R.anim.slide_from_top);
    }

    private void initDB() {
        Common.cartDatabase = CartDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
       // Common.cartRepository.emptyCart();
        try{
             txtCart.setText(String.valueOf(Common.cartRepository.countCartItems()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if (v==imgBack) {
            Intent intent = new Intent(MenuDetailsActivity.this, MainActivity.class);
            intent.putExtra(Constant.MENU_ID, menuId);
            intent.putExtra(Constant.MENU_NAME, menuName);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MenuDetailsActivity.this, MainActivity.class);
        intent.putExtra(Constant.MENU_ID, menuId);
        intent.putExtra(Constant.MENU_NAME, menuName);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void getDataList(String menuId) {
        String tag_string_req = "view_req";

        Configuration.showDialog("Please wait...",pDialog);
        try{Configuration.hideKeyboardFrom(MenuDetailsActivity.this);}catch (Exception e){e.printStackTrace();}


        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.GET_MENU_LIST_CATEGORY,
                response -> {
                    Log.d(TAG,"VIEW RESPONSED"+response);
                    if (pDialog!=null||pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");
                        if (responseCode.equalsIgnoreCase("0")) {
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            rlNoData.setVisibility(View.GONE);
                            requestList.setVisibility(View.VISIBLE);
                            List<MenuItems> items = new Gson().fromJson(jsonArray.toString(),
                                    new TypeToken<List<MenuItems>>(){}.getType());

                            listData.clear();
                            listData.addAll(items);
                            loadListAdapter.notifyDataSetChanged();

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuDetailsActivity.this);
                            requestList.setLayoutManager(mLayoutManager);
                            requestList.setItemAnimator(new DefaultItemAnimator());
                            requestList.setAdapter(loadListAdapter);

                        }else{
                            rlNoData.setVisibility(View.VISIBLE);
                            requestList.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (pDialog!=null||pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.e("PASS ERROR","ERROR<|-|><|-|><|-|><|-|><|-|>"+error.toString());
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.MENU_ID,menuId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    @Override
    public void MenuItemsMainAdapterSelcted(MenuItems menuItems) {

    }
}
