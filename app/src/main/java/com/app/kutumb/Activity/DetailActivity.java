package com.app.kutumb.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.kutumb.Config.Constant;
import com.app.kutumb.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DetailActivity.class.getSimpleName();
    TextView /*txtName,*/txtPrice,txtQty;
    Button btnDecrese,btnIncrease;
    private ImageView imgBack,imgItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imgItem=findViewById(R.id.img_item_detail);
        imgBack=findViewById(R.id.img_back_detail);
        imgBack.setOnClickListener(this);
        txtPrice=findViewById(R.id.txt_price_accessories);
        txtQty=findViewById(R.id.txt_qty_accessories);
        btnDecrese=findViewById(R.id.decrease);
        btnIncrease=findViewById(R.id.increase);
        btnIncrease.setOnClickListener(v -> {
            int count= Integer.parseInt(txtQty.getText().toString());
            if (count<4)
                count++;
            txtQty.setText(String.valueOf(count));
           // String value = String.valueOf(count)+" "+contact.getCharge();
           // Log.e("VALIGJ5521FHJFG","gdh12131afdhjs"+value);
        });
        btnDecrese.setOnClickListener(v -> {
            int count= Integer.parseInt(txtQty.getText().toString());
            count--;
            count = count>0 ? --count : 0;
            txtQty.setText(String.valueOf(count));
            // String value = String.valueOf(count) + " " + contact.getCharge();
            // Log.e("VALIGJFHJFG", "gdhafdhjs" + value);
        });
        getItemTableData();
    }

    private void getItemTableData() {
        Log.e(TAG,"ITEM_PRICE--->"+getIntent().getStringExtra(Constant.ITEM_COST)
                +"\n"+getIntent().getStringExtra(Constant.ITEM_NAME));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.logo);

        Glide.with(DetailActivity.this)
                .load(getIntent().getStringExtra(Constant.ITEM_IMAGE_NAME))
                .apply(options)
                .into(imgItem);
    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent =new Intent(DetailActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(DetailActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        finish();
    }
}
