package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.app.kutumb.Adapter.MostSellingItemsAdapter;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
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

public class FilterSearchActivity extends AppCompatActivity
        implements View.OnClickListener,
        MostSellingItemsAdapter.MostSellingItemsAdapterListner {
    private static final String TAG = FilterSearchActivity.class.getSimpleName();
    // private ImageView imgBack;
    private EditText editTextSearch;
    private ImageView imgSearch;
    private TextView txtTitleSearch;
    private RecyclerView requestList;
    String item="";
    private ProgressDialog progressDialog;
    List<MenuItems> mostSellinglistData;
    MostSellingItemsAdapter mostSellingloadListAdapter;
    private RelativeLayout rlNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        progressDialog=new ProgressDialog(FilterSearchActivity.this);
        editTextSearch=findViewById(R.id.edittext_search);
        rlNoData=findViewById(R.id.rl_no_food);
        imgSearch=findViewById(R.id.img_search_filter);
        txtTitleSearch=findViewById(R.id.txt_title_search);
        requestList=findViewById(R.id.recyclerview_filter);
        imgSearch.setOnClickListener(this);
        editTextSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mostSellinglistData = new ArrayList<>();
        mostSellingloadListAdapter = new MostSellingItemsAdapter(FilterSearchActivity.this, mostSellinglistData, this);
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                // Your action on done
                item=editTextSearch.getText().toString();
                if (TextUtils.isEmpty(item)){
                    editTextSearch.setError("Enter something");
                    editTextSearch.requestFocus();
                }else if (Configuration.hasNetworkConnection(FilterSearchActivity.this)){
                    getItemData(item);
                }else {
                    Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"internetError",
                            "No Internet Connectivity");
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        if (v==imgSearch){
            item=editTextSearch.getText().toString();
            if (TextUtils.isEmpty(item)){
                editTextSearch.setError("Enter something");
                editTextSearch.requestFocus();
            }else if (Configuration.hasNetworkConnection(FilterSearchActivity.this)){
                getItemData(item);
            }else {
                Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No Internet Connectivity");
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FilterSearchActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }

    @SuppressLint("SetTextI18n")
    private void getItemData(String item) {
        String tag_string_req = "item_data_search";
        txtTitleSearch.setText("Related to \""+item+"\"");

        Configuration.showDialog("Please wait...",progressDialog);
        try{Configuration.hideKeyboardFrom(FilterSearchActivity.this);}catch (Exception e){e.printStackTrace();}

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.SEARCH_ITEM,
                response -> { Log.e(TAG,"item_data_search-->"+"\n"+response+"\n");
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");
                        if(responseCode.equals("0")) {
                            //  rlNoData.setVisibility(View.GONE);
                            requestList.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayObject = jsonObject.getJSONArray("data");
                            if (jsonArrayObject.isNull(0)) {
                               /* rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");*/
                                requestList.setVisibility(View.GONE);
                                rlNoData.setVisibility(View.VISIBLE);
                                Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"error",
                                        "No Items Available"+
                                                ", Thanks");
                                editTextSearch.setText("");
                            } else {
                                //  rlNoData.setVisibility(View.GONE);
                                requestList.setVisibility(View.VISIBLE);
                                rlNoData.setVisibility(View.GONE);
                                List<MenuItems> items = new Gson().fromJson(jsonArrayObject.toString(),
                                        new TypeToken<List<MenuItems>>() {
                                        }.getType());

                                mostSellinglistData.clear();
                                mostSellinglistData.addAll(items);
                                mostSellingloadListAdapter.notifyDataSetChanged();
                                requestList.setLayoutManager(new LinearLayoutManager(FilterSearchActivity.this,
                                        RecyclerView.VERTICAL, false));
                                requestList.setItemAnimator(new DefaultItemAnimator());
                                requestList.setAdapter(mostSellingloadListAdapter);
                                mostSellingloadListAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                           /* rlNoData.setVisibility(View.VISIBLE);
                            imgData.setImageResource(R.drawable.norequest);
                            txtData.setText("No Transaction");*/
                            requestList.setVisibility(View.GONE);
                            rlNoData.setVisibility(View.VISIBLE);
                            Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"error",
                                    "No Items Available"+
                                            ", Thanks");
                            editTextSearch.setText("");
                            Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"error",
                                    "No Items Available"+
                                            ", Thanks");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                       /* rlNoData.setVisibility(View.VISIBLE);
                        imgData.setImageResource(R.drawable.norequest);
                        txtData.setText("No Transaction");*/
                        requestList.setVisibility(View.GONE);
                        rlNoData.setVisibility(View.VISIBLE);
                        Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"internetError",
                                "No Internet connectivity"+
                                        ", Thanks");
                    }
                    progressDialog.dismiss();
                },
                error -> {

                    progressDialog.dismiss();
                    Log.e("PASS ERROR","ERROR--->"+error.toString());
                    Configuration.openPopupUpDown(FilterSearchActivity.this,R.style.Dialod_UpDown,"internetError",
                            "No Internet connectivity"+
                                    ", Thanks");
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.SEARCH,item);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public void onMenuItemsAdapterSelcted(MenuItems menuItems) {

    }
}
