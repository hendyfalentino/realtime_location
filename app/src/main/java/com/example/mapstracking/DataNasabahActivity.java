package com.example.mapstracking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Model.Nasabah;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataNasabahActivity extends AppCompatActivity {

    Button btn_setoran;
    TextView tv_nama, tv_ttl, tv_ktp, tv_ibu_kandung, tv_alamat, tv_nomor;
    ApiInterface apiInterface;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_nasabah);

        progressBar = findViewById(R.id.progress_bar_data);
        btn_setoran = findViewById(R.id.btn_setoran);
        tv_nama = findViewById(R.id.tv_nama);
        tv_ttl = findViewById(R.id.tv_ttl);
        tv_ktp = findViewById(R.id.tv_ktp);
        tv_ibu_kandung = findViewById(R.id.tv_ibu_kandung);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_nomor = findViewById(R.id.tv_nomor);

        Intent intent = getIntent();
        final String id_nasabah = intent.getStringExtra("id_nasabah");
        final String nama_nasabah = intent.getStringExtra("nama_nasabah");
        String menu = intent.getStringExtra("menu");

        if(menu != null && menu.equals("form")){
            btn_setoran.setVisibility(View.VISIBLE);
        }

        getDataNasabah(id_nasabah);

        btn_setoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataNasabahActivity.this, SetoranActivity.class);
                intent.putExtra("id_nasabah", id_nasabah);
                intent.putExtra("nama_nasabah", nama_nasabah);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDataNasabah(String id_nasabah) {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Nasabah> call = apiInterface.getDataNasabah(id_nasabah);
        call.enqueue(new Callback<Nasabah>() {
            @Override
            public void onResponse(Call<Nasabah> call, Response<Nasabah> response) {
                if(response.body() != null){
                    progressBar.setVisibility(View.GONE);
                    tv_nama.setVisibility(View.VISIBLE);
                    tv_ttl.setVisibility(View.VISIBLE);
                    tv_ktp.setVisibility(View.VISIBLE);
                    tv_ibu_kandung.setVisibility(View.VISIBLE);
                    tv_alamat.setVisibility(View.VISIBLE);
                    tv_nomor.setVisibility(View.VISIBLE);
                    tv_nama.setText(response.body().getNama_nasabah());
                    String ttl = response.body().getTempat_lahir_nasabah() + ", " + response.body().getTanggal_lahir_nasabah();
                    tv_ttl.setText(ttl);
                    tv_ktp.setText(response.body().getKtp_nasabah());
                    tv_ibu_kandung.setText(response.body().getIbu_kandung_nasabah());
                    tv_alamat.setText(response.body().getAlamat_nasabah());
                    tv_nomor.setText(response.body().getNomor_nasabah());
                }
            }

            @Override
            public void onFailure(Call<Nasabah> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DataNasabahActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_SHORT).show();
                Log.d("getData", String.valueOf(t.getCause()));
            }
        });
    }
}