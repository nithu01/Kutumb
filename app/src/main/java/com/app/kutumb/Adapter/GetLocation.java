package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.app.kutumb.Activity.MainActivity;

public class GetLocation extends Service implements LocationListener {

    Context mContext;
    public static boolean isGPSEnabled = false;
    private ProgressDialog progresDialog;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L;
    private static final long MIN_TIME_BW_UPDATES = 60000L;
    protected LocationManager locationManager;

    public GetLocation(Context context) {
        this.mContext = context;
        this.getLocation();
    }

    public GetLocation(){}

    public GetLocation(MainActivity mainActivity, ProgressDialog progressDialog) {
        mContext=mainActivity;
        progresDialog=progressDialog;
    }

    @SuppressLint({"MissingPermission", "WrongConstant"})
    public Location getLocation() {

        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            if (isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("network", 60000L, 10.0F, this);
                    Log.d("Network", "Network");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("network");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled && this.location == null) {
                    this.locationManager.requestLocationUpdates("gps", 60000L, 10.0F, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("gps");
                        if (this.location != null) {
                            progresDialog.dismiss();
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return this.location;
    }

    public void stopUsingGPS() {
        if (this.locationManager != null) {

        }
    }

    public double getLatitude() {
        if (this.location != null) {
            this.latitude = this.location.getLatitude();
            progresDialog.dismiss();
        }
        return this.latitude;
    }

    public double getLongitude() {
        if (this.location != null) {
            this.longitude = this.location.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", (dialog, which) -> {

            Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
            GetLocation.this.mContext.startActivity(intent);

        });
        alertDialog.show();
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}

