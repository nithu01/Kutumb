package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.kutumb.Config.Configuration;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Config.WebService;
import com.app.kutumb.Fragment.FragmentFgtPassword;
import com.app.kutumb.Fragment.FragmentOtpLogin;
import com.app.kutumb.Fragment.FragmentRegistration;
import com.app.kutumb.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    public static FrameLayout frameLayout;
    @SuppressLint("StaticFieldLeak")
    public static ScrollView scrollView;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout rlBottom;
    TextView txtForgot,txtRights,txtSignUp,txtloginOtp;
    private EditText editTextUsername,editTextPassword;
    private ImageView imgShowPass;
    private boolean showpass=false;
    private Button btnLogin;
    private ProgressDialog pDialog;
    private RelativeLayout rlMain;
    private LinearLayout txtCancel;
    SessionManager session;
    Fragment currentFragment;
    FragmentTransaction ft;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rlMain=findViewById(R.id.rl_main_login);
        txtSignUp=findViewById(R.id.txt_signup);
        txtCancel=findViewById(R.id.txt_cancel);
        rlBottom=findViewById(R.id.rl_bottom_login);
        frameLayout=findViewById(R.id.frame_login);
        scrollView=findViewById(R.id.scrollview_login);
        txtForgot=findViewById(R.id.forgot);
        txtloginOtp=findViewById(R.id.OTP);
        txtloginOtp.setOnClickListener(this);
        session=new SessionManager(LoginActivity.this);
        editTextPassword=findViewById(R.id.password_login);
        editTextUsername=findViewById(R.id.username_login);
        imgShowPass=findViewById(R.id.img_showpass);
        pDialog=new ProgressDialog(LoginActivity.this);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtRights=findViewById(R.id.txt_copyrights);
        txtRights.setText(Html.fromHtml("<font color='#000000'> " +"\u00a9"+  " 2020 Kutumb" +
                "</font>"+"<font color='#E15616'>" + "" + "</font>"));
      //  txtRights.setVisibility(View.GONE);
        rlMain.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> {

                    Rect r = new Rect();
                    rlMain.getWindowVisibleDisplayFrame(r);
                    int screenHeight = rlMain.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    int keypadHeight = screenHeight - r.bottom;

                  //  Log.d(TAG, "keypadHeight = " + keypadHeight);

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        // keyboard is opened
                      //  rlBottom.setVisibility(View.GONE);
                    }
                    else {
                        // keyboard is closed
                      //  rlBottom.setVisibility(View.VISIBLE);
                    }
                });
        try{
            type=SplashActivity.getPreferences(Constant.TYPE,"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v==txtSignUp){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentRegistration();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if (v==txtForgot){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentFgtPassword();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if (v==txtCancel){
            SplashActivity.savePreferences(Constant.TYPE,"");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            finish();
        }
        if (v==imgShowPass){
            int start, end;
            if (showpass) {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                editTextPassword.setSelection(start, end);
                showpass = false;
                imgShowPass.setImageResource(R.drawable.ic_hide);
            } else {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(null);
                editTextPassword.setSelection(start, end);
                showpass = true;
                imgShowPass.setImageResource(R.drawable.ic_view);
            }
        }
        if (v==btnLogin){
            String username=editTextUsername.getText().toString();
            String password=editTextPassword.getText().toString();
            if (Configuration.hasNetworkConnection(LoginActivity.this)){
                if (username.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter details",Toast.LENGTH_LONG).show();
                }else {
                    WebService.login(username,password,pDialog,LoginActivity.this,session,type);
                }
            }else {
                Toast.makeText(LoginActivity.this,"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
        if (v==txtloginOtp){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentOtpLogin();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
       /* new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit without login?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {*/
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        finish();
               /*     }
                }).create().show();*/
    }
}
