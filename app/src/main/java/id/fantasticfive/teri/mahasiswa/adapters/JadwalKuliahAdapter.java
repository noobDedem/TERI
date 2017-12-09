package id.fantasticfive.teri.mahasiswa.adapters;

/**
 * Created by Demas on 9/17/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.models.JadwalKuliah;

public class JadwalKuliahAdapter extends RecyclerView.Adapter<JadwalKuliahAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<JadwalKuliah> jadwalKuliahArrayList;
    private int positionx;

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView namaMatkul, waktu, ruang, hari;

        public MyViewHolder(View view) {
            super(view);
            namaMatkul = (TextView) view.findViewById(R.id.nama_matkul);
            waktu = (TextView) view.findViewById(R.id.waktu);
            ruang = (TextView) view.findViewById(R.id.ruang);
            hari = (TextView) view.findViewById(R.id.jadwal_hari);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, view.getId(), 0, "Pasang Pengingat");
            contextMenu.add(0, view.getId(), 0, "Hapus Pengingat");
        }
    }


    public JadwalKuliahAdapter(Context mContext, ArrayList<JadwalKuliah> jadwalKuliahArrayList) {
        this.mContext = mContext;
        this.jadwalKuliahArrayList = jadwalKuliahArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_jadwalkuliah, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        JadwalKuliah jadwalKuliah = jadwalKuliahArrayList.get(position);
        holder.namaMatkul.setText(jadwalKuliah.getNamaMataKuliah());
        holder.waktu.setText(jadwalKuliah.getWaktuKuliah());
        holder.ruang.setText(jadwalKuliah.getRuangMataKuliah());
        holder.hari.setText(jadwalKuliah.getHariMataKuliah());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPositionx(holder.getPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return jadwalKuliahArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }
}
