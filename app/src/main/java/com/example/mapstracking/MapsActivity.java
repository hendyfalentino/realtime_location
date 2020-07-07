package com.example.mapstracking;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Route.FetchURL;
import com.example.mapstracking.Route.TaskLoadedCallback;
import com.example.mapstracking.Model.Mapping;
import com.example.mapstracking.userHandler.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    LatLng latLng;
    FusedLocationProviderClient fusedLocationProviderClient;
    SessionManager sessionManager;
    ApiInterface apiInterface;
    String id_petugas;
    private Polyline currentPolyline;
    int i, j;
    String[][] destLoc;
    LatLng[] destLatLng;
    Marker[] markers;
    double currentLatitude, currentLongitude, lastLatitude, lastLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Handler handler = new Handler();
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                getCurrentLocation();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(run);
        sessionManager = new SessionManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        getCurrentLocation();
        getDestinationMarker();
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.hideInfoWindow();
        Location loc1 = new Location("loc1");
        loc1.setLatitude(latLng.latitude);
        loc1.setLongitude(latLng.longitude);
        Location loc2 = new Location("loc2");
        loc2.setLatitude(marker.getPosition().latitude);
        loc2.setLongitude(marker.getPosition().longitude);
        float distance = loc1.distanceTo(loc2);

        if (distance < 30 && marker.getTag() == "FALSE") {
            String markerTitle = marker.getTitle();
            changeDestLocStatus(markerTitle);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            if (currentPolyline != null) {
                currentPolyline.remove();
            }
            new FetchURL(MapsActivity.this).execute(getUrl(latLng, marker.getPosition()), "driving");
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
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
                    latLng = new LatLng(currentLatitude, currentLongitude);
                    if (lastLatitude == 0.0d && lastLongitude == 0.0d ){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    } else if (currentLatitude != lastLatitude && currentLongitude != lastLongitude) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    }
                }
                lastLatitude = currentLatitude;
                lastLongitude = currentLongitude;
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + "driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
    }

    @Override
    public void onTaskDone(Object... values) {
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void getDestinationMarker(){
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_petugas = user.get(SessionManager.id_petugas);
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Mapping>> call = apiInterface.getMapping(id_petugas);
        call.enqueue(new Callback<List<Mapping>>() {
            @Override
            public void onResponse(Call<List<Mapping>> call, Response<List<Mapping>> response) {
                if(response.body() != null){
                    int size = response.body().size();
                    destLoc = new String[size][4];
                    for(i=0 ; i<size; i++){
                        for(j=0 ; j<4; j++){
                            if(j==0){
                                destLoc[i][j] = response.body().get(i).getId_mapping();
                            }else if(j==1){
                                destLoc[i][j] = response.body().get(i).getLatidue_mapping();
                            }else if(j==2){
                                destLoc[i][j] = response.body().get(i).getLongitude_mapping();
                            }else if(j==3){
                                destLoc[i][j] = response.body().get(i).getStatus_mapping();
                            }
                        }
                    }
                    for(i=0; i<size; i++){
                        markers = new Marker[size];
                        destLatLng = new LatLng[size];
                        destLatLng[i] = new LatLng(Double.parseDouble(destLoc[i][1]), Double.parseDouble(destLoc[i][2]));
                        if(destLoc[i][3].equals("FALSE")){
                            markers[i] = mMap.addMarker(new MarkerOptions().position(destLatLng[i]).title(destLoc[i][0]));
                            markers[i].setTag("FALSE");
                        } else {
                            markers[i] = mMap.addMarker(new MarkerOptions().position(destLatLng[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(destLoc[i][0]));
                            markers[i].setTag("TRUE");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Mapping>> call, Throwable t) {
                Log.d("getData", t.toString());
            }
        });
    }

    public void changeDestLocStatus(String id){
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.setStatusMapping(id);
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