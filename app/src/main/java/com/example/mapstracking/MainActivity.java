package com.example.mapstracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Model.CurrentLocation;
import com.example.mapstracking.userHandler.LoginActivity;
import com.example.mapstracking.userHandler.LogoutActivity;
import com.example.mapstracking.userHandler.SessionActivity;
import com.example.mapstracking.userHandler.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    LatLng latLng;
    GoogleMap map;
    private LatLng marker1 = new LatLng(1.5050588, 124.8727851);
    private LatLng marker2 = new LatLng(1.49642737355, 124.878909588);
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    ApiInterface apiInterface;
    double currentLatitude;
    double currentLongitude;
    double lastLatitude;
    double lastLongitude;
    String user_id;
    TextView textView;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        sessionManager = new SessionManager(this);
        sessionManager.createSession("1"); //Temporary
        sessionManager.checkLogIn();
        HashMap<String, String> user = sessionManager.getUserDetail();
        user_id = user.get(SessionManager.user_id);
        final Handler handler = new Handler();
        final int count = 0;
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                getCurrentLocation();
                if(count != 1) {
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.post(run);
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
        /*
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (map != null) {
            map.setMyLocationEnabled(true);
        }
    }

    private void getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLatitude = location.getLatitude();
                    currentLatitude = Double.parseDouble(new DecimalFormat("##.####").format(currentLatitude));
                    currentLongitude = location.getLongitude();
                    currentLongitude = Double.parseDouble(new DecimalFormat("##.####").format(currentLongitude));
                    latLng = new LatLng(currentLatitude,currentLongitude);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    if (lastLatitude == 0.0d && lastLongitude == 0.0d ){
                        saveLocation();
                    } else {
                        if (currentLatitude != lastLatitude && currentLongitude != lastLongitude) {
                            saveLocation();
                        }
                    }
                }
            }
        });
        lastLatitude = currentLatitude;
        lastLongitude = currentLongitude;
    }

    public void saveLocation(){
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<CurrentLocation>> call = apiInterface.saveCurrentLocation(currentLatitude, currentLongitude, user_id);
        call.enqueue(new Callback<List<CurrentLocation>>() {
            @Override
            public void onResponse(Call<List<CurrentLocation>> call, Response<List<CurrentLocation>> response) {

            }

            @Override
            public void onFailure(Call<List<CurrentLocation>> call, Throwable t) {
                Log.d("getData", t.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                getCurrentLocation();
            }
        }
    }

}
/*
<LinearLayout
        android:id="@+id/panelInfo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        android:orientation="vertical"
        android:layout_alignParentBottom="true"
        android:background="@android:color/white">
        <TextView
            android:id="@+id/tvDuration"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="14sp"/>
        <Button
            android:id="@+id/btn_logout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:layout_marginTop="25dp"
            android:layout_centerVertical="true"
            android:text="Logout" />
    </LinearLayout>
 */