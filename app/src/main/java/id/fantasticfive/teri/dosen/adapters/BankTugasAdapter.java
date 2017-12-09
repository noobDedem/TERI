package id.fantasticfive.teri.dosen.adapters;

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
import id.fantasticfive.teri.dosen.activities.DetailTugasActivity;
import id.fantasticfive.teri.dosen.models.BankTugas;
import id.fantasticfive.teri.dosen.models.Komentar;
import id.fantasticfive.teri.dosen.models.Tugas;

/**
 * Created by rinadh27 on 11/6/2017.
 */

public class BankTugasAdapter extends RecyclerView.Adapter<BankTugasAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<BankTugas> bankTugasArrayList;
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
            contextMenu.add(0, view.getId(), 0, "Delete");
        }
    }

    public BankTugasAdapter(Context mContext, ArrayList<BankTugas> bankTugasArrayList) {
        this.mContext = mContext;
        this.bankTugasArrayList = bankTugasArrayList;
    }

    @Override
    public BankTugasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_bank_tugas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BankTugasAdapter.MyViewHolder holder, final int position) {
        holder.namaFile.setText(bankTugasArrayList.get(position).getNamaFile());
        holder.itemView.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BankTugas bankTugas = bankTugasArrayList.get(position);
//                saveBankTugasInSP (bankTugas);
                String url = bankTugasArrayList.get(position).getFilePath();
                Log.e("File Path", url);
                Intent pindah = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (pindah.resolveActivity(mContext.getPackageManager()) != null) {
                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(pindah);
                }
//                pindah.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void saveBankTugasInSP(BankTugas bankTugas) {
        SharedPreferences sp = mContext.getSharedPreferences("TugasDosen",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bankTugas);
        editor.putString("bankTugas",json);
        editor.commit();
    }


}
