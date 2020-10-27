package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.kutumb.Config.PrefManager;
import com.app.kutumb.R;


public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    static SharedPreferences sharedPreferences;
    //  SessionManager session;
    TextView txtRights;
    private PrefManager prefManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new PrefManager(SplashActivity.this);
        //session=new SessionManager(this);
        txtRights=findViewById(R.id.txt_copyright);
        txtRights.setText(Html.fromHtml("<font color='#000000'> " +"\u00a9"+  " 2020 Kutumbh" + "</font>"));
//                +"<font color='#E15616'>" + "world" + "</font>"));

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,6000);
            }
        },6000);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        progressBar =  findViewById(R.id.progress_bar);
        new Thread(() -> {
            doWork();
            startApp();
            finish();
        }).start();
    }
    private void doWork() {
        for (int progress=0; progress<100; progress+=30){
            try{
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startApp(){
        if (prefManager.isFirstTimeLaunch()){
            //launchHomeScreen();
            Intent intent =new Intent(SplashActivity.this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent =new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getPreferences(String key, String val) {
        return sharedPreferences.getString(key, val);
    }
}
