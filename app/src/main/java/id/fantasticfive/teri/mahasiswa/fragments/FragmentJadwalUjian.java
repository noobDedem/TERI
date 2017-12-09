package id.fantasticfive.teri.mahasiswa.fragments;

/**
 * Created by anism on 30/09/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import id.fantasticfive.teri.alarm.AlarmReceiver;
import id.fantasticfive.teri.mahasiswa.models.JadwalKuliah;
import id.fantasticfive.teri.mahasiswa.adapters.JadwalUjianAdapter;
import id.fantasticfive.teri.R;


public class FragmentJadwalUjian extends Fragment{

    private ArrayList<JadwalKuliah> jadwalKuliahArray = new ArrayList<>();
    private JadwalUjianAdapter adapter;
    private boolean flag;
    private Context context;
    private AlarmReceiver alarmReceiver;

    public FragmentJadwalUjian() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMatkul();
        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.content_jadwalkuliah, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view);

        adapter = new JadwalUjianAdapter(getContext(), jadwalKuliahArray);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        registerForContextMenu(rv);
        return v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int position = -1;
        try {
            position = adapter.getPositionx();
        } catch (Exception e) {
            e.printStackTrace();
            return super.onContextItemSelected(item);
        }

        if (item.getTitle().equals("Pasang Pengingat")) {
            if (jadwalKuliahArray.get(position).getNotifIDUjian() == 0) {
                int id = sharedPreferences.getInt("ID", 0);

                if (id == 0){
                    editor.putInt("ID", 1);
                    editor.commit();
                    jadwalKuliahArray.get(position).setNotifIDUjian(1);
                } else {
                    id += 1;
                    editor.putInt("ID", id);
                    editor.commit();
                    jadwalKuliahArray.get(position).setNotifIDUjian(id);
                }
            }

            /**
             * Buat masang alarm
             */
            alarmReceiver.setAlarmUjian(this.getActivity(), "Jadwal Ujian",
                    jadwalKuliahArray.get(position).getTanggalUjian(),
                    jadwalKuliahArray.get(position).getJamUjian(),
                    jadwalKuliahArray.get(position).getNamaMataKuliah(),
                    jadwalKuliahArray.get(position).getRuangUjian(),
                    jadwalKuliahArray.get(position).getNotifID());

            saveMatkulInSp(jadwalKuliahArray);

            return super.onContextItemSelected(item);

        } else if (item.getTitle().equals("Hapus Pengingat")) {
            if (jadwalKuliahArray.get(position).getNotifID() == 0) {
                Toast.makeText(this.getActivity(), "Pengingat belum diset", Toast.LENGTH_LONG);
            } else {
                /**
                 * Buat hapus alarm
                 */
                alarmReceiver.cancelAlarm(this.getActivity(), jadwalKuliahArray.get(position).getNotifID());
                jadwalKuliahArray.get(position).setNotifID(0);
                Toast.makeText(this.getActivity(), "Pengingat berhasil dihapus", Toast.LENGTH_LONG);
            }

            saveMatkulInSp(jadwalKuliahArray);

            return super.onContextItemSelected(item);
        }

        return super.onContextItemSelected(item);
    }

    private void saveMatkulInSp(ArrayList<JadwalKuliah> matkul){
        SharedPreferences preferences = context.getSharedPreferences("matkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(matkul);
        editor.clear();
        editor.putString("SelectedMatkul", json);
        editor.commit();
        adapter.notifyDataSetChanged();
    }

    private boolean kelolaMatkul() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("KelolaMatkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean flagMatkul = sharedPreferences.getBoolean("UJIAN", false);
        editor.putBoolean("UJIAN", false);
        editor.commit();
        return flagMatkul;
    }

    public void getMatkul() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("matkul", Context.MODE_PRIVATE);
        context = getActivity().getApplicationContext();
        Gson gson = new Gson();
        String json = preferences.getString("SelectedMatkul", null);
        Type type = new TypeToken<ArrayList<JadwalKuliah>>(){}.getType();
        jadwalKuliahArray = gson.fromJson(json, type);
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = kelolaMatkul();
        if (flag) {
            getMatkul();
            adapter.notifyDataSetChanged();
            flag = false;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}