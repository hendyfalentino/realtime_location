package com.example.mapstracking;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Adapter.ListNasabahAdapter;
import com.example.mapstracking.Model.Nasabah;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NasabahActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ApiInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Nasabah> nasabah;
    ListNasabahAdapter adapter;
    String menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah);

        Intent intent = getIntent();
        menu = intent.getStringExtra("menu");

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rv_nasabah);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String like) {
                fetchNasabah(like);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String like) {
                fetchNasabah(like);
                return false;
            }
        });
        fetchNasabah("");
    }

    public void fetchNasabah(String like){
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Nasabah>> call = apiInterface.fetchNasabah(like);
        call.enqueue(new Callback<List<Nasabah>>() {
            @Override
            public void onResponse(Call<List<Nasabah>> call, Response<List<Nasabah>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                nasabah = response.body();
                layoutManager = new LinearLayoutManager(NasabahActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapter = new ListNasabahAdapter(nasabah, NasabahActivity.this, menu);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Nasabah>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NasabahActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_SHORT).show();
                Log.d("getData", t.getMessage());
            }
        });
    }
}