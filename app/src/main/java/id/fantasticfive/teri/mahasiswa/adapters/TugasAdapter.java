package id.fantasticfive.teri.mahasiswa.adapters;

/**
 * Created by Demas on 9/17/2017.
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
import id.fantasticfive.teri.mahasiswa.activities.DetailTugasActivity;
import id.fantasticfive.teri.mahasiswa.models.Tugas;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Tugas> tugasArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, format, deadline, namaMatkul;

        public MyViewHolder(View view) {
            super(view);
            namaMatkul = (TextView) view.findViewById(R.id.matkul_tugas);
            nama = (TextView) view.findViewById(R.id.nama_tugas);
            deadline = (TextView) view.findViewById(R.id.deadline);
        }
    }


    public TugasAdapter(Context mContext, ArrayList<Tugas> tugasArrayList) {
        this.mContext = mContext;
        this.tugasArrayList = tugasArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tugas, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Tugas tugas = tugasArrayList.get(position);
        holder.namaMatkul.setText(tugas.getNamaMatkul());
        holder.nama.setText(tugas.getNama());
        holder.deadline.setText(tugas.getDeadline());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tugas tugas = tugasArrayList.get(position);
                saveTugasInSP (tugas);
                Intent pindah = new Intent(mContext, DetailTugasActivity.class);
                pindah.putExtra("KODE_TUGAS", tugas.getKodeTugas());
                pindah.putExtra("ENROLLMENTKEY", tugas.getEnrollmentKey());
                mContext.startActivity(pindah);
            }
        });
    }

    public void saveTugasInSP (Tugas tugas){
        SharedPreferences sp = mContext.getSharedPreferences("Tugas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tugas);
        editor.putString("SelectedTugas",json);
        editor.commit();
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
}
