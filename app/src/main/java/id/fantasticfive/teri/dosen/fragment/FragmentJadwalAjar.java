package id.fantasticfive.teri.dosen.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.alarm.AlarmReceiver;
import id.fantasticfive.teri.dosen.models.JadwalAjar;
import id.fantasticfive.teri.dosen.adapters.JadwalAjarAdapter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by D-Y-U on 09/10/2017.
 */

public class FragmentJadwalAjar extends Fragment {

    private ArrayList<JadwalAjar> jadwalAjarArrayList = new ArrayList<>();
    private JadwalAjarAdapter adapter;
    private Context mContext;
    boolean flag;
    private AlarmReceiver alarmReceiver;

    public FragmentJadwalAjar() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cekJadwalAjar();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("JadwalAjarFlag", Context.MODE_PRIVATE);
        flag = sharedPreferences.getBoolean("flagJadwal", false);
        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (jadwalAjarArrayList == null) {
            jadwalAjarArrayList = new ArrayList<>();
            new AsyncFetch().execute();
            falseFlag();
        } else if (jadwalAjarArrayList.isEmpty()) {
            new AsyncFetch().execute();
            falseFlag();
        } else if (flag == true) {
            jadwalAjarArrayList.clear();
            new AsyncFetch().execute();
            falseFlag();
        }
        View v=inflater.inflate(R.layout.content_jadwalkuliah, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view);
        adapter = new JadwalAjarAdapter(getContext(), jadwalAjarArrayList);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        return rv;
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
            if (jadwalAjarArrayList.get(position).getNotifID() == 0) {
                int id = sharedPreferences.getInt("ID", 0);

                if (id == 0){
                    editor.putInt("ID", 1);
                    editor.commit();
                    jadwalAjarArrayList.get(position).setNotifID(1);
                } else {
                    id += 1;
                    editor.putInt("ID", id);
                    editor.commit();
                    jadwalAjarArrayList.get(position).setNotifID(id);
                }
            }

            /**
             * Buat masang alarm
             */
            alarmReceiver.setAlarm(this.getActivity(), "Jadwal Ajar",
                    jadwalAjarArrayList.get(position).getHari(),
                    jadwalAjarArrayList.get(position).getJam(),
                    jadwalAjarArrayList.get(position).getNamaMataKuliah(),
                    jadwalAjarArrayList.get(position).getRuang(),
                    jadwalAjarArrayList.get(position).getNotifID());

            saveUjianInSp(jadwalAjarArrayList);

            return super.onContextItemSelected(item);

        } else if (item.getTitle().equals("Hapus Pengingat")) {
            if (jadwalAjarArrayList.get(position).getNotifID() == 0) {
                Toast.makeText(this.getActivity(), "Pengingat belum diset", Toast.LENGTH_LONG);
            } else {
                /**
                 * Buat hapus alarm
                 */
                alarmReceiver.cancelAlarm(this.getActivity(), jadwalAjarArrayList.get(position).getNotifID());
                jadwalAjarArrayList.get(position).setNotifID(0);
                Toast.makeText(this.getActivity(), "Pengingat berhasil dihapus", Toast.LENGTH_LONG);
            }

            saveUjianInSp(jadwalAjarArrayList);

            return super.onContextItemSelected(item);
        }

        return super.onContextItemSelected(item);
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showJadwalAjarURL = getString(R.string.ip) + "/teri/show_jadwalAjar.php";
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(showJadwalAjarURL.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn  = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());
                } else {
                    return ("unsuccesfull");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            SharedPreferences myPrefs = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String nim = myPrefs.getString("NIM", null);
            System.out.println(nim);
            try {
                if (response_code == HttpURLConnection.HTTP_OK) {
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String namaMataKuliah = jsonObject.getString("namaMataKuliah");
                        String ruangMataKuliah = jsonObject.getString("ruang");
                        int hariKuliah = jsonObject.getInt("hari");
                        String jamKuliah = jsonObject.getString("jam");
                        String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                        String kelas = jsonObject.getString("kelas");
                        String user_nomorInduk = jsonObject.getString("User_nomorInduk");
                        String user_nomorInduk2 = jsonObject.getString("User_nomorInduk2");
                        if (user_nomorInduk.equals(nim) || user_nomorInduk2.equals(nim)) {
                            JadwalAjar jadwalAjar = new JadwalAjar(kodeMataKuliah, kelas, namaMataKuliah, hari(hariKuliah), jamKuliah, ruangMataKuliah, user_nomorInduk);
                            jadwalAjarArrayList.add(jadwalAjar);
                        }

                    }

                    saveUjianInSp(jadwalAjarArrayList);

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String hari(int x) {

        switch (x) {
            case 1 :
                return "Senin";
            case 2 :
                return "Selasa";
            case 3 :
                return "Rabu";
            case 4 :
                return "Kamis";
            case 5 :
                return "Jumat";
        }
        return null;
    }

    private void cekJadwalAjar() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("ajar", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("SelectedJadwal", null);
        Type type = new TypeToken<ArrayList<JadwalAjar>>() {}.getType();
        jadwalAjarArrayList = gson.fromJson(json, type);
    }

    private void falseFlag() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("JadwalAjarFlag", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flagJadwal", false);
        editor.commit();
        flag = false;
    }

    private void saveUjianInSp(ArrayList<JadwalAjar> ujian){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("ajar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ujian);
        editor.putString("SelectedJadwal", json);
        editor.commit();
    }

}