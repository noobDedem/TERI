package id.fantasticfive.teri.mahasiswa.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.models.JadwalKuliah;
import id.fantasticfive.teri.mahasiswa.models.JadwalUjian;


/**
 * Created by anism on 28/10/2017.
 */

public class KelolaMataKuliahAdapter  extends RecyclerView.Adapter<KelolaMataKuliahAdapter.MyViewHolder> {
    private ArrayList<JadwalKuliah> mataKuliahArrayList;
    private Map<String,JadwalKuliah> selected = new HashMap<>();
    static View v;


    public KelolaMataKuliahAdapter(Context context, ArrayList<JadwalKuliah> mataKuliahArrayList) {
        this.mataKuliahArrayList = mataKuliahArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView namaMatkul;
        private CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            namaMatkul = (TextView) view.findViewById(R.id.matakuliah);
            checkBox = (CheckBox) view.findViewById(R.id.check_Box);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_kelolamatkul, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final JadwalKuliah matkul = mataKuliahArrayList.get(position);
        holder.namaMatkul.setText(matkul.getNamaMataKuliah() + " " + matkul.getKelas());
        holder.checkBox.setChecked(getFromSP(String.valueOf(position),matkul));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveInSp(String.valueOf(position),isChecked);
                if (isChecked){
                    selected.put(String.valueOf(position),matkul);
                    Toast.makeText(v.getContext(), "Menambahkan " + holder.namaMatkul.getText(), Toast.LENGTH_LONG).show();
                } else{
                    selected.remove(String.valueOf(position));
                    Toast.makeText(v.getContext(), "Menghapus " + holder.namaMatkul.getText(), Toast.LENGTH_LONG).show();
                }
                saveMatkulInSp(getSelectedMataKuliah());
            }
        });

    }

    private boolean getFromSP(String key, JadwalKuliah matkul){
        SharedPreferences preferences = v.getContext().getSharedPreferences("matkul", Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        if(value) {
            selected.put(key, matkul);
        }
        return value;
    }

    private void saveInSp(String key,boolean value){
        SharedPreferences preferences = v.getContext().getSharedPreferences("matkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public ArrayList<JadwalKuliah> getSelectedMataKuliah (){
        ArrayList<JadwalKuliah> select = new ArrayList<>();
        Set<Map.Entry<String, JadwalKuliah>> entrySet = this.selected.entrySet();
        for(Map.Entry<String, JadwalKuliah> entry : entrySet) {
            String key = entry.getKey();
            JadwalKuliah value = entry.getValue();
            select.add(value);
        }
        return select;
    }

    private void saveMatkulInSp(ArrayList<JadwalKuliah> matkul){
        SharedPreferences preferences = v.getContext().getSharedPreferences("matkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(matkul);
        editor.putString("SelectedMatkul", json);
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
