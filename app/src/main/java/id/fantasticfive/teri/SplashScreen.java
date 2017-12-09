package id.fantasticfive.teri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import id.fantasticfive.teri.admin.activities.MainActivityAdmin;
import id.fantasticfive.teri.dosen.activities.MainActivityDosen;
import id.fantasticfive.teri.mahasiswa.activities.MainActivity;

/**
 * Created by Demas on 10/28/2017.
 */

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SharedPreferences myPrefs = getSharedPreferences("Login", MODE_PRIVATE);
                String nimSP = myPrefs.getString("NIM",null);
                String passwordSP = myPrefs.getString("PASSWORD",null);
                int levelSP = myPrefs.getInt("LEVEL",99);

                if (nimSP == null || passwordSP == null) {
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    switch (levelSP) {
                        case 0 :
                            Intent intentadmin = new Intent(getApplicationContext(), MainActivityAdmin.class);
                            startActivity(intentadmin);
                            finish();
                            break;
                        case 1 :
                            Intent intentdosen = new Intent(getApplicationContext(), MainActivityDosen.class);
                            startActivity(intentdosen);
                            finish();
                            break;
                        case 2 :
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }

                }
            }
        }).start();


    }
}
