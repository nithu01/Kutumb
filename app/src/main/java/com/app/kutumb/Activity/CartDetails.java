package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Adapter.CartAdapter;
import com.app.kutumb.Database.DataSource.CartRepository;
import com.app.kutumb.Database.Local.CartDataSource;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Database.ModelDB.Cart;
import com.app.kutumb.R;

import java.util.List;

public class CartDetails extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgBack;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView requestList;
    TextView coupon;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    RelativeLayout relativeLayout;
    Button btn_proceed_pay;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        coupon=findViewById(R.id.txt_coupon);
        btn_proceed_pay=findViewById(R.id.btn_proceed_pay);

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartDetails.this, "Intent", Toast.LENGTH_SHORT).show();
            }
        });
        imgBack=findViewById(R.id.img_back_cart);
        requestList=findViewById(R.id.recyclerview_cart);
        imgBack.setOnClickListener(this);
        relativeLayout=findViewById(R.id.rl_coupon);
        relativeLayout.setOnClickListener(this);
        activity=CartDetails.this;
        initDB();
       // try{
            if (TextUtils.isEmpty(Common.cartRepository.getCost())){
                MainActivity.txtDeliveryName.setText("Address Not Available");
            }else {
                MainActivity.txtDeliveryName.setText("Delivery to : "+Common.cartRepository.getCost());
            }
            MainActivity.txtDeliveryName.setAllCaps(true);
      /*  }catch (Exception e){
            e.printStackTrace();
        }*/
        btn_proceed_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getCartItems();
            }
        });
    }

    private void initDB() {
        Common.cartDatabase = CartDatabase.getInstance(CartDetails.this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
       // Common.cartRepository.emptyCart();
        getCartData();
    }
    public void getCartItems() {

        @SuppressLint("StaticFieldLeak")
        class GetTasks extends AsyncTask<Void, Void, List<Cart>> {

            @SuppressLint("WrongThread")
            @Override
            protected List<Cart> doInBackground(Void... voids) {

                // Cart t =new Cart();

                return CartDatabase
                        .getInstance(activity)
                        .cartDAO()
                        .getCartItems();
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(List<Cart> carts) {
                super.onPostExecute(carts);
                for (int i = 0; i < carts.size(); i++) {
                    Toast.makeText(CartDetails.this,carts.size(),Toast.LENGTH_SHORT).show();
                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
    public static void getCartData() {

        @SuppressLint("StaticFieldLeak")
        class GetTasks extends AsyncTask<Void, Void, List<Cart>> {

            @SuppressLint("WrongThread")
            @Override
            protected List<Cart> doInBackground(Void... voids) {

                // Cart t =new Cart();

                return CartDatabase
                        .getInstance(activity)
                        .cartDAO()
                        .getCartItems();
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(List<Cart> carts) {
                super.onPostExecute(carts);
                CartAdapter adapter = new CartAdapter(activity, carts);
                requestList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                requestList.setItemAnimator(new DefaultItemAnimator());
                requestList.setAdapter(adapter);
                requestList.setVisibility(View.GONE);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            finish();
        }
        if(v==relativeLayout){
           // Toast.makeText(activity, "data", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,CouponActivity.class);
            startActivity(intent);
        }
    }
}
