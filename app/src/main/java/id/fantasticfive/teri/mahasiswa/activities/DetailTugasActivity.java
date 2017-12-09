package id.fantasticfive.teri.mahasiswa.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.fragments.NoticeDialogFragment;
import id.fantasticfive.teri.mahasiswa.models.Tugas;

/**
 * Created by Alfifau on 11/3/2017.
 */

public class DetailTugasActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener {
    String nama, format, deadline, keterangan, namaMataKuliah, kodeMataKuliah, kodeTugas, enrollmentKey;
    TextView txt;
    EditText editText;
    FloatingActionButton floatingActionButton;
    LinearLayout layoutKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtugas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Tugas", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedTugas", "");
        final Tugas tugas = gson.fromJson(json, Tugas.class);

        layoutKomentar = (LinearLayout)findViewById(R.id.layout_komentar);

        txt = (TextView) findViewById(R.id.nama_tugas);
        txt.setText(tugas.getNama());
        txt = (TextView) findViewById(R.id.format_tugas);
        txt.setText(tugas.getFormat());
        txt = (TextView) findViewById(R.id.dealine_tugas);
        txt.setText(tugas.getDeadline());
        txt = (TextView) findViewById(R.id.ket_tugas);
        txt.setText(tugas.getKeterangan());
        txt = (TextView) findViewById(R.id.nama_matkul);
        txt.setText(tugas.getNamaMatkul());

        Bundle extras = getIntent().getExtras();
        kodeTugas = extras.getString("KODE_TUGAS");
        enrollmentKey = extras.getString("ENROLLMENTKEY");

        layoutKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), KomentarActivity.class);
                intent.putExtra("KODE_TUGAS", kodeTugas);
                startActivity(intent);
            }
        });

        floatingActionButton = (FloatingActionButton)findViewById(R.id.upload_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(DetailTugasActivity.this);
                View dialogView = layoutInflater.inflate(R.layout.dialog_enrollment, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailTugasActivity.this);

                alertBuilder.setView(dialogView);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text_enrollment);
                alertBuilder.setTitle("Enrollment Key");

                alertBuilder.setPositiveButton("CEK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (enrollmentKey.equals(editText.getText().toString())) {
                            Intent intent = new Intent(DetailTugasActivity.this, FormUploadActivity.class);
                            intent.putExtra("kodeMatkul", tugas.getKodeMataKuliah());
                            intent.putExtra("kodeTugas", tugas.getKodeTugas());
                            startActivity(intent);
                        } else {
                            Toast.makeText(DetailTugasActivity.this, "Enrollment Key salah", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertBuilder.create();

                alertDialog.show();
            }
        });
    }

    public void showNoticeDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.DialogFragment noticeDialogFragment = new NoticeDialogFragment();
        noticeDialogFragment.show(fragmentManager, "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        if (enrollmentKey.equals(editText.getText().toString())) {
            Intent intent = new Intent(DetailTugasActivity.this, FormUploadActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
