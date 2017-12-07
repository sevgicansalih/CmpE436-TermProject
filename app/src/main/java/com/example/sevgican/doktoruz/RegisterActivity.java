package com.example.sevgican.doktoruz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.example.sevgican.doktoruz.Constants.*;

public class RegisterActivity extends AppCompatActivity {
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mSignupFormView;
    private TextView mTextView;
    private UserSignupTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username_signup);
        mPasswordView = (EditText) findViewById(R.id.password_signup);
        mSignupFormView = (View) findViewById(R.id.signup_form);
        mProgressView = (View) findViewById(R.id.signup_progress);
        mTextView = (TextView) findViewById(R.id.textView2);
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);

        Button signUpButton = (Button) findViewById(R.id.register_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
    }


    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return username.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        //return password.length() > 4;
        return password.length() > 0;
    }

    private void attemptSignup() {
        if (mAuthTask != null) {
            return;
        }
        Log.e("Signup Activity", "ATTEMPT SIGNUP STARTS!!!");

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            Log.e("Signup Activity", "cancel true olcak");

            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            Log.e("Signup Activity", "burda hata var");

            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            Log.e("Signup Activity", "username valid degil");

            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            Log.e("Signup Activity", "Cancel true oldu");

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Log.e("Signup Activity", "show progress geldi");

            mAuthTask = new UserSignupTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignupFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            mTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mTextView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class UserSignupTask extends AsyncTask<Void, Void, Boolean> {
        String mUsername = "";
        String mPassword = "";


        String sentence;
        String modifiedSentence;
        JSONArray temp;
        JSONObject result;

        public UserSignupTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.e("Signup Activity", "async calisti aga");
            //skip here for now
            try {
                Log.e("Signup Activity", "socket baglanamadi sanki ???");

                Socket clientSocket = new Socket(HOST_IP, 50000);

                Log.e("Signup Activity", "Socket baglandi.");

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sentence = "INSERT INTO users (username,password) VALUES (\"" + mUsername + "\",\"" + mPassword + "\")";

                outToServer.writeBytes(sentence + "\n");
                Log.e("Signup Activity", "out to server oldu");

                //boolean bool = result.getString("password").equals(mPassword);

                outToServer.writeBytes("CLOSE" + "\n");

                clientSocket.close();

                return true;


            } catch (Exception e) {
                Log.e("Signup Activity", "belki burda kucuk tatli bir exception vardir");

                e.printStackTrace();
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            Log.e("Signup Activity","BOOLEAN SUCCESS OLCAK");

            showProgress(false);
            //ifin icinde success olcak
            if (success) {
                // new activity
                Log.e("Signup Activity","Tebrikler is bitti");
                Context context = getApplicationContext();
                CharSequence text = "Register Succesfull!";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(context, text, duration).show();
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
