package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Adapter.AddOnAdapter;
import com.app.kutumb.Adapter.BottomMenuHelper;
import com.app.kutumb.Adapter.CartAdapter;
import com.app.kutumb.Adapter.MostSellingItemsAdapter;
import com.app.kutumb.Adapter.OfferItemsAdapter;
import com.app.kutumb.Adapter.RequestListMenuAdapter;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.PrefManager;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Config.WebService;
import com.app.kutumb.Database.DataSource.AddressTableRepositories;
import com.app.kutumb.Database.Local.AddressDataSource;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.Local.Common;
import com.app.kutumb.Database.ModelDB.AddressTable;
import com.app.kutumb.Database.ModelDB.Cart;
import com.app.kutumb.Interface.Apiinterface;
import com.app.kutumb.Model.AddOnList;
import com.app.kutumb.Model.AddressModel;
import com.app.kutumb.Model.MenuItems;
import com.app.kutumb.Model.OfferItem;
import com.app.kutumb.Model.Response;
import com.app.kutumb.Model.UserData;
import com.app.kutumb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OfferItemsAdapter.OfferItemsAdapterListener,
        MostSellingItemsAdapter.MostSellingItemsAdapterListner,
        AddOnAdapter.AddOnAdapterListner {

    private static final String TAG = MainActivity.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recyclerViewOffer, cartRequestList, recyclerViewAddOn;
    public static RecyclerView requestListData, recyclerViewMostSelling;
    @SuppressLint("StaticFieldLeak")
    private static ViewGroup cartHiddenLayout;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtLocation;
    private ImageView imgShare, imgFilterSearch, imgCartClose;
    @SuppressLint("StaticFieldLeak")
    public static BottomNavigationView bottomNavigationView;
    @SuppressLint("StaticFieldLeak")
    static SwipeRefreshLayout swipeRefreshLayout;
    Button btnProceedPay;
    String address;
    JSONArray jsonArray;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    static ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    public static RequestListMenuAdapter requestListAdapter;
    ArrayList<String> menu_id = new ArrayList<>();
    ArrayList<String> menu_name = new ArrayList<>();
    ArrayList<String> menu_image_name = new ArrayList<>();
    ArrayList<String> items = new ArrayList<>();
    private static List<OfferItem> listData;
    @SuppressLint("StaticFieldLeak")
    private static OfferItemsAdapter loadListAdapter;
    private static List<AddOnList> addOnList;
    @SuppressLint("StaticFieldLeak")
    private static AddOnAdapter addOnAdapter;
    private SessionManager session;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout rlCart;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtItemCount, txtItemsPrice, txtViewCart;
    List<MenuItems> mostSellinglistData;
    @SuppressLint("StaticFieldLeak")
    public static MostSellingItemsAdapter mostSellingloadListAdapter;
    Switch switchVegNonVeg;
    String type = "";
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    TextView txtRecom;
    private ImageView imgFilterSer;
    UserData userData;
    @SuppressLint("StaticFieldLeak")
    static CartAdapter adapterCart;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtItemTotal, txtCharges, txtDiscount, txtDeliveryFee, txtPay,
            txtDeliveryName, txtPlace, txtTimeDelivery, txtTotalAmount, txtAddAddress,
            txtAmountToPay, txtAddressFinal, txtTimeFinalDelivery;
    private Button btnPlaceOrder;
    @SuppressLint("StaticFieldLeak")
    public static RadioButton rbBtnPay;
    RelativeLayout rlMain, rlBilldetails, rlPayment, rlMostSelling, rlCartDetails, rlNoDataCart;
    NestedScrollView nestedScrollView;
    String PG_ORDER_ID = "";
    String amount = "";

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);

//        customProgressBar=new CustomProgressBar();
        rlMain = findViewById(R.id.rl_main);
        recyclerViewAddOn = findViewById(R.id.recyclerview_addon);
        rlCartDetails = findViewById(R.id.rl_cart_details_main);
        rlNoDataCart = findViewById(R.id.rl_no_cart_data);
        btnProceedPay = findViewById(R.id.btn_proceed_pay);
        rlMostSelling = findViewById(R.id.rl_most_selling);
        rlCart = findViewById(R.id.rl_cart_item);
        txtItemTotal = findViewById(R.id.txt_item_total);
        txtCharges = findViewById(R.id.txt_charges);
        txtDiscount = findViewById(R.id.txt_discount);
        txtPay = findViewById(R.id.txt_to_pay);
        txtDeliveryFee = findViewById(R.id.txt_delivery_fee);
        txtDeliveryName = findViewById(R.id.txt_delivery_name);
        txtPlace = findViewById(R.id.txt_place_delivery);
        txtTimeDelivery = findViewById(R.id.txt_time_delivery);
        txtTotalAmount = findViewById(R.id.txt_total_amount);
        txtAddAddress = findViewById(R.id.txt_address_delivery);
        rlBilldetails = findViewById(R.id.rl_bill_detail);
        nestedScrollView = findViewById(R.id.nestedscrollview_cart);
        txtItemCount = findViewById(R.id.txt_item_count);
        txtItemsPrice = findViewById(R.id.txt_item_price);

        txtViewCart = findViewById(R.id.txt_view_cart);
        txtRecom = findViewById(R.id.txt_recommended);
        imgFilterSer = findViewById(R.id.img_filter);

        rlPayment = findViewById(R.id.rl_payment);
        txtAmountToPay = findViewById(R.id.txt_amt_final);
        txtAddressFinal = findViewById(R.id.txt_address_final);
        rbBtnPay = findViewById(R.id.rb_btn_payment);
        txtTimeFinalDelivery = findViewById(R.id.txt_time_final);
        btnPlaceOrder = findViewById(R.id.btn_place_order);
        btnPlaceOrder.setOnClickListener(this);
        linearLayout = findViewById(R.id.linear_egg);
        cartRequestList = findViewById(R.id.recyclerview_cart);
        activity = MainActivity.this;
        session = new SessionManager(MainActivity.this);

        requestListData = findViewById(R.id.recyclerView_data);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_layout);

        imgShare = findViewById(R.id.img_share_app);

        imgFilterSearch = findViewById(R.id.img_filter_search);
        recyclerViewOffer = findViewById(R.id.recyclerview_offer);

        txtLocation = findViewById(R.id.txt_location);
        //  address = getIntent().getStringExtra("address");
        // txtLocation.setText(SplashActivity.getPreferences("address",""));
        txtViewCart.setOnClickListener(this);
        cartHiddenLayout = findViewById(R.id.hidden_panel_cart);

        imgCartClose = findViewById(R.id.img_back_cart);
        imgCartClose.setOnClickListener(this);
        relativeLayout = findViewById(R.id.rl_coupon);
        relativeLayout.setOnClickListener(this);

        bottomNavigationView = findViewById(R.id.bottomBar);
        txtLocation.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgFilterSearch.setOnClickListener(this);
        txtAddAddress.setOnClickListener(this);
        rlBilldetails.setOnClickListener(this);

        btnProceedPay.setOnClickListener(this);
        swipeRefreshLayout.setEnabled(false);
        listData = new ArrayList<>();
        loadListAdapter = new OfferItemsAdapter(MainActivity.this, listData, this);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        recyclerViewMostSelling = findViewById(R.id.recyclerview_most_selling);
        mostSellinglistData = new ArrayList<>();
        mostSellingloadListAdapter = new MostSellingItemsAdapter(MainActivity.this, mostSellinglistData, this);

        addOnList = new ArrayList<>();
        addOnAdapter = new AddOnAdapter(MainActivity.this, addOnList, this);

        switchVegNonVeg = findViewById(R.id.switch_veg_nonveg);
        type = "";
        getData(type);
        switchVegNonVeg.setChecked(false);
        switchVegNonVeg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchVegNonVeg.isChecked()) {
                type = "1";
                linearLayout.setVisibility(View.VISIBLE);
                getData(type);
            } else {
                type = "";
                linearLayout.setVisibility(View.GONE);
                getData(type);
            }
        });
        imgFilterSer.setOnClickListener(this);

        try {
            String addTitle = Common.addressRepository.nameAddress();
            String count = Common.cartRepository.getQuantity();
            if (TextUtils.isEmpty(addTitle)) {
                txtLocation.setText(SplashActivity.getPreferences("address", ""));
            } else {
                txtLocation.setText("Deliver to : " + addTitle);
            }
            if (Integer.valueOf(count) > 0) {
                BottomMenuHelper.showBadge(this, bottomNavigationView,
                        R.id.navigation_orders, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Configuration.hasNetworkConnection(MainActivity.this)) {
            getOffers();
            getMostSelling();

        } else {
            Configuration.openPopupUpDown(MainActivity.this, R.style.Dialod_UpDown, "internetErrorm1",
                    "No Internet Connectivitym1" +
                            ", Thanks");
        }
        try {
            BottomMenuHelper.removeBadge(bottomNavigationView, R.id.navigation_orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userData = PrefManager.getInstance(MainActivity.this).getUserData();
        } catch (Exception e) {

        }
        try {
            if (getIntent().getStringExtra("address").equals("address")) {
                slideUpCart();
                getCartData();
                nestedScrollView.setVisibility(View.VISIBLE);
                rlCartDetails.setVisibility(View.VISIBLE);
                rlNoDataCart.setVisibility(View.GONE);
                txtItemTotal.setText("₹" + Common.cartRepository.getCost());
                txtCharges.setText("₹" + "0.00");
                txtDiscount.setText("₹" + "0.00");
                txtDeliveryFee.setText("₹" + "0.00");
                amount=Common.cartRepository.getCost();
                txtPay.setText("₹" + Float.valueOf(Common.cartRepository.getCost()));
                txtTimeDelivery.setText("40 MINT");
                //txtDeliveryName.setText("Address not available");
                //  Log.e(TAG,"ADDress-->"+Common.addressRepository.nameAddress());
                if (Common.addressRepository.nameAddress() == null || TextUtils.isEmpty(Common.addressRepository.nameAddress())) {
                    txtDeliveryName.setText("Address not available");
                } else {
                    txtDeliveryName.setText("Deliver to: " + Common.addressRepository.nameAddress());
                }
                txtPlace.setVisibility(View.GONE);
                txtTotalAmount.setText(txtPay.getText().toString());
                txtDeliveryName.setText(SplashActivity.getPreferences("city", "") + "\n" + SplashActivity.getPreferences("pincode", ""));
            }
        } catch (Exception e) {

        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("SetTextI18n")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_search:
                    Intent intent = new Intent(MainActivity.this, FilterSearchActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    return true;
                case R.id.navigation_orders:
                    try {
                        Common.addressRepository = AddressTableRepositories.getInstance(AddressDataSource.getInstance(Common.cartDatabase.addressTableDao()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    slideUpCart();
                    getCartData();
                    BottomMenuHelper.removeBadge(bottomNavigationView, R.id.navigation_orders);

                    if (TextUtils.isEmpty(Common.cartRepository.getCost()) || Common.cartRepository.getCost() == null) {
                        Toast.makeText(MainActivity.this, Common.cartRepository.getCost(), Toast.LENGTH_LONG).show();
                        nestedScrollView.setVisibility(View.GONE);
                        rlCartDetails.setVisibility(View.GONE);
                        rlNoDataCart.setVisibility(View.VISIBLE);
                    } else {
                        nestedScrollView.setVisibility(View.VISIBLE);
                        rlCartDetails.setVisibility(View.VISIBLE);
                        rlNoDataCart.setVisibility(View.GONE);
                        txtItemTotal.setText("₹" + Common.cartRepository.getCost());
                        txtCharges.setText("₹" + "0.00");
                        txtDiscount.setText("₹" + "0.00");
                        txtDeliveryFee.setText("₹" + "0.00");
                        amount = Common.cartRepository.getCost();
                        txtPay.setText("₹" + Float.valueOf(Common.cartRepository.getCost()));
//                        txtTimeDelivery.setText("40 MINT");
                        //txtDeliveryName.setText("Address not available");
                        Log.e(TAG, "ADDress-->" + Common.addressRepository.nameAddress());
                        if (Common.addressRepository.nameAddress() == null || TextUtils.isEmpty(Common.addressRepository.nameAddress())) {
                            txtDeliveryName.setText("Address not available");
                        } else {
                            txtDeliveryName.setText("Deliver to: " + Common.addressRepository.nameAddress());
                        }
                        txtPlace.setVisibility(View.GONE);
                        txtTotalAmount.setText(txtPay.getText().toString());
                    }
                    return true;

                case R.id.navigation_profile:
                    if (session.isLoggedIn()) {
                        Intent inten = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(inten);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        Intent inten = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(inten);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                    return true;
            }
            return false;
        }
    };

//    private void doTask() {
//       //getMenuList();
//        getData(type);
//        getOffers();
//        swipeRefreshLayout.setRefreshing(false);
//    }

/* private void prepareMovieData() {
        ItemData movie = new ItemData(R.drawable.one);
        movieList.add(movie);

        movie = new ItemData(R.drawable.two);
        movieList.add(movie);

        movie = new ItemData(R.drawable.three);
        movieList.add(movie);

        movie = new ItemData(R.drawable.four);
        movieList.add(movie);

        movie = new ItemData(R.drawable.five);
        movieList.add(movie);

        movie = new ItemData(R.drawable.two);
        movieList.add(movie);

        movie = new ItemData(R.drawable.three);
        movieList.add(movie);

        movie = new ItemData(R.drawable.four);
        movieList.add(movie);

        movie = new ItemData(R.drawable.five);
        movieList.add(movie);

        mAdapter.notifyDataSetChanged();
    }*/

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        Common.addressRepository = AddressTableRepositories.getInstance(AddressDataSource.getInstance(Common.cartDatabase.addressTableDao()));
        if (v == btnProceedPay) {
            progressDialog.show();
            getCartItems();
//            order();
//            if (Common.addressRepository.nameAddress()==null||TextUtils.isEmpty(Common.addressRepository.nameAddress())) {
//               // txtDeliveryName.setText("Address not available");
//                Toast.makeText(activity,"Select Delivery Address", Toast.LENGTH_SHORT).show();
//
//            } else {
//                List<AddressTable> address=Common.addressRepository.getAddress(Common.addressRepository.nameAddress());
//                for (int i=0;i<address.size();i++){
//                    AddressTable t = address.get(i);
//                    txtAddressFinal.setText(t.address_name+"\n"+t.first_name+" "
//                            +t.last_name+"\n"+t.getAddress_line_one()
//                            +" "+t.getAddress_line_two()+"\n"
//                    if(txtDeliveryName.equ){
////
////                getCartItems();
////               // txtDeliveryName.setText("Deliver to: "+Common.addressRepository.nameAddress());
////            }          +t.getCity()+"-"+t.getPincode());
//                }
//                rlPayment.setVisibility(View.VISIBLE);
//                rlBilldetails.setVisibility(View.GONE);
//                txtAmountToPay.setText("₹"+Common.cartRepository.getCost());
//
        }
        if (v == btnPlaceOrder) {
            if (Common.addressRepository.nameAddress() == null || TextUtils.isEmpty(Common.addressRepository.nameAddress())) {
                txtDeliveryName.setText("Address not Available");
                Toast.makeText(activity, "Select Delivery Address", Toast.LENGTH_SHORT).show();
            } else {
                // txtDeliveryName.setText("Deliver to: "+Common.addressRepository.nameAddress());
                progressDialog.setMessage("Please wait...\nBooking in process");
                progressDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, BookingStatus.class);
                    intent.putExtra(Constant.AMOUNT, txtAmountToPay.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }, 3000);
            }
        }
        if (v == txtLocation) {
            if (session.isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            } else {
                Configuration.openPopupUpDownBack(MainActivity.this, R.style.Dialod_UpDown, "login", "error",
                        "Please login/signup first to get order at your door", "");
            }
            //overridePendingTransition(R.anim.slide_in_top,R.anim.slide_from_top);
        }
        if (v == txtAddAddress) {
            Intent intent = new Intent(MainActivity.this, AddressActivity.class);
            startActivity(intent);
        }
        if (v == relativeLayout) {
            Toast.makeText(activity, "data", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CouponActivity.class);
            startActivity(intent);
        }
        if (v == txtViewCart) {
            rlNoDataCart.setVisibility(View.GONE);
            rlCartDetails.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
            slideUpCart();
            getCartData();
            getAddressData(userData.getId());
            txtItemTotal.setText("₹" + Common.cartRepository.getCost());
            txtCharges.setText("₹" + "0.00");
            txtDiscount.setText("₹" + "0.00");
            txtDeliveryFee.setText("₹" + "0.00");
            txtPay.setText("₹" + Float.valueOf(Common.cartRepository.getCost()));
            txtTimeDelivery.setText("40 MINT");
            amount=Common.cartRepository.getCost();
            WebService.getAddOn(MainActivity.this, progressDialog, recyclerViewAddOn,
                    addOnList, addOnAdapter, SplashActivity.getPreferences(Constant.OUTLET_ID, ""));
            //txtDeliveryName.setText("Address not available");
            Log.e(TAG, "ADDress-->" + Common.addressRepository.nameAddress());
            if (Common.addressRepository.nameAddress() == null) {
                txtDeliveryName.setText("Address not available");
            } else {
                txtDeliveryName.setText("Deliver to: " + Common.addressRepository.nameAddress());
            }
            txtPlace.setVisibility(View.GONE);
            txtTotalAmount.setText(txtPay.getText().toString());
        }
        if (v == rlBilldetails) {
            nestedScrollView.fullScroll(View.FOCUS_DOWN);
        }
        if (v == imgCartClose) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            closeCart(progressDialog);
            bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        }
        if (v == imgFilterSer) {
            Intent intent = new Intent(MainActivity.this, FilterSearchActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        if (v == imgShare) {
            try {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, "Hello, this is Restro App please install app from link: " + "");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (v == imgFilterSearch) {
            Intent intent = new Intent(MainActivity.this, FilterSearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

    }

    public static void getCartData() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

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
                //₹
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(List<Cart> carts) {
                super.onPostExecute(carts);
                adapterCart = new CartAdapter(activity, carts);
                cartRequestList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                cartRequestList.setItemAnimator(new DefaultItemAnimator());
                cartRequestList.setAdapter(adapterCart);
                adapterCart.notifyDataSetChanged();
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public static void closeCart(ProgressDialog progressDialog) {
        if (Common.cartRepository.getQuantity() != null) {
            BottomMenuHelper.showBadge(activity, bottomNavigationView,
                    R.id.navigation_orders, Common.cartRepository.getQuantity());
        }
        final Handler handler = new Handler();
        requestListData.setAdapter(requestListAdapter);
        recyclerViewMostSelling.setAdapter(mostSellingloadListAdapter);
        requestListAdapter.notifyDataSetChanged();
        mostSellingloadListAdapter.notifyDataSetChanged();
        handler.postDelayed(progressDialog::dismiss, 3000);
        Animation bottomDown = AnimationUtils.loadAnimation(activity,
                R.anim.slide_bottom_dialog);

        cartHiddenLayout.startAnimation(bottomDown);
        cartHiddenLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        mostSellingloadListAdapter.notifyDataSetChanged();
        bottomNavigationView.setVisibility(View.VISIBLE);
        rlCart.setVisibility(View.GONE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            String count = Common.cartRepository.getQuantity();
            if (Integer.valueOf(count) > 0 && count != null) {
                BottomMenuHelper.showBadge(activity, bottomNavigationView,
                        R.id.navigation_orders, count);
            }
            /*final Handler handler = new Handler();
            handler.postDelayed(() -> {
                MainActivity.requestListData.setAdapter(requestListAdapter);
                MainActivity.recyclerViewMostSelling.setAdapter(mostSellingloadListAdapter);
                requestListAdapter.notifyDataSetChanged();
                mostSellingloadListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }, 3000);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String count = Common.cartRepository.getQuantity();
            if (Integer.valueOf(count) > 0 && count != null) {
                BottomMenuHelper.showBadge(activity, bottomNavigationView,
                        R.id.navigation_orders, count);
            }

            /*final Handler handler = new Handler();
            handler.postDelayed(() -> {
                MainActivity.requestListData.setAdapter(requestListAdapter);
                MainActivity.recyclerViewMostSelling.setAdapter(mostSellingloadListAdapter);
                requestListAdapter.notifyDataSetChanged();
                mostSellingloadListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }, 3000);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void slideUpCart() {
        if (!isPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up_dialog);

            cartHiddenLayout.startAnimation(bottomUp);
            cartHiddenLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            getCartData();
        }/* else {
            // Hide the Panel
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.hideKeyboardFrom(MainActivity.this);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_bottom_dialog);

            cartHiddenLayout.startAnimation(bottomDown);
            cartHiddenLayout.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
          //  getAddressTableData();
            getCartData();
        }*/
    }

    private boolean isPanelShown() {
        return cartHiddenLayout.getVisibility() == View.VISIBLE;
    }

/*private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }*/

    @Override
    public void onBackPressed() {

        if (rlPayment.isShown()) {
            rlPayment.setVisibility(View.GONE);
            rlBilldetails.setVisibility(View.VISIBLE);
        } else if (isPanelShown()) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            closeCart(progressDialog);
            bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(Intent.ACTION_MAIN);
                        launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                        launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchNextActivity);
                        finish();
                    }).create().show();
        }
    }
    /*public void createDummyData(String jArr, int length) {
        ArrayList<CategoryItem> singleItem = new ArrayList<>();
        Log.e(TAG, "JARRAY Item Length: " + jArr.length()+"\n"+ jArr);
        try {
            for (int i=0;i<length;i++) {
                JSONArray jsonArray=new JSONArray(jArr);
                for (int j = 0; j <= jsonArray.length(); j++) {
                    JSONObject jItem = jsonArray.getJSONObject(j);
                    String item = jItem.getString("item_name");
                    String url = jItem.getString("item_image_name");
                    String desc = jItem.getString("item_description");
                    String cost = jItem.getString("item_cost");
                    singleItem.add(new CategoryItem(item,
                            url,
                            desc,
                            cost));

                }
            }
            dm.setAllItemsInSection(singleItem);
            allSampleData.add(dm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    private void getData(String type) {

        String tag_string_req = "fund_req";
        menu_id.clear();
        menu_image_name.clear();
        menu_name.clear();
        items.clear();

        try {
            Configuration.hideKeyboardFrom(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_MENU,
                response -> {
//                    customProgressBar.dialog.dismiss();
                    Log.d(TAG, "RESPONSE" + response + "\n");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("status");
                        if (responseCode.equals("0")) {
                            requestListData.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayObject = jsonObject.getJSONArray("data");
                            if (jsonArrayObject.isNull(0)) {
                               /* rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");*/
                                requestListData.setVisibility(View.GONE);
                            } else {
                                //  rlNoData.setVisibility(View.GONE);
                                requestListData.setVisibility(View.VISIBLE);
                                if (type.equals("1")) {
                                    recyclerViewMostSelling.setVisibility(View.GONE);
                                    requestListData.setVisibility(View.VISIBLE);
                                    txtRecom.setVisibility(View.GONE);
                                } else {
                                    recyclerViewMostSelling.setVisibility(View.VISIBLE);
                                    requestListData.setVisibility(View.VISIBLE);
                                    txtRecom.setVisibility(View.VISIBLE);
                                }
                                for (int i = 0; i < jsonArrayObject.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayObject.getJSONObject(i);
                                    if (jsonObject1.has("items")) {
                                        menu_id.add(jsonObject1.getString("menu_id"));
                                        menu_name.add(jsonObject1.getString("menu_name"));
                                        menu_image_name.add(jsonObject1.getString("menu_image_name"));
                                        items.add(jsonObject1.getString("items"));
                                    }
                                }

                                requestListAdapter = new RequestListMenuAdapter(MainActivity.this,
                                        menu_id, menu_name, menu_image_name, items);
                                requestListData.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
                                requestListData.setAdapter(requestListAdapter);
                                requestListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            /*rlNoData.setVisibility(View.VISIBLE);
                            imgData.setImageResource(R.drawable.norequest);
                            txtData.setText("No Transaction");*/
                            requestListData.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                       /* rlNoData.setVisibility(View.VISIBLE);
                        imgData.setImageResource(R.drawable.norequest);
                        txtData.setText("No Transaction");*/
                        requestListData.setVisibility(View.GONE);
                        Configuration.openPopupUpDown(MainActivity.this, R.style.Dialod_UpDown, "internetError",
                                "No Internet connectivity" +
                                        ", Thanks");
                    }
//                    customProgressBar.dialog.dismiss();
                },
                error -> {
//                    customProgressBar.dialog.dismiss();
                    Log.e("PASS ERROR", "ERROR--->" + error.toString());
                    Configuration.openPopupUpDown(MainActivity.this, R.style.Dialod_UpDown, "internetError",
                            "No Internet connectivitym2" +
                                    ", Thanks");
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE, Constant.APICODE_VALUE);
                params.put(Constant.ITEM_TYPE, type);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getOffers() {
        String tag_string_req = "fund_req";

        listData.clear();
        // Configuration.showDialog("Please wait...",progressDialog);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_OFFERS,
                response -> {

                    Log.d(TAG, "Recharge RESPONSED" + response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)) {

                                recyclerViewOffer.setVisibility(View.GONE);
                            } else {
                                recyclerViewOffer.setVisibility(View.VISIBLE);
                                List<OfferItem> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<OfferItem>>() {
                                        }.getType());

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();

                                // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                                recyclerViewOffer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                recyclerViewOffer.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewOffer.setAdapter(loadListAdapter);

                            }
                        } else {
                            recyclerViewOffer.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        recyclerViewOffer.setVisibility(View.GONE);
                    }

                    progressDialog.dismiss();
                },
                error -> progressDialog.dismiss()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE, Constant.APICODE_VALUE);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public void onOfferItemsAdapterSelected(OfferItem offerItem) {

    }

    public void getMostSelling() {
        String tag_string_req = "most_selling";

        Configuration.showDialog("Please wait...", progressDialog);
        try {
            Configuration.hideKeyboardFrom(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_MOST_SELLING,
                response -> {
                    Log.e(TAG, "response_most_selling-->" + "\n" + response + "\n");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("status");
                        if (responseCode.equals("0")) {
                            //  rlNoData.setVisibility(View.GONE);
                            //  recyclerViewMostSelling.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayObject = jsonObject.getJSONArray("data");
                            if (jsonArrayObject.isNull(0)) {
                               /* rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");*/
                                recyclerViewMostSelling.setVisibility(View.GONE);
                                rlMostSelling.setVisibility(View.GONE);
                            } else {
                                //  rlNoData.setVisibility(View.GONE);
                                recyclerViewMostSelling.setVisibility(View.VISIBLE);
                                rlMostSelling.setVisibility(View.VISIBLE);
                                List<MenuItems> items = new Gson().fromJson(jsonArrayObject.toString(),
                                        new TypeToken<List<MenuItems>>() {
                                        }.getType());

                                mostSellinglistData.clear();
                                mostSellinglistData.addAll(items);
                                mostSellingloadListAdapter.notifyDataSetChanged();
                                recyclerViewMostSelling.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                        RecyclerView.VERTICAL, true));
                                recyclerViewMostSelling.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewMostSelling.setAdapter(mostSellingloadListAdapter);
                                mostSellingloadListAdapter.notifyDataSetChanged();
                            }
                        } else {
                           /* rlNoData.setVisibility(View.VISIBLE);
                            imgData.setImageResource(R.drawable.norequest);
                            txtData.setText("No Transaction");*/
                            recyclerViewMostSelling.setVisibility(View.GONE);
                            rlMostSelling.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                       /* rlNoData.setVisibility(View.VISIBLE);
                        imgData.setImageResource(R.drawable.norequest);
                        txtData.setText("No Transaction");*/
                        recyclerViewMostSelling.setVisibility(View.GONE);
                        rlMostSelling.setVisibility(View.GONE);
                        Configuration.openPopupUpDown(MainActivity.this, R.style.Dialod_UpDown, "internetError",
                                "No Internet connectivity" +
                                        ", Thanks");
                    }
                    progressDialog.dismiss();
                },
                error -> {
                    progressDialog.dismiss();
                    Log.e("PASS ERROR", "ERROR--->" + error.toString());
                    Configuration.openPopupUpDown(MainActivity.this, R.style.Dialod_UpDown, "internetError",
                            "No Internet connectivitym3" +
                                    ", Thanks");
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE, Constant.APICODE_VALUE);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public void onMenuItemsAdapterSelcted(MenuItems menuItems) {

    }

    @Override
    public void onAddOnAdapterSelcted(MenuItems menuItems) {

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
                jsonArray = new JSONArray();
                for (int i = 0; i < carts.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("productid", carts.get(i).getItem_id());
                        jsonObject.put("productname", carts.get(i).getItem_cost());
                        jsonObject.put("productprice", carts.get(i).getItem_cost());
                        jsonObject.put("offerprice", carts.get(i).getQuantity());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                order();
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void order() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.CONSTANT).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface = retrofit.create(Apiinterface.class);
        Call<Response> call = apiinterface.order(Constant.APICODE_VALUE, userData.getId(), SplashActivity.getPreferences("house", ""), SplashActivity.getPreferences("street", ""), SplashActivity.getPreferences("street", ""), SplashActivity.getPreferences("pincode", ""), SplashActivity.getPreferences("city", ""), SplashActivity.getPreferences("city", ""), "27.123456", "28.3123456", "12345", jsonArray.toString(), txtAmountToPay.getText().toString(), "COD");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
//                Configuration.openPopupUpDownBack(MainActivity.this, R.style.Dialod_UpDown, "main", "success",
//                        response.body().getMessage(), "");
                if(response.body().getMessage().trim().equals("Order is placed successfully"))
                pay();
//                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("TAG", "VALUEDATA" + t);

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
                                txtAddAddress.setText("Add Address");
//                                rlAddressList.setVisibility(View.GONE);
//                                scrollViewAddress.setVisibility(View.VISIBLE);
                            } else {
                                txtDeliveryName.setText("Select Address");
                                txtAddAddress.setText("Select Address");
//                                rlAddressList.setVisibility(View.VISIBLE);
//                                scrollViewAddress.setVisibility(View.GONE);
//
//                                List<AddressModel> items = new Gson().fromJson(jsonArray.toString(),
//                                        new TypeToken<List<AddressModel>>() {
//                                        }.getType());
//
//                                listData.clear();
//                                listData.addAll(items);
//                                loadListAdapter.notifyDataSetChanged();
//
//                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddressActivity.this);
//                                recyclerViewAddressList.setLayoutManager(mLayoutManager);
//                                recyclerViewAddressList.setItemAnimator(new DefaultItemAnimator());
//                                recyclerViewAddressList.setAdapter(loadListAdapter);
                            }

                        } else {
                            txtAddAddress.setText("Add Address");
//                            rlAddressList.setVisibility(View.GONE);
//                            scrollViewAddress.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                    assert progressDialog != null;
                    progressDialog.dismiss();
                    Configuration.openPopupUpDown(MainActivity.this, R.style.Dialod_UpDown,
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

    public void pay() {
       // Toast.makeText(MainActivity.this,""+amount,Toast.LENGTH_SHORT).show();
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        PG_ORDER_ID = "LIVE" + Integer.toString(n);

        PaymentParams pgPaymentParams = new PaymentParams();
        pgPaymentParams.setAPiKey("74505ade-1af0-4c03-9377-13d189a0c287");
        pgPaymentParams.setAmount("100");
        pgPaymentParams.setEmail(userData.getEmail());
        pgPaymentParams.setName(userData.getFirst_name());
        pgPaymentParams.setPhone(userData.getPhone());
        pgPaymentParams.setOrderId(PG_ORDER_ID);
        pgPaymentParams.setCurrency("INR");
        pgPaymentParams.setDescription("Kutumb App");
        pgPaymentParams.setCity("New Delhi");
        pgPaymentParams.setState("State");
        pgPaymentParams.setAddressLine1("XXXXXXXXXXXXXX");
        pgPaymentParams.setAddressLine2("Delhi");
        pgPaymentParams.setZipCode("401107");
        pgPaymentParams.setCountry("India");
        pgPaymentParams.setReturnUrl("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        pgPaymentParams.setMode("LIVE");
        pgPaymentParams.setUdf1("");
        pgPaymentParams.setUdf2("");
        pgPaymentParams.setUdf3("");
        pgPaymentParams.setUdf4("");
        pgPaymentParams.setUdf5("");
        pgPaymentParams.setEnableAutoRefund("n");
        pgPaymentParams.setOfferCode("testcoupon");
        //pgPaymentParams.setSplitInfo("{\"vendors\":[{\"vendor_code\":\"24VEN985\",\"split_amount_percentage\":\"20\"}]}");

        PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, MainActivity.this);
        pgPaymentInitialzer.initiatePaymentProcess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String paymentResponse = data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: " + paymentResponse);
                    if (paymentResponse.equals("null")) {
                        System.out.println("Transaction Error!");
//                        transactionIdView.setText("Transaction ID: NIL");
//                        transactionStatusView.setText("Transaction Status: Transaction Error!");
                    } else {
                        JSONObject response = new JSONObject(paymentResponse);
//                        transactionIdView.setText("Transaction ID: "+response.getString("transaction_id"));
//                        transactionStatusView.setText("Transaction Status: "+response.getString("response_message"));
                       // order(response.getString("transaction_id"), "S");
                                        Configuration.openPopupUpDownBack(MainActivity.this, R.style.Dialod_UpDown, "main", "success",
                        "Order Placed Successfully", "");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }
    public void order(String orderid,String text){
        final Dialog dialg = new Dialog(this);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_recharge);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        TextView message=dialg.findViewById(R.id.message);
        TextView order_id=dialg.findViewById(R.id.order_id);
        TextView thankyou_message=dialg.findViewById(R.id.txt_status_recharge);
        ImageView imageView = dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus = dialg.findViewById(R.id.txt_status);
        Button close = dialg.findViewById(R.id.btn_okay);

        if (text.equalsIgnoreCase("S") ) {
            message.setText("Order Successfull");
            imageView.setImageResource(R.drawable.success);
            order_id.setText("Order Id:"+orderid);
            txtStatus.setText("Success");
            thankyou_message.setText("Thank You for your order");
        }else{
            message.setText("Something went wrong");
            imageView.setImageResource(R.drawable.failed);
            txtStatus.setText("Please try again");
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(status.equalsIgnoreCase("S")) {
//                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//                }else{
//                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//                }
            }
        });
        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
}
