package id.fantasticfive.teri.mahasiswa.activities;

/**
 * Created by Rindh27 on 31/10/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.Normalizer;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.mahasiswa.models.UploadedTugas;


public class FormUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = DetailTugasActivity.class.getSimpleName();
    private String selectedFilePath;
    private String SERVER_URL;
    String location, fileName;
    String kodeMatkulIn, kodeTugasIn, namaIn, nimIn;
    EditText inputName;
    EditText inputKodeTugas;
    EditText inputNim;
    Button bUpload, uploadedFile;
    TextView tvFileName;
    Button inputFile;
    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    private View layout;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_upload);

        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);

        Toolbar tool = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();

        inputKodeTugas = (EditText) findViewById(R.id.input_kodetugas);
        inputName = (EditText) findViewById(R.id.input_nama);
        inputNim = (EditText) findViewById(R.id.input_nim);
        inputFile = (Button) findViewById(R.id.input_file);
        bUpload = (Button) findViewById(R.id.b_upload);
        uploadedFile = (Button) findViewById(R.id.uploaded_file);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);

        layout = (CoordinatorLayout) findViewById(R.id.layout);

        inputKodeTugas.setText(bundle.getString("kodeTugas"));
        inputName.setText(sharedPreferences.getString("NAMA", "gagal"));
        inputNim.setText(sharedPreferences.getString("NIM", "gagal"));
        inputFile.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        uploadedFile.setOnClickListener(this);

        kodeMatkulIn = bundle.getString("kodeMatkul");
        kodeTugasIn = inputKodeTugas.getText().toString();
        namaIn =inputName.getText().toString();
        nimIn = inputNim.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if(view== inputFile){

            int permissionCheck  = ContextCompat.checkSelfPermission(FormUploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //on attachment icon click
                showFileChooser();
            } else {
                requestPermission();
            }

        }
        if(view == bUpload){
            //on upload button Click
            if(selectedFilePath != null){
                dialog = ProgressDialog.show(FormUploadActivity.this,"","Mengunggah File...",true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //creating new thread to handle Http Operations

                        uploadFile(selectedFilePath);
                    }
                }).start();
            }else{
                Toast.makeText(FormUploadActivity.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
            }
        }
        if(view == uploadedFile) {
            Intent intent = new Intent(this, UploadedTugasActivity.class);
            intent.putExtra("kodeMataKuliah", kodeMatkulIn);
            intent.putExtra("kodeTugas", kodeTugasIn);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvFileName.setText(selectedFilePath);
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private int uploadFile(final String selectedFilePath) {
        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize =  1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        String extension = parts[parts.length-1];
        String[] extension2 = extension.split("\\.");
        location = kodeMatkulIn + "/" + inputKodeTugas.getText().toString() + "/";
        fileName = inputKodeTugas.getText().toString() + "_" + inputName.getText().toString().replaceAll(" ", "_") + "_" + inputNim.getText().toString() + "." + extension2[extension2.length-1];
        SERVER_URL = getString(R.string.ip) + "/teri/upload.php?path=" + location;
        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    new AsyncFetch().execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://localhost/teri/uploads/"+ fileName);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menyambungkan ke server", Toast.LENGTH_LONG);
                }

                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FormUploadActivity.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(FormUploadActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(FormUploadActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String path = (getString(R.string.ip) + "/" + "uploads/" + location + fileName).replaceAll(" ", "%20").replaceAll("/", "%2F").replaceAll(":", "%3A");
        String addBankTugas = getString(R.string.ip) + "/teri/bankTugas.php?kodeMataKuliah="+kodeMatkulIn+"&kodeTugas="+kodeTugasIn+"&nama="+namaIn.replaceAll(" ", "_")+"&nim="+nimIn+"&namaFile="+fileName+"&filePath="+path;

        int coba = 0;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(addBankTugas);
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFileChooser();
            } else {
                Snackbar.make(layout, "Ijin ditolak oleh pengguna", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Snackbar.make(layout, "Ijin ini dibutuhkan untuk mengupload file", Snackbar.LENGTH_INDEFINITE) .setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(FormUploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
                }
            }).show();
        } else {
            Snackbar.make(layout, "Meminta ijin", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }
}
