package id.fantasticfive.teri.admin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.models.MataKuliah;
import id.fantasticfive.teri.admin.adapters.MataKuliahAdapter;
import id.fantasticfive.teri.admin.activities.TambahMataKuliahActivity;

/**
 * Created by alfifau on 09/10/2017.
 */

public class FragmentMataKuliah extends Fragment {

    private ArrayList<MataKuliah> mataKuliahArray = new ArrayList<>();
    MataKuliahAdapter adapter;
    FloatingActionButton fab;

    public FragmentMataKuliah() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new AsyncFetch().execute();
        View v=inflater.inflate(R.layout.content_matakuliah, container, false);
        fab = (FloatingActionButton)v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahMataKuliahActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view);
        adapter = new MataKuliahAdapter(getContext(), mataKuliahArray);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        return v;
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String showMatakuliahURL = getString(R.string.ip) + "/teri/show_matkul_admin.php";
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(showMatakuliahURL.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn  = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
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
//            if (mataKuliahArray.isEmpty()) {
//            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (response_code == HttpURLConnection.HTTP_OK) {
                    JSONArray jsonArray = new JSONArray(s);
                    mataKuliahArray.clear();

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String kodeMataKuliah = jsonObject.getString("kodeMataKuliah");
                        String namaMataKuliah = jsonObject.getString("namaMataKuliah");
                        String enrollMataKuliah = jsonObject.getString("enrollmentKey");
                        String dosen1MataKuliah = jsonObject.getString("User_nomorInduk");
                        String dosen2MataKuliah = jsonObject.getString("User_nomorInduk2");
                        MataKuliah mataKuliah = new MataKuliah(kodeMataKuliah, namaMataKuliah, enrollMataKuliah, dosen1MataKuliah, dosen2MataKuliah);
                        mataKuliahArray.add(mataKuliah);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (mataKuliahArray.isEmpty()) {
//            }
        }
    }

    public String hari(int x) {

        switch (x) {
            case 1 :
                return "Senin";
            case 2 :
                return "Selasa";
            case 3 :
                return "Rabu";
            case 4 :
                return "Kamis";
            case 5 :
                return "Jumat";
        }
        return null;
    }

    public boolean cekMatkul() {
        SharedPreferences preferences = getActivity().getSharedPreferences("TambahMatkul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean flag = preferences.getBoolean("SIMPAN", false);
        editor.putBoolean("SIMPAN", false);
        editor.commit();
        return flag;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cekMatkul()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}