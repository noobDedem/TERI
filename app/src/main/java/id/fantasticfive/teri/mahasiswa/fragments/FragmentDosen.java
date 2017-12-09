package id.fantasticfive.teri.mahasiswa.fragments;

/**
 * Created by anism on 30/09/2017.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import id.fantasticfive.teri.mahasiswa.models.Dosen;
import id.fantasticfive.teri.mahasiswa.adapters.DosenAdapter;
import id.fantasticfive.teri.R;


public class FragmentDosen extends Fragment{

    private ArrayList<Dosen> DosenArray = new ArrayList<>();
    DosenAdapter adapter;

    public FragmentDosen() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new AsyncFetch().execute();
        View v=inflater.inflate(R.layout.content_dosen, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view);
        adapter = new DosenAdapter(getContext(), DosenArray);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        return v;
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {

        HttpURLConnection conn;
        URL url = null;
        String userURL = getString(R.string.ip) + "/teri/show_dosen.php";
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            if (DosenArray.isEmpty()) {
                try {
                    url = new URL(userURL);
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (DosenArray.isEmpty()) {
                try {
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String nip = jsonObject.getString("nomorInduk");
                            String namaDosen = jsonObject.getString("nama");
                            String alamat = jsonObject.getString("alamat");
                            String telepon = jsonObject.getString("telepon");
                            String email = jsonObject.getString("email");
                            Dosen Dosen = new Dosen(nip, namaDosen, alamat, telepon, email);
                            DosenArray.add(Dosen);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}