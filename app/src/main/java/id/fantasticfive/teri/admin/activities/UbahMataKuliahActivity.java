package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 09/11/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.adapters.UserAdapter;
import id.fantasticfive.teri.admin.models.MataKuliah;

public class UbahMataKuliahActivity extends AppCompatActivity {
    EditText mnama, mkode, menroll, mdosen1, mdosen2;
    String nama, kode, enroll, dosen1, dosen2, kodematkul;
    Button mhapus, msimpan;
    UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_mata_kuliah);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_km);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Matkul", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedMatkul","");
        MataKuliah mataKuliah= gson.fromJson(json, MataKuliah.class);
        kodematkul = mataKuliah.getKodeMataKuliah();

        mkode = (EditText) findViewById(R.id.kode_matkul);
        mkode.setText(mataKuliah.getKodeMataKuliah());
        mnama = (EditText) findViewById(R.id.nama_matkul);
        mnama.setText(mataKuliah.getNamaMataKuliah());
        menroll = (EditText) findViewById(R.id.enroll_matkul);
        menroll.setText(mataKuliah.getEnrollment());
        mdosen1 = (EditText) findViewById(R.id.dosen1_matkul);
        mdosen1.setText(mataKuliah.getDosenPengampu1());
        mdosen2 = (EditText) findViewById(R.id.dosen2_matkul);
        mdosen2.setText(mataKuliah.getDosenPengampu2());

        msimpan = (Button)findViewById(R.id.simpan_button);
        mhapus = (Button)findViewById(R.id.hapus_button);

        msimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = mnama.getText().toString();
                enroll = menroll.getText().toString();
                dosen1 = mdosen1.getText().toString();
                dosen2 = mdosen2.getText().toString();
                if (nama.equals("") || enroll.equals("") || dosen1.equals("") || dosen2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua form", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        new AsyncFetch().execute();
                        SharedPreferences preferences = getSharedPreferences("TambahMatkul", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("SIMPAN", true);
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_LONG).show();
                        finish();
                        DetailMataKuliahActivity.detailMataKuliahActivity.finish();
                    }catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Gagal mengubah data", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Hapus");
        builder.setMessage("Hapus "+ nama+"?");
        builder.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new AsyncFetch2().execute();
                            SharedPreferences preferences = getSharedPreferences("TambahMatkul", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("SIMPAN", true);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Data berhasil dihapus", Toast.LENGTH_LONG).show();
                            finish();
                            DetailMataKuliahActivity.detailMataKuliahActivity.finish();
                        }catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal menghapus data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        mhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }



    class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/ubah_matkul.php?kode="+kodematkul+"&nama="+nama+"&dosen1="+dosen1+"&dosen2="+dosen2+"&enroll="+enroll;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(editTugasUrl.replaceAll(" ", "%20"));
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

    class AsyncFetch2 extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/hapus_matkul.php?kode="+kodematkul;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(editTugasUrl.replaceAll(" ", "%20"));
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
