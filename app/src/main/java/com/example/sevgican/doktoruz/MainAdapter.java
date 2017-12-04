package com.example.sevgican.doktoruz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sevgican on 05.12.2017.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    String [] mUser;

    public MainAdapter(String[] str) {
        mUser = str;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.mFirstName.setText("asdf");
    }

    @Override
    public int getItemCount() {
        return mUser.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mFirstName;
        public ViewHolder(View itemView) {
            super(itemView);

            mFirstName = (TextView) itemView.findViewById(R.id.first_name);
        }
    }
}