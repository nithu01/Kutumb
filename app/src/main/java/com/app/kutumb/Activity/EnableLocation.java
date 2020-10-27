package com.app.kutumb.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kutumb.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EnableLocation extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    final static int REQUEST_LOCATION = 199;
    GoogleApiClient googleApiClient;
    GPSTracker gps;
    Geocoder geocoder;
    TextView textView;
    ProgressBar progressBar;
    double latitude,longitude;
    List<Address> addresses;
    String errorMessage = "";
    String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);
        if (!checkPermission()) {
            requestPermission();
            gpsCheck();
//            ButtonActivity();
        } else {
            gpsCheck();
//            ButtonActivity();
        }
        progressBar = findViewById(R.id.progressBar);
        textView=findViewById(R.id.set_location_manually);
        gps =new GPSTracker(this);
        geocoder = new Geocoder(this);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationdPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if ( locationdPermission ) {
                        textView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gps =new GPSTracker(EnableLocation.this);
                                // check if GPS enabled
                                if (gps.canGetLocation()) {
                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();
                                    geocoder = new Geocoder(EnableLocation.this, Locale.getDefault());
                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                        Log.d("TAG", "multipartImageUpload: " + addresses);
                                    } catch (IOException ioException) {
                                        errorMessage = "Service Not Available";
                                        Log.e("TAG", errorMessage, ioException);
                                    } catch (IllegalArgumentException illegalArgumentException) {
                                        errorMessage = "Invalid Latitude or Longitude Used";
                                        Log.e("TAG", errorMessage + ". " +
                                                "Latitude = " + gps.getLatitude() + ", Longitude = " +
                                                gps.getLongitude(), illegalArgumentException);
                                    }
                                    if (addresses != null && addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        result = address.getAddressLine(0);
                                        SplashActivity.savePreferences("address",result);
                                        Intent intent = new Intent(EnableLocation.this,MainActivity.class);
//
                                        startActivity(intent);

//                                        Toast.makeText(EnableLocation.this, latitude+"\n"+longitude+"\n"+result, Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                        },5000);


                    } else {
                        Toast.makeText(this, "Permission Denied, You cannot access location.", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to All the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EnableLocation.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean gpsCheck() {
        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) EnableLocation.this.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(EnableLocation.this)) {
//            Toast.makeText(MainActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            Log.e("TAG","Gps not enabled");
            enableLoc();
        }else{
//            Toast.makeText(MainActivity.this, "Gps already not enabled", Toast.LENGTH_SHORT).show();
            Log.e("TAG","Gps already enabled");
        }
        return true;
    }
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }
    private boolean enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(EnableLocation.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener((ConnectionResult connectionResult) ->
                    {
                        Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                    }).build();

            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                    final Status status = locationSettingsResult.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(EnableLocation.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                    }
                }
            });
        }
        return false;
    }
}
