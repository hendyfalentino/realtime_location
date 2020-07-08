package com.example.mapstracking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapstracking.DataNasabahActivity;
import com.example.mapstracking.Model.Nasabah;
import com.example.mapstracking.R;

import java.util.List;

public class ListNasabahAdapter extends RecyclerView.Adapter<ListNasabahAdapter.MyViewHolder>{

    List<Nasabah> nasabah;
    Context context;
    String menu;

    public ListNasabahAdapter(List<Nasabah> nasabah, Context context, String menu) {
        this.nasabah = nasabah;
        this.context = context;
        this.menu = menu;
    }

    @NonNull
    @Override
    public ListNasabahAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_nasabah, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNasabahAdapter.MyViewHolder holder, int position) {
        final Nasabah index = nasabah.get(position);
        holder.nama_nasabah.setText(nasabah.get(position).getNama_nasabah());
        String ttl = nasabah.get(position).getTempat_lahir_nasabah() + ", " + nasabah.get(position).getTanggal_lahir_nasabah();
        holder.ttl_nasabah.setText(ttl);
        holder.ktp_nasabah.setText(nasabah.get(position).getKtp_nasabah());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DataNasabahActivity.class);
                intent.putExtra("id_nasabah", index.getId_nasabah());
                intent.putExtra("nama_nasabah", index.getNama_nasabah());
                intent.putExtra("menu", menu);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nasabah.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nama_nasabah, ttl_nasabah, ktp_nasabah;
        ConstraintLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_nasabah = itemView.findViewById(R.id.tv_nama_nasabah);
            ttl_nasabah = itemView.findViewById(R.id.tv_ttl_nasabah);
            ktp_nasabah = itemView.findViewById(R.id.tv_ktp_nasabah);
            layout = itemView.findViewById(R.id.list_nasabah_container);
        }
    }
}
