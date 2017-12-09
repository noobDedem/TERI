package id.fantasticfive.teri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import id.fantasticfive.teri.admin.activities.MainActivityAdmin;
import id.fantasticfive.teri.dosen.activities.MainActivityDosen;
import id.fantasticfive.teri.mahasiswa.activities.MainActivity;

/**
 * Created by Demas on 10/4/2017.
 */

public class LoginActivity extends AppCompatActivity {

    EditText nomorInduk, password;
    Button button;
    String nomorIndukLogin, passwordLogin;

    public LoginActivity() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nomorInduk = (EditText)findViewById(R.id.login_nomor);
        password = (EditText)findViewById(R.id.login_password);
        button = (Button)findViewById(R.id.login_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomorIndukLogin = nomorInduk.getText().toString();
                passwordLogin = password.getText().toString();
                try {
                    new AsyncFetch(getApplicationContext()).execute();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        private static final String EXTRA_NOMORINDUK = "id.fantasticfive.teri.NOMORINDUK";
        Context context;
        HttpURLConnection conn;
        URL url = null;
        String loginUrl = getString(R.string.ip) + "/teri/check_login.php?nomorinduk="+nomorIndukLogin+"&password="+passwordLogin;
        int response_code;

        public AsyncFetch(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(loginUrl);
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

        @Override
        protected void onPostExecute(String s) {
            String status = "", nim = "", nama = "", alamat = "", telepon = "", email = "", pass = "";
            String statusFlag;
            Integer level = null;
            if (response_code == HttpURLConnection.HTTP_OK) {
                try {

                    JSONObject mainObject = new JSONObject(s);
                    JSONObject data = mainObject.getJSONObject("data");
                    status = mainObject.getString("status");
                    nim = data.getString("nomorInduk");
                    nama = data.getString("nama");
                    alamat = data.getString("alamat");
                    telepon = data.getString("telepon");
                    email = data.getString("email");
                    level = data.getInt("level");
                    pass = data.getString("password");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                switch (status) {
                    case "true" :
                        statusFlag = "true";
                        break;
                    default:
                        statusFlag = "false";
                        break;
                }
                if (statusFlag == "true") {
                    SharedPreferences myPrefs = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.putString("NAMA", nama);
                    editor.putString("NIM",nim);
                    editor.putString("PASSWORD",pass);
                    editor.putInt("LEVEL",level);
                    editor.commit();
                    switch (level) {
                        case 0 :
                            Intent intentadmin = new Intent(context, MainActivityAdmin.class);
                            startActivity(intentadmin);
                            finish();
                            break;
                        case 1 :
                            Intent intentdosen = new Intent(context, MainActivityDosen.class);
                            startActivity(intentdosen);
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("JadwalAjarFlag", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorDosen = sharedPreferences.edit();
                            editorDosen.putBoolean("flagJadwal", true);
                            editorDosen.commit();
                            finish();
                            break;
                        case 2 :
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("JadwalFlag", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putBoolean("flagUjian", false);
                            editor1.commit();
                            finish();
                            break;

                        default:
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                            break;
                    }
                } else {
                    Toast.makeText(context, "Nomor induk atau password salah", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
            }
        }
    }
}
