package com.app.kutumb.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.app.kutumb.Adapter.CouponAdapter;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Interface.Apiinterface;
import com.app.kutumb.Model.CouponStatus;
import com.app.kutumb.Model.Datum;
import com.app.kutumb.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CouponActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ArrayList<String> arrayList;
    CouponAdapter couponAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        recyclerView=findViewById(R.id.recyclerview);
        arrayList=new ArrayList<String>();

        progressDialog = new ProgressDialog(CouponActivity.this);
        getCoupon();
    }

    private void getCoupon() {
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.CONSTANT).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface = retrofit.create(Apiinterface.class);
        Call<CouponStatus> call = apiinterface.getCoupon(Constant.APICODE_VALUE);
        call.enqueue(new Callback<CouponStatus>() {
            @Override
            public void onResponse(Call<CouponStatus> call, retrofit2.Response<CouponStatus> response) {
                progressDialog.dismiss();
                ArrayList<Datum> arrayList = new ArrayList<>();
                Datum datum=null;
                if(response.body().getStatus().equals("0")){
                    for(int i=0;i<response.body().getData().size();i++) {
                        datum=new Datum();
                        datum.setCode(response.body().getData().get(i).getCode());
                        arrayList.add(datum);

                    }

                }
                CouponAdapter rechargeTransaction = new CouponAdapter(getApplicationContext(), arrayList);
                recyclerView.setAdapter(rechargeTransaction);
            }

            @Override
            public void onFailure(Call<CouponStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CouponActivity.this, "Error" + t, Toast.LENGTH_SHORT).show();

            }

        });

    }


}
