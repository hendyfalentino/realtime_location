package com.example.mapstracking.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.MainActivity;
import com.example.mapstracking.userHandler.SessionManager;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends BroadcastReceiver {

    double currentLatitude;
    double currentLongitude;
    double lastLatitude;
    double lastLongitude;
    LatLng latLng;
    ApiInterface apiInterface;
    String user_id;

    public static final String ACTION_PROCESS_UPDATE = "com.example.mapstracking.Service.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        user_id = user.get(SessionManager.user_id);
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    Location location = result.getLastLocation();
                    currentLatitude = location.getLatitude();
                    currentLatitude = Double.parseDouble(new DecimalFormat("##.####").format(currentLatitude));
                    currentLongitude = location.getLongitude();
                    currentLongitude = Double.parseDouble(new DecimalFormat("##.####").format(currentLongitude));
                    latLng = new LatLng(currentLatitude,currentLongitude);
                    if (lastLatitude == 0.0d && lastLongitude == 0.0d ){
                        saveLocation(user_id);
                    } else if (currentLatitude != lastLatitude && currentLongitude != lastLongitude) {
                        saveLocation(user_id);
                    }
                }
                lastLatitude = currentLatitude;
                lastLongitude = currentLongitude;
            }
        }
    }

    public void saveLocation(String uId){
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.saveCurrentLocation(currentLatitude, currentLongitude, uId);
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
