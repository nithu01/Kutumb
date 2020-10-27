package com.app.kutumb.Config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Activity.LoginActivity;
import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Adapter.AddOnAdapter;
import com.app.kutumb.Interface.Apiinterface;
import com.app.kutumb.Model.AddOnList;
import com.app.kutumb.Model.UserData;
import com.app.kutumb.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by aftab on 5/9/2018.
 */

public class WebService {

    Context mContext;


    public WebService(Context context) {
        mContext=context;
    }

    public static void login(String username, String password, final ProgressDialog progressDialog,
                             final LoginActivity loginActivity, final SessionManager session, final String type) {

        String tag_string_req = "login_res";

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

//        try {
//            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
//            SSLContext context = null;
//            context = SSLContext.getInstance("TLS");
//            context.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//            e.printStackTrace();
//        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN, response -> {
                    Log.e(TAG, "DATA Login Response: " + response);
                    progressDialog.dismiss();
                    try {
                        JSONObject jObj=new JSONObject(response);
                        String status=jObj.getString("status");
                        if (status.equalsIgnoreCase("0")){
                            JSONObject jsonObject=jObj.getJSONObject("data");
                            session.setLogin(true);
                            UserData userData = new UserData(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("phone"),
                                    jsonObject.getString("first_name"),
                                    jsonObject.getString("last_name"));
                            PrefManager.getInstance(loginActivity).userLogin(userData);
                            Intent intent =new Intent(loginActivity, MainActivity.class);
                            intent.putExtra(Constant.TYPE,type);
                            loginActivity.startActivity(intent);
                            loginActivity.finish();
                            loginActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                           // SplashActivity.savePreferences(Constant.TYPE,"");
                        }else {
                            Configuration.openPopupUpDown(loginActivity,R.style.Dialod_UpDown,"error", "Invalid details Please confirm your details");
                        }
                     /*   String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");*/
                        //  Log.d(TAG, "Response: " + jarray.toString());
                        // Log.d(TAG,"status:"+status);
                        //JSONObject jsonObject=jsonObject1.getJSONObject("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(loginActivity,
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.EMAIL, username);
                params.put(Constant.PASSWORD,password);
                params.put(Constant.GROUP_ID,"2");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(100, TimeUnit.SECONDS)
//                .readTimeout(100,TimeUnit.SECONDS).build();
//        Retrofit retrofit=new Retrofit.Builder().baseUrl(AppConfig.CONSTANT).client(client).addConverterFactory(GsonConverterFactory.create()).build();
//        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
//        Call<com.app.kutumb.Model.Response> call=apiinterface.login(Constant.APICODE_VALUE,username,password,"2");
//        call.enqueue(new Callback<com.app.kutumb.Model.Response>() {
//            @Override
//            public void onResponse(Call<com.app.kutumb.Model.Response> call, Response<com.app.kutumb.Model.Response> response) {
//                Toast.makeText(loginActivity,response.body().getMessage(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<com.app.kutumb.Model.Response> call, Throwable t) {
//                Log.d("TAG","VALUEDATA"+t);
//            }
//        });
    }

    public static void getAddOn(MainActivity mainActivity, ProgressDialog progressDialog,
                                RecyclerView recyclerViewAddOn, List<AddOnList> addOnList,
                                AddOnAdapter addOnAdapter, String outletId) {

        String tag_string_req="add_on_list";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_ON_LIST, response -> {
            Log.e(TAG, "add_on_list Response: " + response);
            progressDialog.dismiss();
            try {

                JSONObject jsonObject = new JSONObject(response);

                String status = jsonObject.getString("status");
                final String message = jsonObject.getString("message");

                if (status.equalsIgnoreCase("0")) {
                    JSONArray userList = jsonObject.getJSONArray("data");

                    // rlNoData.setVisibility(View.GONE);
                    recyclerViewAddOn.setVisibility(View.VISIBLE);
                    List<AddOnList> items = new Gson().fromJson(userList.toString(),
                            new TypeToken<List<AddOnList>>() {
                            }.getType());

                    addOnList.clear();
                    addOnList.addAll(items);
                    addOnAdapter.notifyDataSetChanged();

                    recyclerViewAddOn.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                    recyclerViewAddOn.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewAddOn.setAdapter(addOnAdapter);
                } else {
                    Configuration.openPopupUpDown(mainActivity, R.style.Dialod_UpDown, "error",
                            message);
                    //  rlNoData.setVisibility(View.VISIBLE);
                    recyclerViewAddOn.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "user_list Error: " + error.getMessage());

            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.OUTLET_ID,outletId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}
