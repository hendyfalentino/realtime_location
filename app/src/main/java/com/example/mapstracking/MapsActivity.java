package com.example.mapstracking;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.mapstracking.directionhelpers.FetchURL;
import com.example.mapstracking.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    LatLng latLng;
    private LatLng marker1 = new LatLng(1.5050588, 124.8727851);
    private LatLng marker2 = new LatLng(1.4124, 124.9878);
    private LatLng marker3 = new LatLng(1.4915238, 124.8380605);
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLatitude;
    double currentLongitude;
    double lastLatitude;
    double lastLongitude;
    ApiInterface apiInterface;
    String user_id = "1";
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        mMap.addMarker(new MarkerOptions().position(marker1).title("Marker 1"));
        mMap.addMarker(new MarkerOptions().position(marker2).title("Marker 2"));
        mMap.addMarker(new MarkerOptions().position(marker3).title("Marker 3"));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Location loc1 = new Location("loc1");
        loc1.setLatitude(latLng.latitude);
        loc1.setLongitude(latLng.longitude);
        Location loc2 = new Location("loc2");
        loc2.setLatitude(marker.getPosition().latitude);
        loc2.setLongitude(marker.getPosition().longitude);
        float distance = loc1.distanceTo(loc2);

        if(currentPolyline != null){
            currentPolyline.remove();
        }
        if(distance<10){
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            new FetchURL(MapsActivity.this).execute(getUrl(latLng, marker.getPosition(), "driving"), "driving");
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
                    latLng = new LatLng(currentLatitude,currentLongitude);
                    if (lastLatitude == 0.0d && lastLongitude == 0.0d ){
                        saveLocation();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    } else {
                        if (currentLatitude != lastLatitude && currentLongitude != lastLongitude) {
                            saveLocation();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
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

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}