package com.example.sevgican.doktoruz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    GetUserData mGetUserData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String username = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        mGetUserData = new GetUserData(username);
        mGetUserData.execute();

    }
    public void setAdp(){
        mRecyclerView.setAdapter(mAdapter);
    }
    public class GetUserData extends AsyncTask<String, Void, JSONArray> {

        private final String mUserName;
        String sentence;
        String modifiedSentence;
        JSONArray temp;
        JSONArray temp2;

        JSONObject result;

        public GetUserData(String username) {
            mUserName=username;
        }

        @Override
        protected JSONArray doInBackground(String... username) {
            // TODO: attempt authentication against a network service.
            Log.e("Main Activity","async calisti aga");
            //skip here for now
            try {
                Log.e("Main Activity","socket baglanamadi sanki ???");

                Socket clientSocket = new Socket(LoginActivity.HOST_IP, 50000);

                Log.e("Main Activity","Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "SELECT hospitalid FROM userselection WHERE username=\""+mUserName+"\"";

                outToServer.writeBytes(sentence + "\n");

                Log.e("Main Activity","out to server oldu");

                modifiedSentence = inFromServer.readLine();

                Log.e("Main Activity","json geldi.");

                temp = new JSONArray(modifiedSentence);
                int size = temp.length();
                sentence = "SELECT hospitalname FROM hospitals WHERE hospitalid=";
                for (int i = 0; i <size ; i++) {
                    sentence+=temp.getJSONObject(i).getInt("hospitalid");
                    if(i!=(size-1)){
                        sentence+=" OR hospitalid=";
                    }
                }
                Log.e("Main Activity",sentence);

                outToServer.writeBytes(sentence + "\n");
                Log.e("Main Activity","ikinci kez out to server "+sentence);

                modifiedSentence = inFromServer.readLine();
                Log.e("Main Activity","ikinci kez read ettim anam.");
                outToServer.writeBytes("CLOSE" + "\n");

                temp2 = new JSONArray(modifiedSentence);
                //System.out.println(temp.toString());
                //result = temp2.getJSONObject(0);

                //System.out.println("object ==> "+result.toString());
                //System.out.println("From Server : "+result.getString("password"));
                //boolean bool = result.getString("password").equals(mPassword);
                Log.e("Main Activity","Result alindi.");

                clientSocket.close();

                return temp2;



            } catch (Exception e) {
                Log.e("Main Activity","belki burda kucuk tatli bir exception vardir");
                Log.e("Main Activity", String.valueOf(e));
                return null;
            }
            //return true;
            // TODO: register the new account here.

        }



        @Override
        protected void onPostExecute(final JSONArray jar) {
            mGetUserData = null;
            Log.e("Main Activity","post execute girdim");


            //ifin icinde success olcak
            if (jar!=null) {
                // new activity
                Log.e("Main Activity","adapter set ettim");

                mAdapter = new MainAdapter(jar);
                setAdp();
                return;
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("Main activity","buralar hep dutluktu");

            }
        }

        @Override
        protected void onCancelled() {
            mGetUserData = null;
            //showProgress(false);
        }
    }
}



