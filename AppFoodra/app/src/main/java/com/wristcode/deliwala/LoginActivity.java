package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Ajay Jagadish on 20-Feb-18.
 */

public class LoginActivity extends AppCompatActivity implements IConstants
{
    TextView txtwelcome, txtsignin, txtphone;
    EditText valuephone;
    Button verifyButton;
    SharedPreferences pref;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtwelcome = findViewById(R.id.txtwelcome);
        txtsignin = findViewById(R.id.txtsignin);
        txtphone = findViewById(R.id.txtphone);
        valuephone = findViewById(R.id.valuephone);
        verifyButton = findViewById(R.id.verifyButton);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txtwelcome.setTypeface(font);
        txtsignin.setTypeface(font1);
        txtphone.setTypeface(font1);
        valuephone.setTypeface(font1);
        verifyButton.setTypeface(font1);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();
        editor1.putString("fg","0");
        editor1.apply();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {}
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {}
            else
            {
                //Toast.makeText(this, "You denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void verifyButton(View v)
    {
        if(isNetworkAvailable())
        {
            if (valuephone.getText().toString().matches(""))
            {
                Toast.makeText(LoginActivity.this, "You must enter your mobile number!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                new AsyncPreRegister().execute(valuephone.getText().toString().trim(), pref.getString("tokenId","").toString());
            }
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Check your internet connection and try again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncPreRegister extends AsyncTask<String, String, String>
    {
        TransparentProgressDialog pdLoading = new TransparentProgressDialog(LoginActivity.this);
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
                url = new URL(API_PATH+"customer/pre-register");
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
                        .appendQueryParameter("mobileNumber", params[0])
                        .appendQueryParameter("tokenId", params[1]);
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("UserType", jsonObject.getString("type").toString());
                    editor1.putString("Otp", jsonObject1.getString("otp").toString());

                    if (jsonObject1.has("apiKey"))
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
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}