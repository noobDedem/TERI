package id.fantasticfive.teri.dosen.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import id.fantasticfive.teri.dosen.adapters.BankTugasAdapter;
import id.fantasticfive.teri.dosen.adapters.KomentarAdapter;
import id.fantasticfive.teri.dosen.adapters.TugasAdapter;
import id.fantasticfive.teri.dosen.models.BankTugas;
import id.fantasticfive.teri.dosen.models.Komentar;

/**
 * Created by rindh27 on 11/6/2017.
 */

public class BankTugasActivity extends AppCompatActivity {

    private ArrayList<BankTugas> bankTugasArray = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mdividerItemDecoration;
    private String kodeMataKuliah, kodeTugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bank_tugas);
        Bundle extras = getIntent().getExtras();
        kodeMataKuliah = extras.getString("kodeMataKuliah");
        kodeTugas = extras.getString("kodeTugas");

        System.out.println(kodeMataKuliah);


        new SendBankData().execute();
        System.out.println(bankTugasArray);

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
        mAdapter = new BankTugasAdapter(getApplicationContext(), bankTugasArray);
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mRecyclerView);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public class SendBankData extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showBankTugasURL = getString(R.string.ip) + "/teri/show_banktugas2.php?kodeMataKuliah=" + kodeMataKuliah + "&kodeTugas=" + kodeTugas;
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
                        BankTugas bankTugas = new BankTugas(kodeMataKuliah, kodeTugas, namaFile, filePath);
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

}
