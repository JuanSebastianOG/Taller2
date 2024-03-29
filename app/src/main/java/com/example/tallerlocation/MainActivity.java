package com.example.tallerlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 5;
    private static final int MY_PERMISSIONS_REQUEST_READ_CAMERA =4 ;
    public static final int MY_PERMISSIONS_READ_CONTACTS = 3;
    ImageButton btn_location;
    ImageButton btn_images;
    ImageButton btn_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_location=(ImageButton) findViewById(R.id.btn_location);
        btn_images=(ImageButton) findViewById(R.id.btn_images);
        btn_contacts=(ImageButton) findViewById(R.id.btn_contacts);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        });
        btn_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(MY_PERMISSIONS_REQUEST_READ_CAMERA);
            }
        });

        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(3);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Acceso a Location!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),LocationDetails.class);
                    startActivity(intent);
                } else {
                    //codigo si no cumple
                    Toast.makeText(this, "Funcionalidad Limitada!", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Acceso a Contactos!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),Contacts.class);
                    startActivity(intent);
                } else {
                    //codigo si no cumple
                    Toast.makeText(this, "Funcionalidad Limitada con Contactos!", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Acceso a camara!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),Camera.class);
                    startActivity(intent);
                } else {
                    //codigo si no cumple
                    Toast.makeText(this, "Funcionalidad Limitada!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private void askPermission(int idPer) {

            // Request the permission.
            if(idPer == 3){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_READ_CONTACTS);
            }
            if(idPer == 4){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_CAMERA);

            }
            if(idPer == 5){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }

    }

}
