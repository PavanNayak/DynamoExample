package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wristcode.deliwala.extra.CustomVolleyRequest;
import com.wristcode.deliwala.extra.IConstants;
import com.wristcode.deliwala.extra.TransparentProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ajay Jagadish on 15-Mar-18.
 */

public class LoginDetailsActivity extends AppCompatActivity implements IConstants, GoogleApiClient.OnConnectionFailedListener
{
    TextView txtwelcome, txtsignin, txtusername, txtemail;
    EditText valueusername, valueemail;
    Button verifyButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private ImageLoader imageLoader;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_details);
        txtusername = findViewById(R.id.txtusername);
        valueusername = findViewById(R.id.valueusername);
        txtemail = findViewById(R.id.txtemail);
        valueemail = findViewById(R.id.valueemail);
        txtwelcome = findViewById(R.id.txtwelcome);
        txtsignin = findViewById(R.id.txtsignin);
        verifyButton = findViewById(R.id.verifyButton);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txtwelcome.setTypeface(font);
        txtsignin.setTypeface(font1);
        verifyButton.setTypeface(font1);
        txtusername.setTypeface(font1);
        txtemail.setTypeface(font1);
        valueemail.setTypeface(font1);
        valueusername.setTypeface(font1);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginDetailsActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signIn();

        valueusername.setText(pref.getString("Name", "").toString());
        valueemail.setText(pref.getString("Email", "").toString());
    }

    private static final int RC_SIGN_IN = 9001;

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == LoginActivity.RESULT_OK)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
        {
            Toast.makeText(this, "Sorry, We could not fetch your Name & Email Id!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
            valueusername.setText(acct.getDisplayName().toString());
            valueemail.setText(acct.getEmail().toString());
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Name", acct.getDisplayName());
            editor.putString("Email", acct.getEmail());
            editor.putString("Profile", acct.getPhotoUrl().toString());
            editor.apply();
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public void verifyButton(View v)
    {
        if(isNetworkAvailable())
        {
            if (valueusername.getText().toString().matches("") || valueemail.getText().toString().matches(""))
            {
                Toast.makeText(LoginDetailsActivity.this, "Name and Email ID required!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref1.edit();
                editor.putString("Name", valueusername.getText().toString());
                editor.putString("Email", valueemail.getText().toString());
                editor.putString("Profile", "");
                editor.apply();

                new AsyncRegister().execute(pref.getString("Name", "").toString(), pref.getString("Email", "").toString(), pref.getString("PhoneNo", "").toString(), pref.getString("Profile", "").toString(), pref.getString("tokenId","").toString());
            }
        }
        else
        {
            Toast.makeText(LoginDetailsActivity.this, "Check your internet connection and try again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncRegister extends AsyncTask<String, String, String>
    {
        TransparentProgressDialog pdLoading = new TransparentProgressDialog(LoginDetailsActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(API_PATH+"customer/register");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("email", params[1])
                        .appendQueryParameter("mobileNumber", params[2])
                        .appendQueryParameter("path", params[3])
                        .appendQueryParameter("tokenId", params[4]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }

            try {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return (result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            Toast.makeText(LoginDetailsActivity.this,result, Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("Id", jsonObject1.getString("apiKey").toString());
                    editor1.apply();

                    Intent i = new Intent(LoginDetailsActivity.this, SelectLocationActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                Toast.makeText(LoginDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}