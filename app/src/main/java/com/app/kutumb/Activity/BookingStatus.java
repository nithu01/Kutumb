package com.app.kutumb.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.kutumb.Config.Constant;
import com.app.kutumb.R;


public class BookingStatus extends AppCompatActivity implements View.OnClickListener {
    TextView txtAmount;
    private ImageView imgBack;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);
        imgBack=findViewById(R.id.img_close_boooking);
        imgBack.setOnClickListener(this);
        txtAmount=findViewById(R.id.txt_amount_booking);
        txtAmount.setText("Successfully paid "+getIntent().getStringExtra(Constant.AMOUNT));
    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent=new Intent(BookingStatus.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(BookingStatus.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}
