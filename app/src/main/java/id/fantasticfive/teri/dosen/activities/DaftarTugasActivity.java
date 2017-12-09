package id.fantasticfive.teri.dosen.activities;

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
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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
import id.fantasticfive.teri.dosen.adapters.TugasAdapter;
import id.fantasticfive.teri.dosen.models.Tugas;

public class DaftarTugasActivity extends AppCompatActivity {

    private ArrayList<Tugas> tugasArray = new ArrayList<>();
    private ArrayList<Tugas> tugas = new ArrayList<>();
    private FloatingActionButton fab;
    private Button mButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mdividerItemDecoration;
    private Context mContext;
    private String kodeMatkul;

    public DaftarTugasActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_tugas);
        Bundle extras = getIntent().getExtras();
        kodeMatkul = extras.getString("kdmatkul");
        System.out.println(kodeMatkul);

        new AsyncFetch().execute();
        System.out.println(tugasArray);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_dtgs);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahTugasActivity.class);
                intent.putExtra("kdmatkul", kodeMatkul);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_dtgs);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mdividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mdividerItemDecoration);
        mAdapter = new TugasAdapter(getApplicationContext(), tugasArray);
        mRecyclerView.setAdapter(mAdapter);
    }


    private class AsyncFetch extends AsyncTask<Integer, Void, String> {
        HttpURLConnection conn;
        URL url = null;
        String showMatakuliahURL = getString(R.string.ip) + "/teri/show_tugas_dosen.php?kodeMatkul="+kodeMatkul;
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
                        String kodeTugas =jsonObject.getString("kodeTugas");
                        String namaMataKuliah = jsonObject.getString("namaMataKuliah");
                        String namaTugas = jsonObject.getString("nama");
                        String format = jsonObject.getString("format");
                        String deadline = jsonObject.getString("deadline");
                        String keterangan = jsonObject.getString("keterangan");
                        String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                        Tugas tugas = new Tugas(kodeTugas, namaTugas, format, deadline, keterangan, namaMataKuliah, kodeMataKuliah);
                        tugasArray.add(tugas);
                        System.out.println(tugasArray);
                    }

                    mAdapter.notifyDataSetChanged();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cekTugas()) {
            new AsyncFetch().execute();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean cekTugas() {
        SharedPreferences sharedPreferences = getSharedPreferences("Tugas", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean flag = sharedPreferences.getBoolean("Flag", false);
        editor.putBoolean("Flag", false);
        editor.commit();
        return flag;
    }
}



