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

class Main2Adapter extends RecyclerView.Adapter<Main2Adapter.ViewHolder> {
    JSONArray mHospitalData;
    String item = "";

    public Main2Adapter(JSONArray jar) {
        mHospitalData = jar;
    }

    @Override
    public Main2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Main2Adapter.ViewHolder holder, final int position) {

        try {
            if (getItemCount() == 0) {
                item = "No user Registered to this hospital";
            } else {
                Log.e("Main2 Adapter", "itemleri cekiyorum");
                item = "Registered User # " + (position+1)+" :";
                item += mHospitalData.getJSONObject(position).getString("username");

                holder.mFirstName.setText(item);
                //------CLICK LISTENER VAR BURDA
                /* holder.mFirstName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), Main2Activity.class);
                        String message = null;
                        try {
                            message = mHospitalData.getJSONObject(position).getString("hospitalid");
                        } catch (JSONException e) {
                            Log.e("Main2 Adapter", String.valueOf(e));
                        }
                        intent.putExtra(EXTRA_MESSAGE, message);
                        view.getContext().startActivity(intent);
                    }
                });*/
            }
        } catch (Exception e) {
            Log.e("Main2 Adapter", "Json object get edilmiyor.");
        }

    }

    @Override
    public int getItemCount() {
        return mHospitalData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mFirstName;

        public ViewHolder(View itemView) {
            super(itemView);
            mFirstName = (TextView) itemView.findViewById(R.id.first_name2);
        }
    }
}