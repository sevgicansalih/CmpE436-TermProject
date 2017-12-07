package com.example.sevgican.doktoruz;

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
 * Created by sevgican on 06.12.2017.
 */

class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    JSONArray mHospitalData;
    String item = "";

    public HospitalAdapter(JSONArray jar) {
        mHospitalData = jar;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HospitalAdapter.ViewHolder holder, final int position) {
        try {
            Log.e("Hospital Adapter","itemleri cekiyorum");

            item = mHospitalData.getJSONObject(position).getString("hospitalname");
            item += "\nCity: ";
            item += mHospitalData.getJSONObject(position).getString("cityname");
            item += "\nCapacity: ";
            item += mHospitalData.getJSONObject(position).getString("capacity");

            holder.mFirstName.setText(item);

            holder.mFirstName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Main2Activity.class);
                    String message = null;
                    try {
                        message = mHospitalData.getJSONObject(position).getString("hospitalid");
                    } catch (JSONException e) {
                        Log.e("Hospital Adapter", String.valueOf(e));
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
        return mHospitalData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mFirstName;

        public ViewHolder(View view) {
            super(view);
            mFirstName = (TextView) view.findViewById(R.id.hospital_name);
        }
    }
}
