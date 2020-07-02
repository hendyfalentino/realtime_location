package com.example.mapstracking.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;
import com.example.mapstracking.MainActivity;
import com.google.android.gms.location.LocationResult;

public class LocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.mapstracking.Service.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    Location location = result.getLastLocation();
                    String loc = location.getLatitude() +
                            "/" +
                            location.getLongitude();
                    try {
                        MainActivity.getInstance().updateTv(loc);
                    } catch (Exception ex) {
                        Toast.makeText(context, loc, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
