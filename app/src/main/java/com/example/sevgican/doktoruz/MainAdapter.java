package com.example.sevgican.doktoruz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.sevgican.doktoruz.LoginActivity.EXTRA_MESSAGE;

/**
 * Created by sevgican on 05.12.2017.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    JSONArray mUserData;
    String item="";

    public MainAdapter(JSONArray jar) {
        mUserData = jar;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, final int position) {

        try {
            Log.e("Main Adapter","itemleri cekiyorum");
            item = mUserData.getJSONObject(position).getString("hospitalname");
            if(item.equals("###")){
                holder.mFirstName.setText("You're not registered to any hospital");
                return;
            }
            item += "\nCity: ";
            item += mUserData.getJSONObject(position).getString("cityname");
            item += "\nCapacity: ";
            item += mUserData.getJSONObject(position).getString("capacity");
            holder.mFirstName.setText(item);
            holder.mFirstName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Main2Activity.class);
                    String message = null;
                    try {
                        message = mUserData.getJSONObject(position).getString("hospitalid");
                    } catch (JSONException e) {
                        Log.e("Main Adapter", String.valueOf(e));
                        e.printStackTrace();
                    }
                    intent.putExtra(EXTRA_MESSAGE, message);
                    view.getContext().startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("Main Adapter", "Json object get edilmiyor.");
        }

    }

    @Override
    public int getItemCount() {
        return mUserData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mFirstName;

        public ViewHolder(View itemView) {
            super(itemView);
            mFirstName = (TextView) itemView.findViewById(R.id.first_name);
        }
    }
}
