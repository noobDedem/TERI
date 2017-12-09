package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 14/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.HashSet;
import java.util.Set;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.adapters.KelasAdapter;
import id.fantasticfive.teri.admin.models.Kelas;
import id.fantasticfive.teri.admin.models.MataKuliah;

public class DaftarKelasActivity extends AppCompatActivity {

    private ArrayList<Kelas> kelasArray = new ArrayList<>();
    private ArrayList<Kelas> kelas = new ArrayList<>();
    private FloatingActionButton fab;
    private Button mButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mdividerItemDecoration;
    private Context mContext;
    private String kodematkul;

    public DaftarKelasActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kelas);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Matkul", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedMatkul","");
        MataKuliah mataKuliah = gson.fromJson(json, MataKuliah.class);
        kodematkul = mataKuliah.getKodeMataKuliah();

        new DaftarKelasActivity.AsyncFetch().execute();
        System.out.println(kelasArray);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahKelasActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mdividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mdividerItemDecoration);

        mAdapter = new KelasAdapter(getApplicationContext(), kelasArray);
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mRecyclerView);
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {
        HttpURLConnection conn;
        URL url = null;
        String showMatakuliahURL = getString(R.string.ip) + "/teri/show_kelas.php?kodeMatkul="+kodematkul;
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(showMatakuliahURL.replaceAll(" ", "%20"));
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
//            if (kelasArray.isEmpty()) {
//            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (response_code == HttpURLConnection.HTTP_OK) {
                    kelasArray.clear();
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String namaKelas = jsonObject.getString("kelas");
                        String hariKuliah = jsonObject.getString("hari");
                        String jamKuliah = jsonObject.getString("jam");
                        String ruangKuliah = jsonObject.getString("ruang");
                        String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                        String tanggalUjian = jsonObject.getString("tanggal");
                        String jamUjian = jsonObject.getString("jam_ujian");
                        String ruangUjian = jsonObject.getString("ruang_ujian");
                        String idKuliah = jsonObject.getString("id_kuliah");
                        String idUjian = jsonObject.getString("id_ujian");
                        Kelas kelas = new Kelas(namaKelas, kodeMataKuliah, hariKuliah, jamKuliah, ruangKuliah, tanggalUjian, jamUjian, ruangUjian, idKuliah, idUjian);
                        kelasArray.add(kelas);
                    }
                    ArrayList duplicate = new ArrayList();
                    Set attribute = new HashSet();
                    for (Kelas kls : kelasArray) {
                        if (attribute.contains(kls.getNamaKelas())) {
                            duplicate.add(kls);
                        }
                        attribute.add(kls.getNamaKelas());
                    }

                    kelasArray.removeAll(duplicate);

                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DaftarKelasActivity.this, "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (kelasArray.isEmpty()) {
//            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean cekKelas() {
        SharedPreferences preferences = getSharedPreferences("TambahKelas", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean flag = preferences.getBoolean("SIMPAN", false);
        editor.putBoolean("SIMPAN", false);
        editor.commit();
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cekKelas()) {
            new AsyncFetch().execute();
            mAdapter.notifyDataSetChanged();
        }
    }
}
