package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 20/10/2017.
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import id.fantasticfive.teri.admin.models.User;

public class UbahUserActivity extends AppCompatActivity {
    EditText mnama, malamat, mtelepon, memail, mpassword, mnomor;
    String nama, nomor, alamat, telepon, email, level, password,nomorinduk;
    Button msimpan, mhapus;
    UserAdapter mAdapter;
    Spinner spinner;
    String[] lvl = new String[]{"Admin", "Dosen", "Mahasiswa"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_km);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedUser","");
        final User user = gson.fromJson(json, User.class);
        nomorinduk = user.getNipUser();

        mnama = (EditText)findViewById(R.id.nama_user);
        mnama.setText(user.getNamaUser());
        mnomor = (EditText) findViewById(R.id.nomor_user);
        mnomor.setText(user.getNipUser());
        malamat = (EditText)findViewById(R.id.alamat_user);
        malamat.setText(user.getAlamatUser());
        mtelepon = (EditText) findViewById(R.id.tel_user);
        mtelepon.setText(user.getTeleponUser());
        memail = (EditText)findViewById(R.id.email_user);
        memail.setText(user.getEmailUser());
        mpassword = (EditText) findViewById(R.id.password_user);
        mpassword.setText(user.getPassUser());

        msimpan = (Button)findViewById(R.id.simpan_button);
        mhapus = (Button)findViewById(R.id.hapus_button);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lvl);
        spinner.setAdapter(adapter);
        spinner.setSelection(convertBack(user.getLevelUser()));
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
                nomor = mnomor.getText().toString();
                alamat = malamat.getText().toString();
                telepon = mtelepon.getText().toString();
                email = memail.getText().toString();
                password = mpassword.getText().toString();
                if (nama.equals("") || nomor.equals("") || alamat.equals("") || telepon.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        new AsyncFetch().execute();
                        SharedPreferences preferences = getSharedPreferences("TambahUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("SIMPAN", true);
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_LONG).show();
                        finish();
                        DetailUserActivity.detailUserActivity.finish();
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
        builder.setMessage("Hapus "+ user.getNamaUser()+"?");
        builder.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new AsyncFetch2().execute();
                            SharedPreferences preferences = getSharedPreferences("TambahUser", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("SIMPAN", true);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Data berhasil dihapus", Toast.LENGTH_LONG).show();
                            finish();
                            DetailUserActivity.detailUserActivity.finish();
                        }catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Data gagal dihapus", Toast.LENGTH_LONG).show();
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

    public class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/ubah_user.php?nama="+nama+"&nomor="+nomor+"&alamat="+alamat+"&telepon="+telepon+"&email="+email+"&level="+level+"&password="+password;

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

    public class AsyncFetch2 extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/hapus_user.php?nomor="+nomorinduk;

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

    public String convertLevel(String panjang){
        String returned = "";
        switch (panjang) {
            case "Admin" : returned = "0"; break;
            case "Dosen" : returned = "1"; break;
            case "Mahasiswa" : returned = "2"; break;
        }
        return returned;
    }

    public int convertBack(String lvl) {
        int returned = 0;
        switch (lvl) {
            case "0" : returned = 0; break;
            case "1" : returned = 1; break;
            case "2" : returned = 2; break;
        }
        return returned;
    }
}
