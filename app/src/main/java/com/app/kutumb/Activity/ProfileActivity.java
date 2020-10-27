package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.kutumb.Config.Constant;
import com.app.kutumb.Config.PrefManager;
import com.app.kutumb.Config.SessionManager;
import com.app.kutumb.Model.UserData;
import com.app.kutumb.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private ImageView imgBack;
    TextView txtName,txtMobile,txtEmail,txtAddres;
    private SessionManager session;
    private ImageView imgShowMenu;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgBack=findViewById(R.id.img_back_profile);
        imgShowMenu=findViewById(R.id.img_show_menu);
        imgShowMenu.setOnClickListener(this);
        session=new SessionManager(ProfileActivity.this);
        imgBack.setOnClickListener(this);
        txtName=findViewById(R.id.txt_name_profile);
        txtMobile=findViewById(R.id.txt_mobile_profile);
        txtEmail=findViewById(R.id.txt_email_profile);
        txtAddres=findViewById(R.id.txt_address);
        UserData userData= PrefManager.getInstance(ProfileActivity.this).getUserData();
        txtName.setText(userData.getFirst_name()+" "+userData.getLast_name());
        txtMobile.setText("Mobile: "+userData.getPhone());
        txtEmail.setText("Email Id: "+userData.getEmail());
       // txtAddres.setText("Address: "+userData.getAddress());
    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        }
        if (v==imgShowMenu){
            showMenu(v);
        }
    }
    private void showMenu(View view){
        PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, view);//View will be an anchor for PopupMenu
        popupMenu.inflate(R.menu.menu_main);
        Menu menu = popupMenu.getMenu();
        if (session.isLoggedIn()){
            menu.findItem(R.id.login_logout).setTitle("Logout");
        }else {
            menu.findItem(R.id.login_logout).setTitle("SignIn");
        }
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }


    private void logout() {
        session.setLogin(false);
        SplashActivity.savePreferences(Constant.TYPE,"");
        PrefManager.getInstance(getApplicationContext()).logout();
        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_LONG).show();
        PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().clear().apply();
        PrefManager.getInstance(getApplicationContext()).logout();
        Intent intent =new Intent(ProfileActivity.this,MainActivity.class);
        intent.putExtra(Constant.TYPE,"");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }

   @Override
    public boolean onMenuItemClick(MenuItem item) {
        /*case R.id.menu:
               // logout();
                return(true);*/
       if (item.getItemId() == R.id.login_logout) {
           if (item.getTitle().equals("Logout")) {
               logout();
           }
           return (true);
       }
        return(super.onOptionsItemSelected(item));
    }
}
