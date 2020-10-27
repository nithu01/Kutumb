package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Adapter.AddressAdapter;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.PrefManager;
import com.app.kutumb.Interface.Apiinterface;
import com.app.kutumb.Model.AddressModel;
import com.app.kutumb.Model.Response;
import com.app.kutumb.Model.UserData;
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
import java.util.concurrent.TimeUnit;

import okhttp3.Address;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = AddressActivity.class.getSimpleName();
    private ImageView imgBackLocation;
    @SuppressLint("StaticFieldLeak")
    private static RelativeLayout rlAddressList;
    private RelativeLayout rlCurrentLocation;
    private Button btnAddNewAddress,btnAddAddress;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView recyclerViewAddressList;
    @SuppressLint("StaticFieldLeak")
    private static ScrollView scrollViewAddress;
    private EditText editTextMyAddress,editTextFirstName,editTextLastName,
            editTextPincode,editTextAddressOne,editTextAddresstwo,editTextCity;

    Spinner spinnerState;
    private ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    UserData userData;
    private static List<AddressModel> listData;
    Button add_address;
    @SuppressLint("StaticFieldLeak")
    private static AddressAdapter loadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        activity=AddressActivity.this;
        userData=PrefManager.getInstance(AddressActivity.this).getUserData();



                progressDialog = new ProgressDialog(AddressActivity.this);
                imgBackLocation = findViewById(R.id.img_back_location);
                rlAddressList = findViewById(R.id.rl_addres_list);
                rlCurrentLocation = findViewById(R.id.rl_current_location);
                btnAddNewAddress = findViewById(R.id.btn_add_new_address);
                recyclerViewAddressList = findViewById(R.id.recyclerview_delivery_address);
                scrollViewAddress = findViewById(R.id.scrollview_add_address);
                editTextMyAddress = findViewById(R.id.edittext_address_title);
                editTextPincode = findViewById(R.id.edittext_pincode);
                editTextAddressOne = findViewById(R.id.edittext_address_one);
                editTextAddresstwo = findViewById(R.id.edittext_address_two);
                editTextCity = findViewById(R.id.edittext_city);
                editTextFirstName = findViewById(R.id.edittext_first_name);
                editTextLastName = findViewById(R.id.edittext_last_name);
                btnAddAddress = findViewById(R.id.btn_add_address);
                spinnerState = findViewById(R.id.spinner_state);
                imgBackLocation.setOnClickListener(this);
                btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlAddressList.setVisibility(View.GONE);
                        scrollViewAddress.setVisibility(View.VISIBLE);
                    }
                });
                rlCurrentLocation.setOnClickListener(this);
//        scrollViewAddress.setVisibility(View.GONE);
                btnAddAddress.setOnClickListener(this);
                listData = new ArrayList<>();
                loadListAdapter = new AddressAdapter(AddressActivity.this, listData);
                // permissionsToRequest=new ArrayList<>();
                UserData userData = PrefManager.getInstance(AddressActivity.this).getUserData();
                if (Configuration.hasNetworkConnection(AddressActivity.this)) {
                    getAddressData(userData.getId());
                } else {
                    Configuration.openPopupUpDown(AddressActivity.this, R.style.Dialod_UpDown, "error",
                            "No Internet Connectivity");
                }
        editTextFirstName.setText(userData.getFirst_name());
         editTextLastName.setText(userData.getLast_name());
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.savePreferences("city",editTextCity.getText().toString());
                SplashActivity.savePreferences("pincode",editTextPincode.getText().toString());
                SplashActivity.savePreferences("house",editTextAddressOne.getText().toString());
                SplashActivity.savePreferences("street",editTextAddresstwo.getText().toString());
                SplashActivity.savePreferences("first",editTextFirstName.getText().toString());
                SplashActivity.savePreferences("last",editTextLastName.getText().toString());

                                            ProgressDialog progressDialog=new ProgressDialog(AddressActivity.this);
                                            progressDialog.show();

                                               OkHttpClient client = new OkHttpClient.Builder()
                                                       .connectTimeout(100, TimeUnit.SECONDS)
                                                       .readTimeout(100, TimeUnit.SECONDS).build();
                                               Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.CONSTANT).client(client).addConverterFactory(GsonConverterFactory.create()).build();
                                               Apiinterface apiinterface = retrofit.create(Apiinterface.class);
                                               Call<Response> call = apiinterface.addaddress(Constant.APICODE_VALUE,userData.getId(), editTextAddressOne.getText().toString(), editTextAddresstwo.getText().toString(),"delhi",editTextPincode.getText().toString(),editTextCity.getText().toString(),"delhi","12.2131","12.312","delhi");
                                               call.enqueue(new Callback<Response>() {
                                                   @Override
                                                   public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                                       Toast.makeText(AddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                       progressDialog.dismiss();
                                                       startActivity(new Intent(AddressActivity.this,MainActivity.class).putExtra("address","address"));
                                                   }

                                                   @Override
                                                   public void onFailure(Call<Response> call, Throwable t) {
                                                       Log.d("TAG", "VALUEDATA" + t);

                                                   }
                                               });
                                           }
        });
            }


            private void getAddressData(String id) {
                String tag_string_req = "GET_USER_ADDRESS";

                Configuration.showDialog("Please wait...", progressDialog);
                progressDialog.setCanceledOnTouchOutside(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        AppConfig.GET_USER_ADDRESS,
                        response -> {

                            Log.d(TAG, "GET_USER_ADDRESS" + response);
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase("0")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.isNull(0)) {
                                        rlAddressList.setVisibility(View.GONE);
                                        scrollViewAddress.setVisibility(View.VISIBLE);
                                    } else {
                                        rlAddressList.setVisibility(View.VISIBLE);
                                        scrollViewAddress.setVisibility(View.GONE);

                                        List<AddressModel> items = new Gson().fromJson(jsonArray.toString(),
                                                new TypeToken<List<AddressModel>>() {
                                                }.getType());

                                        listData.clear();
                                        listData.addAll(items);
                                        loadListAdapter.notifyDataSetChanged();

                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddressActivity.this);
                                        recyclerViewAddressList.setLayoutManager(mLayoutManager);
                                        recyclerViewAddressList.setItemAnimator(new DefaultItemAnimator());
                                        recyclerViewAddressList.setAdapter(loadListAdapter);
                                    }

                                } else {
                                    rlAddressList.setVisibility(View.GONE);
                                    scrollViewAddress.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {

                            assert progressDialog != null;
                            progressDialog.dismiss();
                            Configuration.openPopupUpDown(AddressActivity.this, R.style.Dialod_UpDown,
                                    "error", "Something went wrong\nTry after sometime" +
                                            "\n(Please check your internet connection)");
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(Constant.API_CODE, Constant.APICODE_VALUE);
                        params.put(Constant.USER_ID, id);
                        return params;
                    }

                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
            }

    @Override
    public void onClick(View view) {

    }
}
