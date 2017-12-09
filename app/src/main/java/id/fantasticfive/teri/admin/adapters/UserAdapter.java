package id.fantasticfive.teri.admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.fantasticfive.teri.R;
import id.fantasticfive.teri.admin.activities.DetailUserActivity;
import id.fantasticfive.teri.admin.models.User;

/**
 * Created by Rindh27 on 09/10/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<User> userArrayList;
    List<User> userList;
    public static UserAdapter ua;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaUser;
        public Object result;

        public MyViewHolder(View view) {
            super(view);
            namaUser = (TextView) view.findViewById(R.id.nama_user);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


    public void swap(ArrayList<User> userArrayList){
        if (userArrayList == null || userArrayList.size()==0)
            return;
        if (userList != null && userList.size()>0)
            userList.clear();
            userList.addAll(userArrayList);
            notifyDataSetChanged();
    }

    public UserAdapter(Context mContext, ArrayList<User> userArrayList) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
    }


    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);

        return new UserAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final UserAdapter.MyViewHolder holder, final int position) {
        final User user = userArrayList.get(position);
        holder.namaUser.setText(user.getNamaUser());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = userArrayList.get(position);
                saveUserInSP(user);
                Intent pindah = new Intent(mContext, DetailUserActivity.class);
                mContext.startActivity(pindah);
            }
        });
    }

    public void saveUserInSP (User user){
        SharedPreferences sp = mContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("SelectedUser",json);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        try {
            return userArrayList.size();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return 0;
    }

}
