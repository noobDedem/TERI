package id.fantasticfive.teri.mahasiswa.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.adapters.KelolaMataKuliahAdapter;
import id.fantasticfive.teri.mahasiswa.models.JadwalKuliah;
import id.fantasticfive.teri.mahasiswa.models.JadwalUjian;

/**
 * Created by anism on 28/10/2017.
 */

public class KelolaMataKuliahActivity extends AppCompatActivity {

    private ArrayList<JadwalKuliah> jadwalKuliahArray = new ArrayList<>();
    private ArrayList<JadwalUjian> jadwalUjianArray = new ArrayList<>();
    KelolaMataKuliahAdapter adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mdividerItemDecoration;
    private Context mContext;

    public KelolaMataKuliahActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kelolamatkul);
        new AsyncFetch().execute();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_km);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mdividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mdividerItemDecoration);
        mAdapter = new KelolaMataKuliahAdapter(getApplicationContext(), jadwalKuliahArray);
        mRecyclerView.setAdapter(mAdapter);
    }


    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showMatakuliahURL = getString(R.string.ip) + "/teri/show_jadwal2.php";
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            if (jadwalKuliahArray.isEmpty()) {
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (jadwalKuliahArray.isEmpty()) {
                try {
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        JSONArray jsonArray = new JSONArray(s);

                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String namaMataKuliah = jsonObject.getString("namaMataKuliah");
                            String ruangMataKuliah = jsonObject.getString("ruang");
                            int hariKuliah = jsonObject.getInt("hari");
                            String kelas = jsonObject.getString("kelas");
                            String jamUjian = jsonObject.getString("jam_ujian");
                            String ruangUjian = jsonObject.getString("ruang_ujian");
                            String tanggalUjian = jsonObject.getString("tanggal");
                            String jamKuliah = jsonObject.getString("jam");
                            JadwalKuliah jadwalKuliah = new JadwalKuliah(namaMataKuliah, ruangMataKuliah, hari(hariKuliah), jamKuliah, kelas, tanggalUjian, jamUjian, ruangUjian);
                            jadwalKuliahArray.add(jadwalKuliah);
                        }

                        mAdapter.notifyDataSetChanged();
                        SharedPreferences sharedPreferences = getSharedPreferences("JadwalFlag", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("flagUjian", true);
                        editor.putBoolean("flagTugas", true);
                        editor.commit();
                    } else {
                        Toast.makeText(KelolaMataKuliahActivity.this, "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("KelolaMatkul", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SIMPAN", true);
        editor.putBoolean("UJIAN", true);
        editor.putBoolean("TUGAS", true);
        editor.commit();

        finish();
    }
}