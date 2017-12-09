package id.fantasticfive.teri.admin.adapters;

/**
 * Created by alfifau on 09/10/2017.
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
import id.fantasticfive.teri.admin.activities.DetailMataKuliahActivity;
import id.fantasticfive.teri.admin.models.MataKuliah;

public class MataKuliahAdapter extends RecyclerView.Adapter<MataKuliahAdapter.MyViewHolder> {

    private Context mContext;
    public ArrayList<MataKuliah> mataKuliahArrayList;
    public static MataKuliahAdapter mka;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kodeMatkul, namaMatkul,enrollment;

        public MyViewHolder(View view) {
            super(view);
            kodeMatkul = (TextView) view.findViewById(R.id.kode_matkul);
            namaMatkul = (TextView) view.findViewById(R.id.nama_matkul);
        }
    }


    public MataKuliahAdapter(Context mContext, ArrayList<MataKuliah> mataKuliahArrayList) {
        this.mContext = mContext;
        this.mataKuliahArrayList = mataKuliahArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mata_kuliah, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MataKuliah mataKuliah = mataKuliahArrayList.get(position);
        holder.kodeMatkul.setText(mataKuliah.getKodeMataKuliah());
        holder.namaMatkul.setText(mataKuliah.getNamaMataKuliah());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMatkulInSP(mataKuliah);
                Intent pindah = new Intent(mContext, DetailMataKuliahActivity.class);
                mContext.startActivity(pindah);
            }
        });

    }

    public void saveMatkulInSP (MataKuliah mataKuliah){
        SharedPreferences sp = mContext.getSharedPreferences("Matkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mataKuliah);
        editor.putString("SelectedMatkul",json);
        editor.commit();
    }


    @Override
    public int getItemCount() {
        try {
            return mataKuliahArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }
}
