package id.fantasticfive.teri.admin.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.models.Kelas;

public class UbahKelasActivity extends AppCompatActivity {
    EditText mkelas, mjamKuliah, mruangKuliah, mtanggalUjian, mjamUjian, mruangUjian;
    String namakelas, jamKuliah, ruangKuliah, tanggalUjian, jamUjian, ruangUjian, idKuliah, idUjian, kodeMatkul;
    Button msimpan, mhapus;
    Spinner mhariKuliah;
    Integer hariKuliah;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener times, times2;
    public ArrayList<Kelas> kelasArrayList;
    Calendar myCalendar;
    Kelas kelas;
    String [] items = new String[]{"Senin","Selasa","Rabu","Kamis","Jumat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kelas);

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

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Kelas", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedKelas","");
        kelas= gson.fromJson(json, Kelas.class);
        idKuliah = kelas.getIdKuliah();
        idUjian = kelas.getIdUjian();

        mkelas = (EditText) findViewById(R.id.kelas_jadwal);
        mkelas.setText(kelas.getNamaKelas());
        mhariKuliah= (Spinner) findViewById(R.id.hari_kuliah);
        mjamKuliah = (EditText) findViewById(R.id.jam_kuliah);
        mjamKuliah.setText(kelas.getJamKuliah());
        mruangKuliah = (EditText) findViewById(R.id.ruang_kuliah);
        mruangKuliah.setText(kelas.getRuangKuliah());
        mtanggalUjian = (EditText) findViewById(R.id.tanggal_ujian);
        mtanggalUjian.setText(kelas.getTanggalUjian());
        mjamUjian = (EditText) findViewById(R.id.jam_ujian);
        mjamUjian.setText(kelas.getJamUjian());
        mruangUjian = (EditText) findViewById(R.id.ruang_ujian);
        mruangUjian.setText(kelas.getRuangUjian());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mhariKuliah.setAdapter(adapter);
        //TODO masih belum bener drop down yang ini
        mhariKuliah.setSelection(Integer.parseInt(kelas.getHariKuliah()) - 1);
        mhariKuliah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hariKuliah = hari(items[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        mtanggalUjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UbahKelasActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mjamKuliah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(UbahKelasActivity.this, times, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();

            }
        });

        mjamUjian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(UbahKelasActivity.this, times2, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();

            }
        });

        msimpan = (Button)findViewById(R.id.simpan_button);
        msimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namakelas= mkelas.getText().toString();
                jamKuliah = mjamKuliah.getText().toString();
                ruangKuliah = mruangKuliah.getText().toString();
                tanggalUjian = mtanggalUjian.getText().toString();
                jamUjian = mjamUjian.getText().toString();
                ruangUjian = mruangUjian.getText().toString();
                if (namakelas.equals("") || jamKuliah.equals("") || ruangKuliah.equals("") || tanggalUjian.equals("") ||
                        jamUjian.equals("") || ruangUjian.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        new AsyncFetch().execute();
                        SharedPreferences preferences = getSharedPreferences("TambahKelas", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("SIMPAN", true);
                        editor.commit();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Hapus");
        builder.setMessage("Hapus kelas "+ kelas.getNamaKelas()+"?");
        builder.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new DeleteJadwalKuliah().execute();
                            new DeleteJadwalUjian().execute();
                            SharedPreferences preferences = getSharedPreferences("TambahKelas", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("SIMPAN", true);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Data berhasil dihapus", Toast.LENGTH_LONG).show();
                            finish();
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

        mhapus = (Button)findViewById(R.id.hapus_button);
        mhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void updateLabelDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mtanggalUjian.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mjamKuliah.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime2() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mjamUjian.setText(sdf.format(myCalendar.getTime()));
    }

    public class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/ubah_kelas.php?idKuliah="+idKuliah+"&kelas="+namakelas+"&hariKuliah="+hariKuliah+"&jamKuliah="+jamKuliah+"&ruangKuliah="+ruangKuliah+"&idUjian="+idUjian+"&tanggalUjian="+tanggalUjian+"&jamUjian="+jamUjian+"&ruangUjian="+ruangUjian;

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

    public class DeleteJadwalKuliah extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/delete_kelas_kuliah.php?kodeMatkul="+kelas.getKodeMatkul()+"&kelas="+kelas.getNamaKelas();

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

    public class DeleteJadwalUjian extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String editTugasUrl = getString(R.string.ip) + "/teri/delete_kelas_ujian.php?kodeMatkul="+kelas.getKodeMatkul()+"&kelas="+kelas.getNamaKelas();

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
        return 0;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
