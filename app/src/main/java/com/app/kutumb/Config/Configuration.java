package com.app.kutumb.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.kutumb.Activity.LoginActivity;
import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aftab on 1/10/2018.
 */

public class Configuration {

    public static boolean hasNetworkConnection(Context context) {
        // TODO Auto-generated method stub

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void showDialog(String message, ProgressDialog dialog)
    {
        dialog.show();
        dialog.setMessage(message);
        dialog.setCancelable(false);
    }

    public static void saveValue(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getValue(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, "empty");
    }







    public static void showTime(final TextView edtTime, Context context, final String date){

        final Calendar myCalendar = Calendar.getInstance();
        final int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String min;
                        int hours = hourOfDay;
                        String timeSet;
                        if (hours > 12) {
                            hours -= 12;
                            timeSet = "PM";
                        } else if (hours == 0) {
                            hours += 12;
                            timeSet = "AM";
                        } else if (hours == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        if (minute<10){
                            min="0"+minute;
                        }else {
                            min=String.valueOf(minute);
                        }
                        int m=Integer.valueOf(min);
                        int h= hours;
                        String m2="",h2="";
                        if (m>0&&m<30){
                            m2="30";
                        }else if (m>30){
                            h2=String.valueOf(h+1);
                            m2="00";
                        }
                        Log.e("MINUTSES","MINURS CHECK--->"+m2+" hours--->"+h2);

                        String aTime = String.valueOf(hours) + ':' +
                                min + " " + timeSet;

                      //  edtTime.setText(hr + " : " + min);
                        edtTime.setText(date+"\n"+aTime);
                    }
                }, hour, minute, false);
        timePickerDialog.show();

    }



    public static String encodePath(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        //Base64.de
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Dialog openDialog(Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setContentView(R.layout.dialog);
        final Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return  dialog;
    }


    public static Dialog openBdayDialog(Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setContentView(R.layout.birthday_dialog);
        final Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return  dialog;
    }


    public static void closeDialog(Dialog dialog, TextView textView, String bloodGroup)
    {

        dialog.dismiss();
        textView.setText(bloodGroup);
    }

    public  static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void showCustomToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void openPopupUpDown(Context context, int animationSource,String error, String message) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.notice_info);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_notice_info);
        TextView txtMessage=dialg.findViewById(R.id.txt_notice_info);
        Button btnOk=dialg.findViewById(R.id.btn_notice_info);
        txtMessage.setText(message);
        if (error.equalsIgnoreCase("error")){
            imageView.setImageResource(R.drawable.warning);
        }else if (error.equalsIgnoreCase("internetError")){
            imageView.setImageResource(R.drawable.nointernet);
        }else {
            imageView.setImageResource(R.drawable.success);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialg.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialg.getWindow()).getAttributes().windowAnimations = animationSource;
        }
        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static void openPopupUpDownBack(final Context context, int animationSource, final String back, String error, String message, final String type) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.notice_info);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_notice_info);
        TextView txtMessage=dialg.findViewById(R.id.txt_notice_info);
        Button btnOk=dialg.findViewById(R.id.btn_notice_info);
        txtMessage.setText(message);
        if (error.equalsIgnoreCase("error")){
            imageView.setImageResource(R.drawable.warning);
        }else if (error.equalsIgnoreCase("internetError")){
            imageView.setImageResource(R.drawable.nointernet);
        }else {
            imageView.setImageResource(R.drawable.success);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialg.dismiss();
                if (back.equalsIgnoreCase("main")) {
                    // SplashActivity.savePreferences(Constant.SERVICE, "");
                    Intent intent = new Intent(context, MainActivity.class);
                   // intent.putExtra(Constant.TYPE,type);
                    // intent.putExtra(Constant.MESSAGE,response.getString("Message"));
                    context.startActivity(intent);
                }else if (back.equalsIgnoreCase("login")){
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialg.getWindow()).getAttributes().windowAnimations = animationSource;
        }
        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

}
