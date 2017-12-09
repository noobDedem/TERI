package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 09/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.models.MataKuliah;

public class DetailMataKuliahActivity extends AppCompatActivity {
    TextView mkode, mnama, menroll, mdosen1, mdosen2;
    Button mubah;
    LinearLayout layoutKelas, layoutUjian, layoutJadwal;
    public static Activity detailMataKuliahActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mata_kuliah);

        detailMataKuliahActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        MataKuliah mataKuliah = gson.fromJson(json, MataKuliah.class);
        final String kode = mataKuliah.getKodeMataKuliah();

        mkode = (TextView) findViewById(R.id.kode_matkul);
        mkode.setText(mataKuliah.getKodeMataKuliah());
        mnama = (TextView) findViewById(R.id.nama_matkul);
        mnama.setText(mataKuliah.getNamaMataKuliah());
        menroll = (TextView) findViewById(R.id.enroll_matkul);
        menroll.setText(mataKuliah.getEnrollment());
        mdosen1 = (TextView) findViewById(R.id.dosen_matkul);
        mdosen1.setText(mataKuliah.getDosenPengampu1());
        mdosen2 = (TextView) findViewById(R.id.dosen2_matkul);
        mdosen2.setText(mataKuliah.getDosenPengampu2());
        mubah = (Button)findViewById(R.id.ubah_button);

        mubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMataKuliahActivity.this, UbahMataKuliahActivity.class);
                startActivity(intent);
            }
        });

        layoutKelas = (LinearLayout)findViewById(R.id.kelas_matkul);
        layoutKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMataKuliahActivity.this, DaftarKelasActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
