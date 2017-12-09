package id.fantasticfive.teri.mahasiswa.adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.activities.DetailDosenActivity;
import id.fantasticfive.teri.mahasiswa.models.Dosen;

/**
 * Created by Rindh27 & Alfifau on 09/10/2017.
 */

public class DosenAdapter extends RecyclerView.Adapter<DosenAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Dosen> dosenArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaDosen;

        public MyViewHolder(View view) {
            super(view);
            namaDosen = (TextView) view.findViewById(R.id.nama_dosen);

        }
    }


    public DosenAdapter(Context mContext, ArrayList<Dosen> DosenArrayList) {
        this.mContext = mContext;
        this.dosenArrayList = DosenArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dosen, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Dosen dosen = dosenArrayList.get(position);
        holder.namaDosen.setText(dosen.getNamaDosen());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dosen dosen = dosenArrayList.get(position);
                saveDosenInSP(dosen);
                Intent pindah = new Intent(mContext, DetailDosenActivity.class);
                mContext.startActivity(pindah);
            }
        });
    }

    public void saveDosenInSP (Dosen dosen){
        SharedPreferences sp = mContext.getSharedPreferences("Dosen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dosen);
        editor.putString("SelectedDosen",json);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        try {
            return dosenArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }
}
