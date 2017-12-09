package id.fantasticfive.teri.mahasiswa.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import id.fantasticfive.teri.mahasiswa.adapters.UploadedTugasAdapter;
import id.fantasticfive.teri.mahasiswa.models.UploadedTugas;

/**
 * Created by Demas on 11/21/2017.
 */

public class UploadedTugasActivity extends AppCompatActivity {

    private ArrayList<UploadedTugas> bankTugasArray = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private UploadedTugasAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mdividerItemDecoration;
    private String kodeMataKuliah, kodeTugas, nim;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bank_tugas);

        Bundle extras = getIntent().getExtras();
        kodeMataKuliah = extras.getString("kodeMataKuliah");
        kodeTugas = extras.getString("kodeTugas");

        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        nim = preferences.getString("NIM", "");

        new SendBankData().execute();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_banktgs);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_banktgs);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mdividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mdividerItemDecoration);
        mAdapter = new UploadedTugasAdapter(getApplicationContext(), bankTugasArray);
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mRecyclerView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = mAdapter.getPositionx();
        } catch (Exception e) {
            e.printStackTrace();
            return super.onContextItemSelected(item);
        }

        if (item.getTitle() == "Hapus") {
            id = bankTugasArray.get(position).getId();
            try {
                new DeleteData().execute();
                SharedPreferences preferences = getSharedPreferences("Uploaded", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("DELETE", true);
                editor.commit();
                new SendBankData().execute();
                Toast.makeText(getApplicationContext(), "Berhasil menghapus data", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Gagal menhapus data", Toast.LENGTH_LONG).show();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class SendBankData extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showBankTugasURL = getString(R.string.ip) + "/teri/show_uploaded_tugas.php?kodeMataKuliah=" + kodeMataKuliah + "&kodeTugas=" + kodeTugas + "&nomorInduk=" + nim;
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(showBankTugasURL.replaceAll(" ", "%20"));
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
                    bankTugasArray.clear();
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                        String kodeTugas = jsonObject.getString("kodeTugas");
                        String namaFile = jsonObject.getString("namaFile");
                        String filePath = jsonObject.getString("filePath");
                        int id = jsonObject.getInt("id");
                        UploadedTugas bankTugas = new UploadedTugas(kodeMataKuliah, kodeTugas, namaFile, filePath, id);
                        bankTugasArray.add(bankTugas);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DeleteData extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showBankTugasURL = getString(R.string.ip) + "/teri/delete_uploaded_tugas.php?id=" + id;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(showBankTugasURL.replaceAll(" ", "%20"));
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

        @Override
        protected void onPostExecute(String s) {
            try {
                bankTugasArray.clear();
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                    String kodeTugas = jsonObject.getString("kodeTugas");
                    //String nama = jsonObject.getString("nama");
                    //int nim = jsonObject.getInt("nim");
                    String namaFile = jsonObject.getString("namaFile");
                    String filePath = jsonObject.getString("filePath");
                    int id = jsonObject.getInt("id");
                    UploadedTugas bankTugas = new UploadedTugas(kodeMataKuliah, kodeTugas, namaFile, filePath, id);
                    bankTugasArray.add(bankTugas);
                }

                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
