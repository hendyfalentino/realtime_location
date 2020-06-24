package com.example.mapstracking.API;

import com.example.mapstracking.Model.CurrentLocation;
import com.example.mapstracking.Model.MapDistance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("maps/api/distancematrix/json?")
    Call<List<MapDistance>> getDistance (
            @Field("distance") String distance
    );

    @FormUrlEncoded
    @POST("saveCurrentLocation.php")
    Call<List<CurrentLocation>> saveCurrentLocation(
            @Field("location_latitude") double location_latitude,
            @Field("location_longitude") double location_longitude
    );

}
