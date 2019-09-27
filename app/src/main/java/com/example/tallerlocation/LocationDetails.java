package com.example.tallerlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission_group.LOCATION;

public class LocationDetails extends AppCompatActivity {
    public static final int REQUEST_CHECK_SETTINGS = 5;
    private static final double RADIUS_OF_EARTH_KM = 6371 ;

    private FusedLocationProviderClient mFusedLocationClient;
    TextView lat;
    TextView longi;
    TextView alti;
    TextView txt_distance;

    private LocationRequest mLocationRequest;

    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        lat = findViewById(R.id.latitude);
        longi = findViewById(R.id.longitud);
        alti = findViewById(R.id.altitude);
        txt_distance = findViewById(R.id.txt_distance);

        mLocationRequest = createLocationRequest();

        lat.setText("Latitude: ");
        longi.setText("Longitude: ");
        alti.setText("Altitude: ");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //Una vez se tengan permisos
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                startLocationUpdates(); //Todas las condiciones para recibir localizaciones
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(LocationDetails.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    Toast.makeText(getApplicationContext(), "Funciona!", Toast.LENGTH_LONG).show();
                    lat.setText("Latitude: " + location.getLatitude());
                    longi.setText("Longitude: " + location.getLongitude());
                    alti.setText("Altitude: " + location.getAltitude());
                    double lati2=47007;
                    double long2=-741445;

                    double distance=distance( location.getLatitude(),  location.getLongitude(), lati2 ,  long2);
                    txt_distance.setText( "Distancia al Aereopuerto:  "+ distance);

                }
            }
        };


        //mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new
        //      OnSuccessListener<Location>() {
        //        @Override
        //      public void onSuccess(Location location) {
        //        Log.i("LOCATION", "onSuccess location");
        //      if (location != null) {
        //        Toast.makeText(getApplicationContext(), "Funciona!", Toast.LENGTH_LONG).show();
        //      lat.setText(String.valueOf(location.getLatitude()));
        //    longi.setText(String.valueOf(location.getLongitude()));
        //  alti.setText(String.valueOf(location.getAltitude()));
        //}
        //}
        //}
        //);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates(); //Se encendió la localización!!!
                } else {
                    Toast.makeText(this,
                            "Sin acceso a localización, hardware deshabilitado!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }

}




