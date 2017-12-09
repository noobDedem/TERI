package id.fantasticfive.teri.admin.fragments;

/**
 * Created by alfifau on 09/10/2017.
 */

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
import id.fantasticfive.teri.admin.activities.TambahUserActivity;
import id.fantasticfive.teri.admin.models.User;
import id.fantasticfive.teri.admin.adapters.UserAdapter;


public class FragmentPengguna extends Fragment {

    private ArrayList<User> userArray = new ArrayList<>();
    UserAdapter adapter;
    FloatingActionButton fab;

    public FragmentPengguna() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new AsyncFetch().execute();
        View v = inflater.inflate(R.layout.content_pengguna, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahUserActivity.class);
                startActivity(intent);

            }
        });
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view);
        adapter = new UserAdapter(getContext(), userArray);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        return v;
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String userURL = getString(R.string.ip) + "/teri/show_user.php";
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                url = new URL(userURL.replaceAll(" ", "%20"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
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
//            if (userArray.isEmpty()) {
//            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (response_code == HttpURLConnection.HTTP_OK) {
                    userArray.clear();
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String namaUser = jsonObject.getString("nama");
                        String nipUser = jsonObject.getString("nomorInduk");
                        String alamatUser = jsonObject.getString("alamat");
                        String telUser = jsonObject.getString("telepon");
                        String emailUser = jsonObject.getString("email");
                        String levelUser = jsonObject.getString("level");
                        String passwordUser = jsonObject.getString("password");
                        User user = new User(namaUser, nipUser, alamatUser, telUser, emailUser, levelUser, passwordUser);
                        userArray.add(user);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (userArray.isEmpty()) {
//            }
        }
    }

    public boolean cekUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("TambahUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean flag = preferences.getBoolean("SIMPAN", false);
        editor.putBoolean("SIMPAN", false);
        editor.commit();
        return flag;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cekUser()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

}