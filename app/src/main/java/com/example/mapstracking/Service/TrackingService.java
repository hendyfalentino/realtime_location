package com.example.mapstracking.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.userHandler.SessionManager;
import com.google.android.gms.location.LocationResult;

import java.text.DecimalFormat;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingService extends BroadcastReceiver {

    double currentLatitude;
    double currentLongitude;
    double lastLatitude;
    double lastLongitude;
    ApiInterface apiInterface;
    String id_petugas;

    public static final String ACTION_PROCESS_UPDATE = "com.example.mapstracking.Service.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_petugas = user.get(SessionManager.id_petugas);
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    Location location = result.getLastLocation();
                    currentLatitude = Double.parseDouble(String.valueOf(location.getLatitude()));
                    currentLongitude = Double.parseDouble(String.valueOf(location.getLongitude()));

                    if (lastLatitude == 0.0d && lastLongitude == 0.0d ){
                        saveLocation(id_petugas);
                    } else if (getDistance(currentLatitude, currentLongitude, lastLatitude, lastLongitude) > 2) {
                        saveLocation(id_petugas);
                    }
                }
                lastLatitude = currentLatitude;
                lastLongitude = currentLongitude;
            }
        }
    }

    private float getDistance(double Latitude1, double Longitude1, double Latitude2, double Longitude2) {
        Location location1 = new Location("location1");
        Location location2 = new Location("location2");

        location1.setLatitude(Latitude1);
        location1.setLongitude(Longitude1);

        location2.setLatitude(Latitude2);
        location2.setLongitude(Longitude2);

        return location1.distanceTo(location2);
    }

    public void saveLocation(String uId){
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.saveTracking(currentLatitude, currentLongitude, uId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }
}
