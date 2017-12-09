package id.fantasticfive.teri.dosen.fragment;

/**
 * Created by D-Y-U on 09/10/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.dosen.models.MataKuliah;
import id.fantasticfive.teri.dosen.adapters.MataKuliahAdapter;

import static android.content.Context.MODE_PRIVATE;


public class FragmentTugas extends Fragment{
    private Context mContext;
    private ArrayList<MataKuliah> matkulArray = new ArrayList<>();
    MataKuliahAdapter adapter;
    FloatingActionButton fab;

    public FragmentTugas() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new FragmentTugas.AsyncFetch().execute();
        View v = inflater.inflate(R.layout.content_tugas_dsn, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view_tgs);
        adapter = new MataKuliahAdapter(getContext(), matkulArray);
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        return v;
    }

    private class AsyncFetch extends AsyncTask<Integer, Void, String> {
        HttpURLConnection conn;
        URL url = null;
        SharedPreferences myPrefs = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String nim = myPrefs.getString("NIM", null);
        String userURL = getString(R.string.ip) + "/teri/show_matkul.php?noinduk="+nim;
        int response_code;

        @Override
        protected String doInBackground(Integer... integers) {
            if (matkulArray.isEmpty()) {
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (matkulArray.isEmpty()) {
                try {
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String kdmatkul = jsonObject.getString("kodeMataKuliah");
                            String namaMatkul = jsonObject.getString("namaMataKuliah");
                            MataKuliah matkul = new MataKuliah(kdmatkul, namaMatkul);
                            matkulArray.add(matkul);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Gagal menghubungkan ke server", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Devider extends RecyclerView.ItemDecoration{
        private Drawable mDivider;

        public Devider(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }

            outRect.top = mDivider.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(c);
            }
        }
    }
}