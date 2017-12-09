package id.fantasticfive.teri.mahasiswa.adapters;

/**
 * Created by anism on 09/10/2017.
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

public class JadwalUjianAdapter extends RecyclerView.Adapter<JadwalUjianAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<JadwalKuliah> jadwalUjianArrayList;
    private int positionx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView namaMatkul, jamUjian, ruangUjian, tanggalUjian;

        public MyViewHolder(View view) {
            super(view);
            namaMatkul = (TextView) view.findViewById(R.id.nama_matkul);
            jamUjian = (TextView) view.findViewById(R.id.jam_ujian);
            ruangUjian = (TextView) view.findViewById(R.id.ruang_ujian);
            tanggalUjian = (TextView) view.findViewById(R.id.tanggal_ujian);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, view.getId(), 0, "Pasang Pengingat");
            contextMenu.add(0, view.getId(), 0, "Hapus Pengingat");
        }
    }

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public JadwalUjianAdapter(Context mContext, ArrayList<JadwalKuliah> jadwalUjianArrayList) {
        this.mContext = mContext;
        this.jadwalUjianArrayList = jadwalUjianArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_jadwalujian, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        JadwalKuliah jadwalUjian = jadwalUjianArrayList.get(position);
        holder.namaMatkul.setText(jadwalUjian.getNamaMataKuliah());
        holder.jamUjian.setText(jadwalUjian.getJamUjian());
        holder.ruangUjian.setText(jadwalUjian.getRuangUjian());
        holder.tanggalUjian.setText(jadwalUjian.getTanggalUjian());
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
            return jadwalUjianArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }
}

