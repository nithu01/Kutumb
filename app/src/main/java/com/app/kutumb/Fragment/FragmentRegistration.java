package com.app.kutumb.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.AppController;
import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static com.android.volley.VolleyLog.TAG;
import static com.app.kutumb.Activity.LoginActivity.frameLayout;
import static com.app.kutumb.Activity.LoginActivity.rlBottom;
import static com.app.kutumb.Activity.LoginActivity.scrollView;

public class FragmentRegistration extends Fragment implements View.OnClickListener {

    private ImageView imgBack;
    private LinearLayout lnForm,lnOtp;
    private EditText editTextFirstName,editTextLastName,editTextEmail,editTextMobile,editTextPassword,editTextOtp;
    private ImageView imgShowPass;
    private Button btnContinue,btnSubmit;
    private boolean showpass=false;
    private TextView txtEnterOtp;
    private ProgressDialog progressDialog;
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registration,container,false);
        progressDialog=new ProgressDialog(getActivity());
        session=new SessionManager(getActivity());

        imgBack=view.findViewById(R.id.img_back_registration);
        lnForm=view.findViewById(R.id.ln_form_regis);
        lnOtp=view.findViewById(R.id.ln_otp_regis);
        editTextFirstName=view.findViewById(R.id.edittext_first_name);
        editTextLastName=view.findViewById(R.id.edittext_last_name);
        editTextEmail=view.findViewById(R.id.edittext_email);
        editTextMobile=view.findViewById(R.id.edittext_mobile);
        editTextPassword=view.findViewById(R.id.edittext_password);
        imgShowPass=view.findViewById(R.id.img_showpass_regis);
        btnContinue=view.findViewById(R.id.btn_continue_registration);
        btnSubmit=view.findViewById(R.id.btn_otp_registration);
        txtEnterOtp=view.findViewById(R.id.txt_enter_otp);
        editTextOtp=view.findViewById(R.id.edittext_otp);
        btnSubmit.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
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
        if (v==imgShowPass){
            int start, end;
            if (showpass) {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                editTextPassword.setSelection(start, end);
                showpass = false;
                imgShowPass.setImageResource(R.drawable.hide);
            } else {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(null);
                editTextPassword.setSelection(start, end);
                showpass = true;
                imgShowPass.setImageResource(R.drawable.show);
            }
        }
        if (v==btnContinue){
            String firstName=editTextFirstName.getText().toString();
            String lastName=editTextLastName.getText().toString();
            String email=editTextEmail.getText().toString();
            String mobile=editTextMobile.getText().toString();
            String password=editTextPassword.getText().toString();

            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))) {
                if (firstName.isEmpty()) {
                    editTextFirstName.setError("Enter First Name");
                    editTextFirstName.requestFocus();
                } else if (lastName.isEmpty()) {
                    editTextLastName.setError("Enter Last Name");
                    editTextLastName.requestFocus();
                } else if (email.isEmpty()) {
                    editTextEmail.setError("Enter Email Id");
                    editTextEmail.requestFocus();
                } else if (!Configuration.isEmailValid(email)) {
                    editTextEmail.setError("Enter Valid Email Id");
                    editTextEmail.requestFocus();
                } else if (mobile.isEmpty()) {
                    editTextMobile.setError("Enter Mobile No");
                    editTextMobile.requestFocus();
                } else if (mobile.length() < 10 || mobile.length() > 10) {
                    editTextMobile.setError("Enter Valid Mobile No");
                    editTextMobile.requestFocus();
                } else if (password.isEmpty()) {
                    editTextPassword.setError("Enter Password");
                    editTextPassword.requestFocus();
                } else if (password.length()<=5){
                    editTextPassword.setError("Password should not be less then 6 characters");
                    editTextPassword.requestFocus();
                }else {
                    registration(firstName,lastName,email,mobile,password,"N");
                }
            }else {
                Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown,"internetError",
                        "No Internet Connectivity");
            }
        }if (v==btnSubmit){
            String otp=editTextOtp.getText().toString();
            String firstName=editTextFirstName.getText().toString();
            String lastName=editTextLastName.getText().toString();
            String email=editTextEmail.getText().toString();
            String mobile=editTextMobile.getText().toString();
            String password=editTextPassword.getText().toString();
            if (otp.isEmpty()){
                editTextOtp.setError("Enter Otp");
                editTextOtp.requestFocus();
            }else {
                verifyOtp(firstName,lastName,email,mobile,password,otp,"Y");
            }
        }
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
    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }
}
