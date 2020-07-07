package com.example.mapstracking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Petugas {

    @SerializedName("id_petugas")
    @Expose
    String id_petugas;

    @SerializedName("password_petugas")
    @Expose
    String password_petugas;

    public String getid_petugas() {
        return id_petugas;
    }

    public void setid_petugas(String id_petugas) {
        this.id_petugas = id_petugas;
    }

    public String getpassword_petugas() {
        return password_petugas;
    }

    public void setpassword_petugas(String password_petugas) {
        this.password_petugas = password_petugas;
    }
}
