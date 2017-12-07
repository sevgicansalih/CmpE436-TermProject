package com.example.sevgican.doktoruz;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.sevgican.doktoruz.Constants.username;
import static com.example.sevgican.doktoruz.LoginActivity.EXTRA_MESSAGE;

/**
 * Created by sevgican on 06.12.2017.
 */

class Main2Adapter extends RecyclerView.Adapter<Main2Adapter.ViewHolder> {
    JSONArray mHospitalData;
    String item = "";
    ImageView addIconView;
    ImageView deleteIconView;
    Boolean bool;

    public Main2Adapter(JSONArray jar, ImageView addIconView, ImageView deleteIconView) {
        mHospitalData = jar;
        this.addIconView = addIconView;
        this.deleteIconView = deleteIconView;
        bool = true;
    }

    @Override
    public Main2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Main2Adapter.ViewHolder holder, final int position) {

        try {
            if (mHospitalData.length()==0) {
                Log.e("Main2 Adapter","item count 0");
                item = "No user Registered to this hospital";
                holder.mFirstName.setText(item);
                addIconView.setVisibility(View.VISIBLE);
                deleteIconView.setVisibility(View.GONE);

            } else {
                Log.e("Main2 Adapter", "itemleri cekiyorum");
                item = "Registered User # " + (position + 1) + " :";
                String str = mHospitalData.getJSONObject(position).getString("username");
                item += str;
                Log.e("Main2 Adapter", "length : "+mHospitalData.length()+"username :"+username);

                if (mHospitalData.length() > 0) {
                    //Log.e("Main2 Adapter", "boolean ? : "+mHospitalData.getJSONObject(position).getString("username").equals(username));
                    if(bool) {
                        if (mHospitalData.getJSONObject(position).getString("username").equals(username)) {
                            addIconView.setVisibility(View.GONE);
                            deleteIconView.setVisibility(View.VISIBLE);
                            bool = false;
                        } else {
                            addIconView.setVisibility(View.VISIBLE);
                            deleteIconView.setVisibility(View.GONE);
                        }
                    }
                    holder.mFirstName.setText(item);
                }else{
                    addIconView.setVisibility(View.VISIBLE);
                    deleteIconView.setVisibility(View.GONE);
                }

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