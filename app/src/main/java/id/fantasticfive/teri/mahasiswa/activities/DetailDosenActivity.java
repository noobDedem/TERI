package id.fantasticfive.teri.mahasiswa.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.models.Dosen;

/**
 * Created by Alfifau on 10/29/2017.
 */

public class DetailDosenActivity extends AppCompatActivity {
    String nip, nama, alamat, telepon, email;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Dosen", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedDosen","");
        Dosen dosen = gson.fromJson(json, Dosen.class);

        txt = (TextView) findViewById(R.id.nama_dosen);
        txt.setText(dosen.getNamaDosen());
        txt = (TextView) findViewById(R.id.nip_dosen);
        txt.setText(dosen.getNipDosen());
        txt = (TextView) findViewById(R.id.alamat_dosen);
        txt.setText(dosen.getAlamatDosen());
        txt = (TextView) findViewById(R.id.tel_dosen);
        txt.setText(dosen.getTelDosen());
        txt = (TextView) findViewById(R.id.email_dosen);
        txt.setText(dosen.getEmailDosen());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}