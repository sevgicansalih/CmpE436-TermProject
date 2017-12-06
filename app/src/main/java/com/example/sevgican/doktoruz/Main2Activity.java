package com.example.sevgican.doktoruz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Main2Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView mTextView;
    private RecyclerView.LayoutManager mLayoutManager;
    GetHospitalData mGetHospitalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTextView = (TextView) findViewById(R.id.text_main2);
        Intent intent = getIntent();
        String hospitalid = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        mGetHospitalData = new GetHospitalData(hospitalid);
        mGetHospitalData.execute();

    }
    public void setAdp(){
        mRecyclerView.setAdapter(mAdapter);
        Log.e("Main2 Activity","adapter set edildi.");
    }
    public class GetHospitalData extends AsyncTask<String, Void, JSONArray> {

        private final String mHospitalid;
        String sentence;
        String modifiedSentence;
        JSONArray temp;
        JSONArray temp2;
        String name;

        JSONObject result;

        public GetHospitalData(String hospitalid) {
            mHospitalid=hospitalid;
        }

        @Override
        protected JSONArray doInBackground(String... hospitalid) {
            // TODO: attempt authentication against a network service.
            Log.e("Main2 Activity","async calisti aga");
            //skip here for now
            try {
                Log.e("Main2 Activity","socket baglanamadi sanki ???");

                Socket clientSocket = new Socket(LoginActivity.HOST_IP, 50000);

                Log.e("Main2 Activity","Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "SELECT hospitalname FROM hospitals WHERE hospitalid=\""+mHospitalid+"\"";

                outToServer.writeBytes(sentence + "\n");

                Log.e("Main2 Activity","out to server oldu");

                modifiedSentence = inFromServer.readLine();

                temp = new JSONArray(modifiedSentence);

                Log.e("Main2 Activity","json geldi.");

                name = temp.getJSONObject(0).getString("hospitalname");

                sentence = "SELECT username FROM userselection WHERE hospitalid=\""+mHospitalid+"\"";

                outToServer.writeBytes(sentence + "\n");
                outToServer.flush();
                Log.e("Main2 Activity","out to server oldu");

                modifiedSentence = inFromServer.readLine();

                Log.e("Main2 Activity","json geldi.");

                temp2 = new JSONArray(modifiedSentence);

                outToServer.writeBytes("CLOSE" + "\n");
                outToServer.flush();


                clientSocket.close();

                return temp2;



            } catch (Exception e) {
                Log.e("Main2 Activity","belki burda kucuk tatli bir exception vardir");
                Log.e("Main2 Activity", String.valueOf(e));
                e.printStackTrace();
                return null;
            }
            //return true;
            // TODO: register the new account here.

        }



        @Override
        protected void onPostExecute(final JSONArray jar) {
            mGetHospitalData = null;
            Log.e("Main2 Activity","post execute girdim");
            setText(name);

            //ifin icinde success olcak
            if (jar!=null) {
                // new activity
                Log.e("Main2 Activity","adapter set etmeye calisiyorum.");

                mAdapter = new Main2Adapter(jar);
                setAdp();
                return;
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("Main2 Activity","buralar hep dutluktu");

            }
        }

        @Override
        protected void onCancelled() {
            mGetHospitalData = null;
            //showProgress(false);
        }
    }

    private void setText(String name) {
        try {
            mTextView.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
