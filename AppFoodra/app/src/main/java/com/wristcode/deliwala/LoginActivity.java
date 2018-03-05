package com.wristcode.deliwala;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wristcode.deliwala.extra.CustomVolleyRequest;
import com.wristcode.deliwala.extra.IConstants;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ajay Jagadish on 20-Feb-18.
 */

public class LoginActivity extends AppCompatActivity implements IConstants, GoogleApiClient.OnConnectionFailedListener
{
    TextView txtwelcome, txtsignin, txtphone;
    EditText valuephone;
    Button verifyButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private ImageLoader imageLoader;
    SharedPreferences pref;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtwelcome = (TextView) findViewById(R.id.txtwelcome);
        txtsignin = (TextView) findViewById(R.id.txtsignin);
        txtphone = (TextView) findViewById(R.id.txtphone);
        valuephone = (EditText) findViewById(R.id.valuephone);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txtwelcome.setTypeface(font);
        txtsignin.setTypeface(font1);
        txtphone.setTypeface(font1);
        valuephone.setTypeface(font1);
        verifyButton.setTypeface(font1);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED){
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE);
        }


        signIn();
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

            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor1 = preferences1.edit();
            editor1.putString("flag", "1");
            editor1.apply();
        }
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Name", acct.getDisplayName().toString());
            editor.putString("Email", acct.getEmail().toString());
            editor.putString("Profile", acct.getPhotoUrl().toString());
            editor.apply();


        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {}
            else
            {
                //Toast.makeText(this, "You denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncPreRegister extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/customer/pre-register");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return "exception";
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("mobileNumber", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return "exception";
            }

            try
            {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return(result.toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            pdLoading.dismiss();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("UserType", jsonObject.getString("type").toString());
                    editor1.putString("Otp", jsonObject1.getString("otp").toString());
                    if(jsonObject1.has("apiKey"))
                    {
                        editor1.putString("Id", jsonObject1.getString("apiKey").toString());
                    }
                    else
                    {
                        editor1.putString("Id", "");
                    }
                    editor1.putString("PhoneNo", valuephone.getText().toString().trim());
                    editor1.apply();

                    Intent i = new Intent(LoginActivity.this, OTPActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void verifyButton(View v)
    {
        if (valuephone.getText().toString().matches(""))
        {
            Toast.makeText(LoginActivity.this, "You must enter your mobile number!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            new AsyncPreRegister().execute(valuephone.getText().toString().trim());
        }
    }
}
