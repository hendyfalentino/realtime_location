package com.example.mapstracking.API;

import com.example.mapstracking.Model.Mapping;
import com.example.mapstracking.Model.Petugas;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("saveTracking.php")
    Call<ResponseBody> saveTracking(
            @Query("latitude_tracking") double latitude_tracking,
            @Query("longitude_tracking") double longitude_tracking,
            @Query("id_petugas") String id_petugas
    );

    @GET("login.php")
    Call<Petugas> loginRequest(
            @Query("id_petugas") String id_petugas,
            @Query("password_petugas") String password_petugas
    );

    @GET("getMapping.php")
    Call<List<Mapping>> getMapping(
            @Query("id_petugas") String id_petugas
    );

    @GET("setStatusMapping.php")
    Call<ResponseBody> setStatusMapping(
            @Query("id_mapping") String id_mapping
    );

    @GET("insertSetoran.php")
    Call<ResponseBody> insertSetoran(
            @Query("jumlah_setoran") String jumlah_setoran,
            @Query("deskripsi_setoran") String desktipsi_setoran,
            @Query("id_petugas") String id_petugas
    );
}
