package id.fantasticfive.teri.mahasiswa.fragments;

/**
 * Created by anism on 30/09/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import java.util.HashSet;
import java.util.Set;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.models.JadwalKuliah;
import id.fantasticfive.teri.mahasiswa.models.JadwalUjian;
import id.fantasticfive.teri.mahasiswa.models.Tugas;
import id.fantasticfive.teri.mahasiswa.adapters.TugasAdapter;


public class FragmentTugas extends Fragment{

    private ArrayList<Tugas> tugasArray = new ArrayList<>();
    private ArrayList<JadwalKuliah> jadwalKuliahArray = new ArrayList<>();
    private TugasAdapter adapter;
    private boolean flag;

    public FragmentTugas() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJadwalKuliah();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        new AsyncFetch().execute();

        View v=inflater.inflate(R.layout.content_tugas, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view);
        adapter = new TugasAdapter(getContext(), tugasArray);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        return v;
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showMatakuliahURL = getString(R.string.ip) + "/teri/show_tugas.php";
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(showMatakuliahURL);
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
            try {
                if (response_code == HttpURLConnection.HTTP_OK) {
                    tugasArray.clear();
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String kodeTugas = jsonObject.getString("kodeTugas");
                        String namaMataKuliah = jsonObject.getString("namaMataKuliah");
                        String namaTugas = jsonObject.getString("nama");
                        String format = jsonObject.getString("format");
                        String deadline = jsonObject.getString("deadline");
                        String keterangan = jsonObject.getString("keterangan");
                        String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                        String enrollmentKey = jsonObject.getString("enrollmentKey");
                        Tugas tugas = new Tugas(kodeTugas, namaTugas, format, deadline, keterangan, namaMataKuliah, kodeMataKuliah, enrollmentKey);
                        if (jadwalKuliahArray == null) {
                            jadwalKuliahArray = new ArrayList<>();
                        }
                        for (int j = 0; j < jadwalKuliahArray.size(); j++) {
                            if (jadwalKuliahArray.get(j).getNamaMataKuliah().equals(tugas.getNamaMatkul())) {
                                tugasArray.add(tugas);
                            }
                        }
                    }

                    deleteDuplicate();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void cekDaftarTugas() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("tugas", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("SelectedTugas", null);
        Type type = new TypeToken<ArrayList<Tugas>>() {}.getType();
        tugasArray = gson.fromJson(json, type);
    }

    private void getJadwalKuliah() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("matkul", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("SelectedMatkul", null);
        Type type = new TypeToken<ArrayList<JadwalKuliah>>() {}.getType();
        jadwalKuliahArray = gson.fromJson(json, type);
    }

    private void falseFlag() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("JadwalFlag", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flagTugas", false);
        editor.commit();
        flag = false;
    }

    private void saveTugasInSp(ArrayList<Tugas> tugas){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("tugas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tugas);
        editor.putString("SelectedTugas", json);
        editor.commit();
    }

    private void deleteDuplicate(){
        Set<Tugas> set = new HashSet<>();
        set.addAll(tugasArray);
        tugasArray.clear();
        tugasArray.addAll(set);
    }

    private boolean kelolaMatkul() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("KelolaMatkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean flagMatkul = sharedPreferences.getBoolean("TUGAS", false);
        editor.putBoolean("TUGAS", false);
        editor.commit();
        return flagMatkul;
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = kelolaMatkul();
        if (flag) {
            getJadwalKuliah();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}