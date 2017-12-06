package com.example.sevgican.doktoruz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HospitalsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    GetAllHospitals mGetAllHospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        mGetAllHospitals = new GetAllHospitals();
        mGetAllHospitals.execute();
    }
    public void setAdp(){
        mRecyclerView.setAdapter(mAdapter);
    }

    private class GetAllHospitals extends AsyncTask<Void,Void,JSONArray> {
        String sentence;
        String modifiedSentence;
        JSONArray temp;

        public GetAllHospitals(){}


        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                Log.e("Hospitals Activity","socket baglanamadi sanki ???");

                Socket clientSocket = new Socket(LoginActivity.HOST_IP, 50000);

                Log.e("Hospitals Activity","Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "SELECT * FROM hospitals";

                outToServer.writeBytes(sentence + "\n");

                Log.e("Main2 Activity","out to server oldu");

                modifiedSentence = inFromServer.readLine();

                temp = new JSONArray(modifiedSentence);

                Log.e("Hospitals Activity","json geldi.");

                outToServer.writeBytes("CLOSE" + "\n");
                outToServer.flush();


                clientSocket.close();

                return temp;



            } catch (Exception e) {
                Log.e("Main2 Activity","belki burda kucuk tatli bir exception vardir");
                Log.e("Main2 Activity", String.valueOf(e));
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final JSONArray jar) {
            mGetAllHospitals = null;
            Log.e("Hospitals Activity","post execute girdim");


            //ifin icinde success olcak
            if (jar!=null) {
                // new activity
                Log.e("Hospitals Activity","adapter set ettim");

                mAdapter = new HospitalAdapter(jar);
                setAdp();
                return;
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("Hospitals activity","buralar hep dutluktu");

            }
        }

        @Override
        protected void onCancelled() {
            mGetAllHospitals = null;
            //showProgress(false);
        }


    }
}
