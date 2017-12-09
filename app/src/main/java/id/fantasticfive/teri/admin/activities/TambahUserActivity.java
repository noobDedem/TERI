package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 20/10/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class TambahUserActivity extends AppCompatActivity {
    EditText mnama, mnip, malamat, mtelepon, memail, mpassword;
    Button msimpan, mbatal;
    String nama, nip, alamat, telepon, email, level, password;
    Spinner spinner;
    String[] lvl = new String[]{"Admin", "Dosen", "Mahasiswa"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengguna);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_km);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mnama = (EditText)findViewById(R.id.nama_user);
        mnip = (EditText) findViewById(R.id.nomor_user);
        malamat = (EditText)findViewById(R.id.alamat_user);
        mtelepon = (EditText) findViewById(R.id.tel_user);
        memail = (EditText)findViewById(R.id.email_user);
        mpassword = (EditText) findViewById(R.id.password_user);
        msimpan = (Button)findViewById(R.id.simpan_button);
        spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lvl);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = convertLevel(lvl[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        msimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = mnama.getText().toString();
                nip = mnip.getText().toString();
                alamat = malamat.getText().toString();
                telepon = mtelepon.getText().toString();
                email = memail.getText().toString();
//                level = mlevel.getText().toString();
                password = mpassword.getText().toString();
                if (nama.equals("") || nip.equals("") || alamat.equals("") || telepon.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(getApplication(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        new AsyncFetch().execute();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    SharedPreferences preferences = getSharedPreferences("TambahUser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("SIMPAN", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String addTugasUrl = getString(R.string.ip) + "/teri/add_user.php?nama="+nama+"&nip="+nip+"&alamat="+alamat+"&telepon="+telepon+"&email="+email+"&level="+level+"&password="+password;

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

    public String convertLevel(String panjang){
        String returned = "";
        switch (panjang) {
            case "Admin" : returned = "0"; break;
            case "Dosen" : returned = "1"; break;
            case "Mahasiswa" : returned = "2"; break;
        }
        return returned;
    }
}
