package id.fantasticfive.teri.dosen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.dosen.models.Komentar;

/**
 * Created by Demas on 11/6/2017.
 */

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Komentar> komentarArrayList;
    private int positionx;

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView namaUser, isiKomentar, waktu;

        public MyViewHolder(View view) {
            super(view);
            namaUser = (TextView)view.findViewById(R.id.user_komentar);
            isiKomentar = (TextView)view.findViewById(R.id.isi_komentar);
            waktu = (TextView)view.findViewById(R.id.tanggal_komentar);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("HAI");
            contextMenu.add(0, view.getId(), 0, "Delete");
        }
    }

    public KomentarAdapter(Context mContext, ArrayList<Komentar> komentarArrayList) {
        this.mContext = mContext;
        this.komentarArrayList = komentarArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_list_komentar, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.namaUser.setText(komentarArrayList.get(position).getNamaUser());
        holder.isiKomentar.setText(komentarArrayList.get(position).getIsiKomentar());
        holder.waktu.setText(komentarArrayList.get(position).getWaktu());
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
        return komentarArrayList.size();
    }
}
