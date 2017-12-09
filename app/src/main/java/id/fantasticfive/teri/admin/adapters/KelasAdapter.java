package id.fantasticfive.teri.admin.adapters;

/**
 * Created by alfiau on 14/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.activities.UbahKelasActivity;
import id.fantasticfive.teri.admin.models.Kelas;


public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Kelas> kelasArrayList;
    private int positionx;
    static View v;

    public int getPositionx(){
        return positionx;
    }

    public void setPositionx (int positionx){
        this.positionx = positionx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKelas;

        public MyViewHolder (View view){
            super(view);
            mContext = itemView.getContext();
            namaKelas = (TextView) view.findViewById(R.id.namakelas);
        }
    }

    public KelasAdapter (Context mContext, ArrayList<Kelas> kelasArrayList){
        this.mContext = mContext;
        this.kelasArrayList = kelasArrayList;
    }

    @Override
    public KelasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_kelas, parent, false);

        return new KelasAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(KelasAdapter.MyViewHolder holder, final int position) {
        Kelas kelas = kelasArrayList.get(position);
        holder.namaKelas.setText(kelas.getNamaKelas());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kelas kelas = kelasArrayList.get(position);
                saveKelasInSP (kelas);
                Intent pindah = new Intent(mContext, UbahKelasActivity.class);
                mContext.startActivity(pindah);
            }
        });
    }

    public void saveKelasInSP (Kelas kelas){
        SharedPreferences sp = mContext.getSharedPreferences("Kelas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(kelas);
        editor.putString("SelectedKelas",json);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        try {
            return kelasArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }


}
