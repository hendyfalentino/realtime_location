package com.example.mapstracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.mapstracking.Service.BackgroundService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {


    GoogleMap map;
    private LatLng marker1 = new LatLng(1.5050588, 124.8727851);
    private LatLng marker2 = new LatLng(1.49642737355, 124.878909588);
    private TextView tvDuration, tvDistance;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        /*
        final Handler handler = new Handler();
        final int count = 0;
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                Method.getCurrentLocation();
                if(count != 1) {
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.post(run);
         */

        ComponentName componentName = new ComponentName(this, BackgroundService.class);
        JobInfo info = new JobInfo().Builder

        https://www.youtube.com/watch?v=3EQWmME-hNA

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                enableMyLocation();
                map.addMarker(new MarkerOptions().position(marker1).title("Marker 1"));
                map.addMarker(new MarkerOptions().position(marker2).title("Marker 2"));
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (map != null) {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                Method.getCurrentLocation();
            }
        }
    }

}