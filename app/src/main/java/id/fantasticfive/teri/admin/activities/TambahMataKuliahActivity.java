package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 09/11/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import id.fantasticfive.teri.R;

public class TambahMataKuliahActivity extends AppCompatActivity {
    EditText mkode, mnama, menroll, mdosen1, mdosen2;
    Button msimpan;
    String kode, nama, enroll, dosen1, dosen2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mata_kuliah);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_km);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mkode = (EditText) findViewById(R.id.kode_matkul);
        mnama = (EditText)findViewById(R.id.nama_matkul);
        menroll = (EditText) findViewById(R.id.enroll_matkul);
        mdosen1 = (EditText) findViewById(R.id.dosen1_matkul);
        mdosen2 = (EditText) findViewById(R.id.dosen2_matkul);
        msimpan = (Button)findViewById(R.id.simpan_button);

        msimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kode = mkode.getText().toString();
                nama = mnama.getText().toString();
                enroll = menroll.getText().toString();
                dosen1 = mdosen1.getText().toString();
                dosen2 = mdosen2.getText().toString();
                if (kode.equals("") || nama.equals("") || enroll.equals("") || dosen1.equals("") || dosen2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        new TambahMataKuliahActivity.AsyncFetch(getApplicationContext()).execute();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    SharedPreferences preferences = getSharedPreferences("TambahMatkul", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("SIMPAN", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String addTugasUrl = getString(R.string.ip) + "/teri/add_matakuliah.php?kode="+kode+"&nama="+nama+"&enroll="+enroll+"&dosen1="+dosen1+"&dosen2="+dosen2;

        public AsyncFetch(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(addTugasUrl.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn  = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                int response_code = conn.getResponseCode();

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

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
