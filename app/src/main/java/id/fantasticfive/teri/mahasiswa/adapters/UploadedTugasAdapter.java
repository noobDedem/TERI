package id.fantasticfive.teri.mahasiswa.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.models.UploadedTugas;

/**
 * Created by Demas on 11/21/2017.
 */

public class UploadedTugasAdapter extends RecyclerView.Adapter<UploadedTugasAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<UploadedTugas> bankTugasArrayList;
    private int positionx;

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView namaFile;

        public MyViewHolder(View view) {
            super(view);
            namaFile = (TextView)view.findViewById(R.id.namaFile);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("HAI");
            contextMenu.add(0, view.getId(), 0, "Hapus");
        }
    }

    public UploadedTugasAdapter(Context mContext, ArrayList<UploadedTugas> bankTugasArrayList) {
        this.mContext = mContext;
        this.bankTugasArrayList = bankTugasArrayList;
    }

    @Override
    public UploadedTugasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_uploaded_tugas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UploadedTugasAdapter.MyViewHolder holder, final int position) {
        holder.namaFile.setText(bankTugasArrayList.get(position).getNamaFile());
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
            return bankTugasArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }

    private void saveBankTugasInSP(UploadedTugas bankTugas) {
        SharedPreferences sp = mContext.getSharedPreferences("TugasDosen",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bankTugas);
        editor.putString("bankTugas",json);
        editor.commit();
    }


}
