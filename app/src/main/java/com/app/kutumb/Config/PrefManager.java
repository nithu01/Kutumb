package com.app.kutumb.Config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.kutumb.Model.UserData;


/**
 * Created by aftab on 4/30/2018.
 */

public class PrefManager {

    private static final String SHARED_PREF_NAME = Constant.PREFS_NAME;



    private static final String TAG_TOKEN = "tagtoken";


    @SuppressLint("StaticFieldLeak")
    private static PrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    private static final String SET_NOTIFY = "set_notify";

    private SharedPreferences pref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private SharedPreferences.Editor editor;

    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_FIRSTNAME = "FirstName";
    private static final String KEY_LASTNAME = "LastName";


    public PrefManager(Context context) {
        mCtx = context;
        pref = context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }
    public void userLogin(UserData userData) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, userData.getId());
        editor.putString(KEY_EMAIL, userData.getEmail());
        editor.putString(KEY_PHONE,userData.getPhone());
        editor.putString(KEY_FIRSTNAME,userData.getFirst_name());
        editor.putString(KEY_LASTNAME,userData.getLast_name());
        editor.apply();
    }
    public UserData getUserData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserData(
                sharedPreferences.getString(KEY_ID,null),
                sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_PHONE,null),
                sharedPreferences.getString(KEY_FIRSTNAME,null),
                sharedPreferences.getString(KEY_LASTNAME,null));
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // mCtx.startActivity(new Intent(mCtx, SplashMainActivity.class));
    }
    public void saveNotificationSubscription(boolean value){
        SharedPreferences.Editor edits = pref.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.apply();
    }
    public boolean hasUserSubscribeToNotification(){
        return pref.getBoolean(SET_NOTIFY, false);
    }
    public void clearAllSubscriptions(){
        pref.edit().clear().apply();
    }
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

}
