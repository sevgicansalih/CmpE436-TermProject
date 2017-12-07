package com.example.sevgican.doktoruz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.example.sevgican.doktoruz.Constants.HOST_IP;
import static com.example.sevgican.doktoruz.Constants.username;

public class Main2Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView mTextView;
    private RecyclerView.LayoutManager mLayoutManager;
    GetHospitalData mGetHospitalData;
    ImageView addIconView;
    ImageView deleteIconView;
    addUserTask mAddUserTask;
    deleteUserTask mDeleteUserTask;
    String hospitalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTextView = (TextView) findViewById(R.id.text_main2);
        addIconView = (ImageView) findViewById(R.id.addButton);
        deleteIconView = (ImageView) findViewById(R.id.deleteButton);

        addIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddUserTask = new addUserTask(username);
                mAddUserTask.execute();
            }
        });

        deleteIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteUserTask = new deleteUserTask(username);
                mDeleteUserTask.execute();
            }
        });
        Intent intent = getIntent();
        hospitalid = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);


        refresh();
    }

    private void refresh() {
        mGetHospitalData = new GetHospitalData(hospitalid);
        mGetHospitalData.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void setAdp(){
        mRecyclerView.setAdapter(mAdapter);
        Log.e("Main2 Activity","adapter set edildi.");
    }
    private class addUserTask extends AsyncTask<Void, Void, Boolean> {
        String mUsername = "";
        String mPassword = "";


        String sentence;
        String modifiedSentence;
        JSONArray temp;
        JSONObject result;

        public addUserTask(String username) {
            mUsername = username;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.e("addUserTask", "async calisti aga");
            //skip here for now
            try {
                Log.e("addUserTask", "socket baglanamadi sanki ???");

                Socket clientSocket = new Socket(HOST_IP, 50000);

                Log.e("addUserTask", "Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "INSERT INTO userselection (hospitalid,username) VALUES ("+hospitalid+",\"" + mUsername + "\")";

                outToServer.writeBytes(sentence + "\n");
                Log.e("addUserTask", "out to server oldu");

                //boolean bool = result.getString("password").equals(mPassword);

                outToServer.writeBytes("CLOSE" + "\n");

                clientSocket.close();

                return true;


            } catch (Exception e) {
                Log.e("addUserTask", "belki burda kucuk tatli bir exception vardir");
                e.printStackTrace();
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAddUserTask = null;
            Log.e("addUserTask","BOOLEAN SUCCESS OLCAK");

            //ifin icinde success olcak
            if (success) {
                // new activity
                addIconView.setVisibility(View.GONE);
                deleteIconView.setVisibility(View.VISIBLE);
                Log.e("addUserTask","Tebrikler is bitti");
                Context context = getApplicationContext();
                CharSequence text = "Registering to Hospital is Succesfull!";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                refresh();
                finish();
            } else {
                Log.e("addUserTask","Tebrikler OLMADI");

            }
        }

        @Override
        protected void onCancelled() {
            mAddUserTask = null;

        }
    }
    private class deleteUserTask extends AsyncTask<Void, Void, Boolean> {
        String mUsername = "";


        String sentence;
        String modifiedSentence;
        JSONArray temp;
        JSONObject result;

        public deleteUserTask(String username) {
            mUsername = username;

        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.e("deleteUserTask", "async calisti aga");
            //skip here for now
            try {
                Log.e("deleteUserTask", "socket baglanamadan once ???");

                Socket clientSocket = new Socket(HOST_IP, 50000);

                Log.e("deleteUserTask", "Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "DELETE FROM userselection WHERE username = (\"" + mUsername + "\") AND hospitalid="+hospitalid;

                outToServer.writeBytes(sentence + "\n");
                Log.e("deleteUserTask", "out to server oldu");

                //boolean bool = result.getString("password").equals(mPassword);

                outToServer.writeBytes("CLOSE" + "\n");

                clientSocket.close();

                return true;


            } catch (Exception e) {
                Log.e("deleteUserTask", "belki burda kucuk tatli bir exception vardir");

                e.printStackTrace();
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mDeleteUserTask = null;
            Log.e("deleteUserTask","BOOLEAN SUCCESS OLCAK");

            //ifin icinde success olcak
            if (success) {
                // new activity
                addIconView.setVisibility(View.VISIBLE);
                deleteIconView.setVisibility(View.GONE);
                Log.e("deleteUserTask","Tebrikler is bitti");
                Context context = getApplicationContext();
                CharSequence text = "Deletion from hospital is Succesfull!";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(context, text, duration).show();
                refresh();
                finish();
            } else {
                Log.e("deleteUserTask","Tebrikler delete de olmadi");
            }
        }

        @Override
        protected void onCancelled() {
            mDeleteUserTask = null;

        }
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

                Socket clientSocket = new Socket(HOST_IP, 50000);

                Log.e("Main2 Activity","Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "SELECT hospitalname FROM hospitals WHERE hospitalid=\""+mHospitalid+"\"";

                outToServer.writeBytes(sentence + "\n");

                Log.e("Main2 Activity","out to server oldu");

                modifiedSentence = inFromServer.readLine();

                temp = new JSONArray(modifiedSentence);

                Log.e("Main2 Activity","json geldi.");
                if(temp.length()>0) {
                    name = temp.getJSONObject(0).getString("hospitalname");

                    sentence = "SELECT username FROM userselection WHERE hospitalid=\"" + mHospitalid + "\"";

                    outToServer.writeBytes(sentence + "\n");
                    outToServer.flush();
                    Log.e("Main2 Activity", "out to server oldu");

                    modifiedSentence = inFromServer.readLine();

                    Log.e("Main2 Activity", "json geldi.");

                    temp2 = new JSONArray(modifiedSentence);

                    outToServer.writeBytes("CLOSE" + "\n");
                    outToServer.flush();
                }else{

                    outToServer.writeBytes("CLOSE" + "\n");
                    outToServer.flush();

                }

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
            if (jar.length()>0) {
                // new activity
                Log.e("Main2 Activity","adapter set etmeye calisiyorum.");
                Log.e("Main2 Activity", String.valueOf(jar.length()));

                mAdapter = new Main2Adapter(jar,addIconView,deleteIconView);
                setAdp();
                return;
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("Main2 Activity","buralar hep dutluktu");

                addIconView.setVisibility(View.VISIBLE);
                deleteIconView.setVisibility(View.GONE);

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
