package id.fantasticfive.teri.admin.activities;

/**
 * Created by alfifau on 11/11/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.adapters.UserAdapter;
import id.fantasticfive.teri.admin.models.User;

public class DetailUserActivity extends AppCompatActivity {
    Button msimpan, mubah;
    UserAdapter mAdapter;
    TextView mnama, mnomorinduk, malamat, mtelepon, memail, mlevel, mpassword;
    String nomorinduk;
    ArrayList<User> userArrayList;
    public static Activity detailUserActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        detailUserActivity = this;
        mAdapter = new UserAdapter(this, new ArrayList<User>());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("SelectedUser","");
        User user = gson.fromJson(json, User.class);

        mnama = (TextView) findViewById(R.id.nama_user);
        mnama.setText(user.getNamaUser());
        mnomorinduk = (TextView) findViewById(R.id.nomor_user);
        mnomorinduk.setText(user.getNipUser());
        malamat = (TextView) findViewById(R.id.alamat_user);
        malamat.setText(user.getAlamatUser());
        mtelepon = (TextView) findViewById(R.id.tel_user);
        mtelepon.setText(user.getTeleponUser());
        memail = (TextView) findViewById(R.id.email_user);
        memail.setText(user.getEmailUser());
        mlevel = (TextView) findViewById(R.id.level_user);
        mlevel.setText(user.getLevelUser());
        mpassword = (TextView) findViewById(R.id.password_user);
        mpassword.setText(user.getPassUser());
        mubah = (Button)findViewById(R.id.ubah_button);
        mAdapter.swap(userArrayList);

        nomorinduk = mnomorinduk.getText().toString();

        mubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailUserActivity.this, UbahUserActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
