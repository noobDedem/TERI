package id.fantasticfive.teri.dosen.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.dosen.activities.DaftarTugasActivity;
import id.fantasticfive.teri.dosen.models.MataKuliah;


/**
 * Created by anism on 06/11/2017.
 */

public class MataKuliahAdapter extends RecyclerView.Adapter<MataKuliahAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MataKuliah> matkulArrayList;

    TugasAdapter adapter;

    public MataKuliahAdapter(Context mContext, ArrayList<MataKuliah> matkulArrayList) {
        this.mContext = mContext;
        this.matkulArrayList = matkulArrayList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView namaMatkul;

        public MyViewHolder(View itemView) {
            super(itemView);
            namaMatkul = (TextView)itemView.findViewById(R.id.nama_matakuliah);
        }
    }

    @Override
    public MataKuliahAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tugas_dsn, parent, false);

        return new MataKuliahAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MataKuliahAdapter.MyViewHolder holder, final int position) {
        final MataKuliah matkul = matkulArrayList.get(position);
        holder.namaMatkul.setText(matkul.getNamaMataKuliah());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  int position = this.getPosition();

                String kdm = matkul.getKodeMataKuliah();
                System.out.println(kdm);
                Intent intent = new Intent(mContext, DaftarTugasActivity.class);
                intent.putExtra("kdmatkul", kdm);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return matkulArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }
}
