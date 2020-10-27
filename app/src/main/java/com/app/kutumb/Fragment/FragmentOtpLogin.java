package com.app.kutumb.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.PrefManager;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Interface.Apiinterface;
import com.app.kutumb.Model.OtpConfirmResponse;
import com.app.kutumb.Model.UserData;
import com.app.kutumb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.volley.VolleyLog.TAG;
import static com.app.kutumb.Activity.LoginActivity.frameLayout;
import static com.app.kutumb.Activity.LoginActivity.rlBottom;
import static com.app.kutumb.Activity.LoginActivity.scrollView;

public class FragmentOtpLogin extends Fragment implements View.OnClickListener {

    private ImageView imgBack;
    private LinearLayout lnForm,lnOtp;
    private EditText editTextMobile,editTextOtp;
    private Button btnContinue,btnSubmit;
    private boolean showpass=false;
    private TextView txtEnterOtp;
    private ProgressDialog progressDialog;
    SessionManager session;
    String Otp,id,email,phone,fname,lname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_otp_login,container,false);
        progressDialog=new ProgressDialog(getActivity());
        session=new SessionManager(getActivity());

        imgBack=view.findViewById(R.id.img_back_registration);
        lnForm=view.findViewById(R.id.ln_form_regis);
        lnOtp=view.findViewById(R.id.ln_otp_regis);
        editTextMobile=view.findViewById(R.id.edittext_mobile);
        btnContinue=view.findViewById(R.id.btn_continue_registration);
        btnSubmit=view.findViewById(R.id.btn_otp_registration);
        txtEnterOtp=view.findViewById(R.id.txt_enter_otp);
        editTextOtp=view.findViewById(R.id.edittext_otp);
        btnSubmit.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        lnForm.setVisibility(View.VISIBLE);
        lnOtp.setVisibility(View.GONE);
        rlBottom.setVisibility(View.GONE);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {
                frameLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                rlBottom.setVisibility(View.VISIBLE);
                return true;
            }
            return true;
        });

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        if (v==imgBack){
            frameLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            rlBottom.setVisibility(View.VISIBLE);
        }
        if (v==btnContinue){
            String mobile=editTextMobile.getText().toString();

            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))) {
                 if (mobile.isEmpty()) {
                    editTextMobile.setError("Enter Mobile No");
                    editTextMobile.requestFocus();
                } else if (mobile.length() < 10 || mobile.length() > 10) {
                    editTextMobile.setError("Enter Valid Mobile No");
                    editTextMobile.requestFocus();
                }else {
                    SignIn(mobile);
                }
            }else {
                Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown,"internetError",
                        "No Internet Connectivity");
            }
        }if (v==btnSubmit){
            String otp=editTextOtp.getText().toString();
//            String mobile=editTextMobile.getText().toString();
            if (otp.isEmpty()){
                editTextOtp.setError("Enter Otp");
                editTextOtp.requestFocus();
            }else {
                if (otp.equals(Otp)){
                    session.setLogin(true);
                    UserData userData = new UserData(
                           id,email,phone,fname,lname);
                    PrefManager.getInstance(getActivity()).userLogin(userData);
                    Intent intent =new Intent(getActivity(), MainActivity.class);
                    //intent.putExtra(Constant.TYPE,type);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }else {
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "OTP is wrong");
                }
            }
        }
    }

    private void SignIn(String mobile) {
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.CONSTANT).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface = retrofit.create(Apiinterface.class);
                Call<OtpConfirmResponse> call = apiinterface.otp(Constant.APICODE_VALUE,"2",mobile);
                call.enqueue(new Callback<OtpConfirmResponse>() {
                    @Override
                    public void onResponse(Call<OtpConfirmResponse> call, retrofit2.Response<OtpConfirmResponse> response) {
                        progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Toast.makeText(getContext(), Otp, Toast.LENGTH_SHORT).show();
                    Otp = String.valueOf(response.body().getOtp());
                    lnForm.setVisibility(View.GONE);
                    lnOtp.setVisibility(View.VISIBLE);
                    id=response.body().getData().getId();
                    fname=response.body().getData().getFirstName();
                    lname=response.body().getData().getLastName();
                    phone=response.body().getData().getPhone();
                    email=response.body().getData().getEmail();
                }

            }
            @Override
            public void onFailure(Call<OtpConfirmResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error" + t, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void verifyOtp(String firstName,String lastName,String email,String mobile,
                           String password,String otp,String otpStatus) {
        String tag_string_req = "verify_otp";

        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        @SuppressLint("SetTextI18n")
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REGISTRATION, response -> {
            Log.d(TAG, "verify_otp Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jObj=new JSONObject(response);
                String status=jObj.getString("status");
                if (status.equalsIgnoreCase("0")){
                    frameLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    rlBottom.setVisibility(View.VISIBLE);
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"",
                            "Successfully Registered!!!\nPlease login with your credentials");

                }else {
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "Something went wrong");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "verify_otp Error: " + error.getMessage());
            Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown,"error",
                    "No Internet Connectivity or Something went wrong Try again after sometime");
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.FIRST_NAME, firstName);
                params.put(Constant.LAST_NAME,lastName);
                params.put(Constant.EMAIL, email);
                params.put(Constant.MOBILE, mobile);
                params.put(Constant.PASSWORD,password);
                params.put(Constant.OTP_STATUS,otpStatus);
                params.put(Constant.OTP,otp);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void registration(String firstName, String lastName, String email, String mobile, String password,String otpStatus) {
        String tag_string_req = "registration";

        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        @SuppressLint("SetTextI18n")
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REGISTRATION, response -> {
            Log.d(TAG, "registration Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jObj=new JSONObject(response);
                String status=jObj.getString("status");
                if (status.equalsIgnoreCase("0")){
                   // JSONObject jsonObject=jObj.getJSONObject("data");
                    lnForm.setVisibility(View.GONE);
                    lnOtp.setVisibility(View.VISIBLE);
                    txtEnterOtp.setText("Enter Otp Send to your mobile Number "+mobile);


                }else {
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This email already exists");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "registration Error: " + error.getMessage());
            Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown,"error",
                    "No Internet Connectivity or Something went wrong Try again after sometime");
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.API_CODE,Constant.APICODE_VALUE);
                params.put(Constant.FIRST_NAME,firstName);
                params.put(Constant.LAST_NAME,lastName);
                params.put(Constant.EMAIL,email);
                params.put(Constant.MOBILE,mobile);
                params.put(Constant.PASSWORD,password);
                params.put(Constant.OTP_STATUS,otpStatus);
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


//         OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(100,TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS).build();
//        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://dailyciouskitchen.com/app_api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
//        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
//        Call<CouponStatus> call=apiinterface.register(Constant.APICODE_VALUE,firstName,lastName,email,mobile,password,otpStatus);
//        call.enqueue(new Callback<CouponStatus>() {
//            @Override
//            public void onResponse(Call<CouponStatus> call, Response<CouponStatus> response) {
//                if (response.body().getStatus().equals(0)){
//                   // JSONObject jsonObject=jObj.getJSONObject("data");
//                    lnForm.setVisibility(View.GONE);
//                    lnOtp.setVisibility(View.VISIBLE);
//                    txtEnterOtp.setText("Enter Otp Send to your mobile Number "+mobile);
//
//
//                }else {
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
//                            response.body().getErrors());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CouponStatus> call, Throwable t) {
//                Toast.makeText(getContext(),"Failure"+t,Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
