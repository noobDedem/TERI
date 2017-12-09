package id.fantasticfive.teri.dosen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.dosen.models.Tugas;

/**
 * Created by Alfifau on 11/3/2017.
 */

public class DetailTugasActivity extends AppCompatActivity {
    String nama, format, deadline, keterangan, namaMataKuliah, kodeMataKuliah;
    TextView txt;
    Button mButton;
    LinearLayout layoutKomentar, layoutBankTugas;
    public static Activity detailTugasActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtugas_dsn);
        detailTugasActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("TugasDosen", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("detailTugas", "");
        final Tugas tugas = gson.fromJson(json, Tugas.class);

        txt = (TextView) findViewById(R.id.nama_matkul);
        txt.setText(tugas.getNamaMatkul());
        txt = (TextView) findViewById(R.id.nama_tugas);
        txt.setText(tugas.getNama());
        txt = (TextView) findViewById(R.id.format_tugas);
        txt.setText(tugas.getFormat());
        txt = (TextView) findViewById(R.id.dealine_tugas);
        SimpleDateFormat dateinDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'Jam' HH:mm 'WIB'", Locale.US);
        Date deadline = null;
        try {
            deadline = dateinDB.parse(tugas.getDeadline());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dl = sdf.format(deadline);
        txt.setText(dl);
        txt = (TextView) findViewById(R.id.ket_tugas);
        txt.setText(tugas.getKeterangan());

        layoutKomentar = (LinearLayout)findViewById(R.id.layout_komentar);
        layoutKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KomentarActivity.class);
                intent.putExtra("KODE_TUGAS", tugas.getKodeTugas());
                startActivity(intent);
            }
        });

        layoutBankTugas = (LinearLayout)findViewById(R.id.layout_bank_tugas);
        layoutBankTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          Intent intent = new Intent(getApplicationContext(), BankTugasActivity.class);
                intent.putExtra("kodeMataKuliah", tugas.getKodeMataKuliah());
                intent.putExtra("kodeTugas", tugas.getKodeTugas());
                startActivity(intent);
            }
        });


        mButton = (Button) findViewById(R.id.edit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateinDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat wkt = new SimpleDateFormat("HH:mm");
                Date deadline = null;
                try {
                    deadline = dateinDB.parse(tugas.getDeadline());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String tanggal = tgl.format(deadline);
                String waktu = wkt.format(deadline);

                Intent intent = new Intent(getApplicationContext(), EditTugasActivity.class);
                intent.putExtra("kdmatkul", tugas.getKodeMataKuliah());
                intent.putExtra("namatugas", tugas.getNama());
                intent.putExtra("format", tugas.getFormat());
                intent.putExtra("kdtugas", tugas.getKodeTugas());
                intent.putExtra("tanggal", tanggal);
                intent.putExtra("waktu", waktu);
                intent.putExtra("ket", tugas.getKeterangan());
                startActivity(intent);
            }
        });

//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("Delete", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean flag = sharedPreferences.getBoolean("DeleteFlag", false);
        if (flag) {
            editor.putBoolean("DeleteFlag", true);
            editor.commit();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
