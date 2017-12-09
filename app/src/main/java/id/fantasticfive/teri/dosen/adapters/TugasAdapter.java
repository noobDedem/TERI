package id.fantasticfive.teri.dosen.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.dosen.activities.DetailTugasActivity;
import id.fantasticfive.teri.dosen.models.Tugas;


/**
 * Created by anism on 07/11/2017.
 */

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Tugas> tugasArrayList;
    private int positionx;
    static View v;

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  namaTugas;

        public MyViewHolder(View view) {
            super(view);
            mContext = itemView.getContext();
            namaTugas = (TextView) view.findViewById(R.id.namatugas);
        }
    }


    public TugasAdapter(Context mContext, ArrayList<Tugas> tugasArrayList) {
        this.mContext = mContext;
        this.tugasArrayList = tugasArrayList;
    }

    @Override
    public TugasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_daftartugas, parent, false);

        return new TugasAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TugasAdapter.MyViewHolder holder, final int position) {
        Tugas tugas = tugasArrayList.get(position);
        holder.namaTugas.setText(tugas.getNama());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tugas tugas = tugasArrayList.get(position);
                saveTugasInSP (tugas);
                Intent pindah = new Intent(mContext, DetailTugasActivity.class);
                mContext.startActivity(pindah);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return tugasArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }

    public void saveTugasInSP (Tugas tugas){
        SharedPreferences sp = mContext.getSharedPreferences("TugasDosen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tugas);
        editor.putString("detailTugas",json);
        editor.commit();
    }

}
