package com.example.mapstracking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Model.Mapping;
import com.example.mapstracking.Route.FetchURL;
import com.example.mapstracking.Route.TaskLoadedCallback;
import com.example.mapstracking.userHandler.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

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
    ProgressBar progressBar;

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

        progressBar = findViewById(R.id.progress_bar_map);
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
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (getDistance(latLng.latitude, latLng.longitude, marker.getPosition().latitude, marker.getPosition().longitude) < 15){
            if (currentPolyline != null) {
                currentPolyline.remove();
            }
        } else {
            if (currentPolyline != null) {
                currentPolyline.remove();
            }
            new FetchURL(MapsActivity.this).execute(getUrl(latLng, marker.getPosition()), "driving");
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String markerTitle = marker.getTitle();
        if (getDistance(latLng.latitude, latLng.longitude, marker.getPosition().latitude, marker.getPosition().longitude) < 15 && marker.getTag() == "FALSE") {
            Intent intent = new Intent(MapsActivity.this, SetoranActivity.class);
            intent.putExtra("map", "map");
            intent.putExtra("id_marker", markerTitle);
            startActivity(intent);
            finish();
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
                    currentLatitude = Double.parseDouble(new DecimalFormat("##.####").format(location.getLatitude()));
                    currentLongitude = Double.parseDouble(new DecimalFormat("##.####").format(location.getLongitude()));
                    latLng = new LatLng(currentLatitude, currentLongitude);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(17)
                            .bearing(getRotationAngle(lastLatitude, lastLongitude, currentLatitude, currentLongitude))
                            .tilt(45)
                            .build();
                    if (lastLatitude == 0.0d && lastLongitude == 0.0d ){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                    } else if (getDistance(currentLatitude, currentLongitude, lastLatitude, lastLongitude) > 5){
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }
                lastLatitude = currentLatitude;
                lastLongitude = currentLongitude;
            }
        });
    }

    public static float getRotationAngle(double lastLat, double lastLng, double curLat , double curLng) {

        float xDiff = (float) (curLat - lastLat);
        float yDiff = (float) (curLng - lastLng);

        return (float) (Math.atan2(yDiff, xDiff) * 180.0 / Math.PI);
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
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_petugas = user.get(SessionManager.id_petugas);
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Mapping>> call = apiInterface.getMapping(id_petugas);
        call.enqueue(new Callback<List<Mapping>>() {
            @Override
            public void onResponse(Call<List<Mapping>> call, Response<List<Mapping>> response) {
                if(response.body() != null){
                    int size = response.body().size();
                    destLoc = new String[size][7];
                    for(i=0; i<size; i++){
                        for(j=0 ; j<7; j++){
                            if(j==0){
                                destLoc[i][j] = response.body().get(i).getId_mapping();
                            }else if(j==1){
                                destLoc[i][j] = response.body().get(i).getLatidue_mapping();
                            }else if(j==2){
                                destLoc[i][j] = response.body().get(i).getLongitude_mapping();
                            }else if(j==3){
                                destLoc[i][j] = response.body().get(i).getStatus_mapping();
                            }else if(j==4){
                                destLoc[i][j] = response.body().get(i).getNama_nasabah();
                            }else if(j==5){
                                destLoc[i][j] = response.body().get(i).getTempat_lahir_nasabah() + ", " + response.body().get(i).getTanggal_lahir_nasabah();
                            }else if(j==6){
                                destLoc[i][j] = response.body().get(i).getKtp_nasabah();
                            }
                        }
                        markers = new Marker[size];
                        destLatLng = new LatLng[size];
                        destLatLng[i] = new LatLng(Double.parseDouble(destLoc[i][1]), Double.parseDouble(destLoc[i][2]));
                        String snippet = "Nama : "+ destLoc[i][4]+"\n"
                                +"TTL    : "+ destLoc[i][5]+"\n"
                                +"KTP    : "+ destLoc[i][6];
                        if(destLoc[i][3].equals("FALSE")){
                            markers[i] = mMap.addMarker(new MarkerOptions()
                                    .position(destLatLng[i])
                                    .title(destLoc[i][0])
                                    .snippet(snippet));
                            markers[i].setTag("FALSE");
                        } else {
                            markers[i] = mMap.addMarker(new MarkerOptions()
                                    .position(destLatLng[i])
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(destLoc[i][0]).snippet(snippet));
                            markers[i].setTag("TRUE");
                        }
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView snippet = new TextView(MapsActivity.this);
                                snippet.setTextColor(Color.BLACK);
                                snippet.setTypeface(null, Typeface.BOLD);
                                snippet.setText(marker.getSnippet());

                                info.addView(snippet);

                                return info;
                            }
                        });
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Mapping>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("getData", t.toString());
            }
        });
    }

}