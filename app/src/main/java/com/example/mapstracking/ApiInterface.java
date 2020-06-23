package com.example.mapstracking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("maps/api/distancematrix/json?")
    Call<List<MapDistance>> getDistance (
            @Field("distance") String distance
    );
}
