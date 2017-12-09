package id.fantasticfive.teri.dosen.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.fantasticfive.teri.R;

public class TambahTugasActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener times;
    private SimpleDateFormat dateFormatter;
    Calendar myCalendar;
    EditText mkodetugas, mnama, mformat, mtanggal, mwaktu, mketerangan;
    Button msimpan;
    String kodeTugas, nama, format, ket,kodeMatkul,waktu,tanggal, deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tugas);
        final Bundle extras = getIntent().getExtras();
        kodeMatkul = extras.getString("kdmatkul");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_km);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        myCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy", Locale.US);
        String currentTime = sdf.format(myCalendar.getTime());
        kodeTugas ="Tugas_" + kodeMatkul +"_"+ currentTime;

        mkodetugas = (EditText)findViewById(R.id.kode_tugas);
        mkodetugas.setText(kodeTugas);
        mkodetugas.setEnabled(false);
        mnama = (EditText)findViewById(R.id.nama_tugas);
        mformat = (EditText) findViewById(R.id.format_tugas);
        mtanggal = (EditText)findViewById(R.id.tanggal);
        mwaktu = (EditText)findViewById(R.id.waktu);
        mketerangan = (EditText) findViewById(R.id.ket_tugas);
        msimpan = (Button)findViewById(R.id.simpan_button);



        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();
            }
        };

        times = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE,i1);
                updateLabelTime();
            }
        };

        mtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TambahTugasActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mwaktu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(TambahTugasActivity.this, times, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();

            }
        });
        msimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = mnama.getText().toString();
                format = mformat.getText().toString();
                tanggal = mtanggal.getText().toString();
                waktu = mwaktu.getText().toString();
                deadline = tanggal + " " + waktu+":00";
                ket = mketerangan.getText().toString();
                if (nama.equals("") || format.equals("") || tanggal.equals("") || waktu.equals("") || deadline.equals("") || ket.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    Snackbar snackbar = Snackbar.make(view, kodeTugas+kodeMatkul+nama + format + deadline + ket, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    try {
                        new AsyncFetch().execute();
                        SharedPreferences sharedPreferences = getSharedPreferences("Tugas", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("Flag", true);
                        editor.commit();
                        finish();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateLabelDate() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mtanggal.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mwaktu.setText(sdf.format(myCalendar.getTime()));
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String>  {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String addTugas = getString(R.string.ip) + "/teri/add_tugas.php?kdtugas="+kodeTugas+"&nama="+nama+"&format="+format+"&deadline="+deadline+"&ket="+ket+"&kodeMatkul="+kodeMatkul;
        String addTugasUrl = addTugas.replaceAll(" ", "%20");

        int coba = 0;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(addTugasUrl);
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
