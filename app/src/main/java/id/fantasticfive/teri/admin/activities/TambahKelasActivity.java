package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 12/11/2017.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.models.MataKuliah;

public class TambahKelasActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener times, times2;
    EditText mkelas, mjamkuliah, mruangkuliah, mtanggalujian, mjamujian, mruangujian;
    Button msimpan, mbatal;
    String namakelas, jamkuliah, ruangkuliah, tanggalujian, jamujian, ruangujian, kodematkul;
    int harikuliah;
    Spinner mharikuliah;
    Calendar myCalendar;
    String [] items = new String[]{"Senin","Selasa","Rabu","Kamis","Jumat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kelas);

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

        mkelas = (EditText) findViewById(R.id.kelas_jadwal);
        mharikuliah = (Spinner) findViewById(R.id.hari_kuliah);
        mjamkuliah = (EditText) findViewById(R.id.jam_kuliah);
        mruangkuliah = (EditText) findViewById(R.id.ruang_kuliah);
        mtanggalujian = (EditText)findViewById(R.id.tanggal_ujian);
        mjamujian = (EditText) findViewById(R.id.jam_ujian);
        mruangujian = (EditText) findViewById(R.id.ruang_ujian);
        msimpan = (Button)findViewById(R.id.simpan_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mharikuliah.setAdapter(adapter);
        mharikuliah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                harikuliah = hari(items[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Matkul", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedMatkul","");
        MataKuliah mataKuliah = gson.fromJson(json, MataKuliah.class);
        kodematkul = mataKuliah.getKodeMataKuliah();

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

        times2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE,i1);
                updateLabelTime2();
            }
        };

        mtanggalujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TambahKelasActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mjamkuliah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(TambahKelasActivity.this, times, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();

            }
        });

        mjamujian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(TambahKelasActivity.this, times2, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();

            }
        });


        msimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namakelas = mkelas.getText().toString();
                jamkuliah = mjamkuliah.getText().toString();
                ruangkuliah = mruangkuliah.getText().toString();
                tanggalujian = mtanggalujian.getText().toString();
                jamujian = mjamujian.getText().toString();
                ruangujian = mruangujian.getText().toString();
                if (namakelas.equals("") || jamkuliah.equals("") || ruangkuliah.equals("") || tanggalujian.equals("") ||
                        jamujian.equals("") || ruangujian.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        new TambahKelasActivity.AsyncFetch(getApplicationContext()).execute();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    SharedPreferences preferences = getSharedPreferences("TambahKelas", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("SIMPAN", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void updateLabelDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mtanggalujian.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mjamkuliah.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime2() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mjamujian.setText(sdf.format(myCalendar.getTime()));
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String addTugasUrl = getString(R.string.ip) + "/teri/add_kelas.php?kelas="+namakelas+"&harikuliah="+harikuliah+"&jamkuliah="+jamkuliah+"&ruangkuliah="+ruangkuliah+"&tanggalujian="+tanggalujian+"&jamujian="+jamujian+"&ruangujian="+ruangujian+"&kode="+kodematkul;

        public AsyncFetch(Context context) {
            this.context = context.getApplicationContext();
        }

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

    public int hari  (String x) {

        switch (x) {
            case "Senin" :
                return 1;
            case "Selasa" :
                return 2;
            case "Rabu" :
                return 3;
            case "Kamis" :
                return 4;
            case "Jumat" :
                return 5;
        }
        return Integer.parseInt(null);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
