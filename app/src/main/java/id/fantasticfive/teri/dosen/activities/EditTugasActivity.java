package id.fantasticfive.teri.dosen.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.dosen.models.Tugas;

public class EditTugasActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener times;
    Calendar myCalendar;
    EditText mkodetugas, mnama, mformat, mtanggal, mwaktu, mketerangan;
    Button msimpan,mhapus;
    String kodeTugas, nama, format, ket,waktu,tanggal, deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tugas_dsn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_km);
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

        myCalendar = Calendar.getInstance();

        SimpleDateFormat dateinDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat wkt = new SimpleDateFormat("HH:mm");
        Date dl = null;
        try {
            dl = dateinDB.parse(tugas.getDeadline());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String tgld = tgl.format(dl);
        String time = wkt.format(dl);

        kodeTugas =tugas.getKodeTugas();

        mkodetugas = (EditText)findViewById(R.id.kode_tugas);
        mkodetugas.setText(kodeTugas);
        mkodetugas.setEnabled(false);
        mnama = (EditText)findViewById(R.id.nama_tugas);
        mnama.setText(tugas.getNama());
        mformat = (EditText) findViewById(R.id.format_tugas);
        mformat.setText(tugas.getFormat());
        mtanggal = (EditText)findViewById(R.id.tanggal);
        mtanggal.setText(tgld);
        mwaktu = (EditText)findViewById(R.id.waktu);
        mwaktu.setText(time);
        mketerangan = (EditText) findViewById(R.id.ket_tugas);
        mketerangan.setText(tugas.getKeterangan());
        msimpan = (Button)findViewById(R.id.simpan_button);
        mhapus = (Button)findViewById(R.id.hapus_button);



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
                new DatePickerDialog(EditTugasActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mwaktu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditTugasActivity.this, times, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();

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
                if (nama.equals("") || format.equals("") || tanggal.equals("") || waktu.equals("") ||
                        deadline.equals("") || ket.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap memenuhi semua kolom", Toast.LENGTH_LONG).show();
                } else {
                    Snackbar snackbar = Snackbar.make(view, kodeTugas+nama + format + deadline + ket, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    try {
                        new AsyncFetch().execute();
                        SharedPreferences sharedPreferences = getSharedPreferences("Tugas", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("Flag", true);
                        editor.commit();
                        DetailTugasActivity.detailTugasActivity.finish();
                        finish();
                    }catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Gagal mengubah", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Hapus");
        builder.setMessage("Hapus "+ tugas.getNama()+"?");
        builder.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new Delete().execute();
                            SharedPreferences sharedPreferences = getSharedPreferences("Tugas", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("Flag", true);
                            editor.commit();
                            DetailTugasActivity.detailTugasActivity.finish();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal menghapus", Toast.LENGTH_LONG).show();
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

    private void updateLabelDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mtanggal.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mwaktu.setText(sdf.format(myCalendar.getTime()));
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String addTugas = getString(R.string.ip) + "/teri/update_tugas.php?kdtugas="+kodeTugas+"&nama="+nama+"&format="+format+"&deadline="+deadline+"&ket="+ket;
        String addTugasUrl = addTugas.replaceAll(" ", "%20");
        int response_code;

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

    }

    private class Delete extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String addTugas = getString(R.string.ip) + "/teri/delete_tugas.php?kdtugas="+kodeTugas;
        String addTugasUrl = addTugas.replaceAll(" ", "%20");
        int response_code;


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
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
