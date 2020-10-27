package com.app.kutumb.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.app.kutumb.Activity.LoginActivity;
import com.app.kutumb.Config.AppConfig;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Interface.Apiinterface;
import com.app.kutumb.Model.Response;
import com.app.kutumb.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.kutumb.Activity.LoginActivity.frameLayout;
import static com.app.kutumb.Activity.LoginActivity.rlBottom;
import static com.app.kutumb.Activity.LoginActivity.scrollView;

public class FragmentFgtPassword extends Fragment implements View.OnClickListener {

    EditText mobile;
    Button submit,back;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        init(view);
        return view;

    }
    public void init(View view){
        progressDialog=new ProgressDialog(getContext());
        back=view.findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        mobile=view.findViewById(R.id.edittext_name);
        submit=view.findViewById(R.id.btn_submit);
        // back=view.findViewById(R.id.rl_toolbar_registration);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==submit){
            progressDialog.show();
            forgetPassword(mobile.getText().toString());
        }
        if(view==back){
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
    public void forgetPassword(String username){


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit=new Retrofit.Builder().baseUrl(AppConfig.CONSTANT).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.forget(Constant.APICODE_VALUE,username);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                //  mutableLiveData.setValue();
                //    Log.d("TAG","VALUEDATAa"+t);
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Forget successfull",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });

    }
}
