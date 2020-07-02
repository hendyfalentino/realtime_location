package com.example.mapstracking.API;

import com.example.mapstracking.Model.DestinationLocation;
import com.example.mapstracking.Model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("saveCurrentLocation.php")
    Call<ResponseBody> saveCurrentLocation(
            @Query("location_latitude") double location_latitude,
            @Query("location_longitude") double location_longitude,
            @Query("user_id") String user_id
    );

    @GET("login.php")
    Call<User> loginRequest(
            @Query("user_id") String user_id,
            @Query("user_password") String user_password
    );

    @GET("getDestLoc.php")
    Call<List<DestinationLocation>> getDestLoc(
            @Query("user_id") String user_id
    );

    @GET("updDestLocStat.php")
    Call<ResponseBody> updDestLocStat(
            @Query("dest_loc_id") String dest_loc_id
    );
}
