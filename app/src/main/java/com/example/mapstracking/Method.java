package com.example.mapstracking;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Model.CurrentLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Method {

    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static LatLng latLng;
    private static double lastLatitude;
    private static double lastLongitude;
    private static double currentLatitude;
    private static double currentLongitude;
    static ApiInterface apiInterface;
    static GoogleMap map;

    public static void getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
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

    public static void saveLocation(){
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<CurrentLocation>> call = apiInterface.saveCurrentLocation(currentLatitude, currentLongitude);
        call.enqueue(new Callback<List<CurrentLocation>>() {
            @Override
            public void onResponse(Call<List<CurrentLocation>> call, Response<List<CurrentLocation>> response) {
                Log.d("getData", String.valueOf(response.isSuccessful()));
                Log.d("getData", String.valueOf(response.message()));
            }

            @Override
            public void onFailure(Call<List<CurrentLocation>> call, Throwable t) {
                Log.d("getData", t.toString());
            }
        });
    }
}
