package id.fantasticfive.teri.dosen.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import id.fantasticfive.teri.dosen.adapters.KomentarAdapter;
import id.fantasticfive.teri.dosen.models.Komentar;

/**
 * Created by Demas on 11/6/2017.
 */

public class KomentarActivity extends AppCompatActivity {

    EditText komentar;
    ImageButton button;
    TextView namaUser, isiKomentar, waktu;
    RecyclerView rv;
    RecyclerView.ItemDecoration mdividerItemDecoration;
    ArrayList<Komentar> komentarArrayList;
    String kontenKomentar, nomorInduk, kodeTugas, idKomentar;
    KomentarAdapter komentarAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.komentar_activity);
        komentarArrayList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        kodeTugas = extras.getString("KODE_TUGAS");
        new ShowKomentar().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        komentar = (EditText)findViewById(R.id.edit_text_komentar);
        button = (ImageButton)findViewById(R.id.send_komentar);
        namaUser = (TextView)findViewById(R.id.user_komentar);
        isiKomentar = (TextView)findViewById(R.id.isi_komentar);
        waktu = (TextView)findViewById(R.id.tanggal_komentar);
        rv = (RecyclerView)findViewById(R.id.rv_komentar);

        registerForContextMenu(rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mdividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(mdividerItemDecoration);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kontenKomentar = komentar.getText().toString();
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                nomorInduk = sp.getString("NIM", null);
                new SendKomentar().execute();
                komentar.setText("");
            }
        });

        komentarAdapter = new KomentarAdapter(this, komentarArrayList);
        rv.setAdapter(komentarAdapter);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = komentarAdapter.getPositionx();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String nomorInduk = sp.getString("NIM", null);
        String nomorIndukList = komentarArrayList.get(position).getIdUser();

        if (nomorInduk.equals(komentarArrayList.get(position).getIdUser())) {
            idKomentar = komentarArrayList.get(position).getIdKomentar();
            new DeleteKomentar().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Anda Tidak Dapat Menghapus Komentar Tersebut", Toast.LENGTH_LONG).show();
        }

        return super.onContextItemSelected(item);
    }

    public class SendKomentar extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String userURL = getString(R.string.ip) + "/teri/insert_komentar.php?isiKomentar=" + kontenKomentar + "&nomorInduk=" + nomorInduk + "&kodeTugas=" + kodeTugas;
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(userURL.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
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
            if (response_code == HttpURLConnection.HTTP_OK) {
                new ShowKomentar().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class ShowKomentar extends AsyncTask<Integer, Void, String>{

        HttpURLConnection conn;
        URL url = null;
        String userURL = getString(R.string.ip) + "/teri/show_komentar.php?kodeTugas=" +  kodeTugas;
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(userURL.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
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
                    komentarArrayList.clear();
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idKomentar = jsonObject.getString("idKomentar");
                        String idUser = jsonObject.getString("nomorInduk");
                        String namaUser = jsonObject.getString("nama");
                        String isiKomentar = jsonObject.getString("isiKomentar");
                        String waktu = jsonObject.getString("time");
                        Komentar komentar = new Komentar(idKomentar, idUser, isiKomentar, namaUser, waktu);
                        komentarArrayList.add(komentar);
                    }
                    komentarAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DeleteKomentar extends AsyncTask<Integer, Void, String>{

        HttpURLConnection conn;
        URL url = null;
        String userURL = getString(R.string.ip) + "/teri/delete_komentar.php?idKomentar=" +  idKomentar;
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(userURL.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
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
            if (response_code == HttpURLConnection.HTTP_OK) {
                new ShowKomentar().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
