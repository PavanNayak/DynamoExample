package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Ajay Jagadish on 26-Feb-18.
 */

public class OTPActivity extends AppCompatActivity implements IConstants {
    TextView tvtitle, tvsubtitle, tvotp, tvresendotp;
    static EditText valueotp;
    Button btn_verify;
    SharedPreferences pref;
    String inputotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tvsubtitle = (TextView) findViewById(R.id.tvsubtitle);
        tvotp = (TextView) findViewById(R.id.txtotp);
        tvresendotp = (TextView) findViewById(R.id.resendotp);
        valueotp = (EditText) findViewById(R.id.valueotp);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        pref = PreferenceManager.getDefaultSharedPreferences(OTPActivity.this);
        inputotp = pref.getString("Otp", "").toString();

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");

        tvtitle.setTypeface(font);
        tvsubtitle.setTypeface(font2);
        tvotp.setTypeface(font2);
        valueotp.setTypeface(font2);
        btn_verify.setTypeface(font2);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputotp.equals(valueotp.getText().toString()))
                {
                    if (pref.getString("UserType", "").toString().equals("new"))
                    {
                        new AsyncRegister().execute(pref.getString("Name", "").toString(), pref.getString("Email", "").toString(), pref.getString("PhoneNo", "").toString(), pref.getString("Profile", "").toString());
                    }
                    Intent i = new Intent(OTPActivity.this, SelectLocationActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(OTPActivity.this, "Please enter a valid OTP!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvresendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncResendOtp().execute(pref.getString("PhoneNo", "").toString());
            }
        });
    }

    private class AsyncRegister extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OTPActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/customer/register");
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
                        .appendQueryParameter("path", params[3]);
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
                if (jsonObject.getString("status").equals("true")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(OTPActivity.this);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("Id", jsonObject1.getString("apiKey").toString());
                    editor1.apply();

                    Intent i = new Intent(OTPActivity.this, SelectLocationActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                Toast.makeText(OTPActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncResendOtp extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OTPActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/customer/resend-otp");
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

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("mobileNumber", params[0]);
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
                if (jsonObject.getString("status").equals("true")) {
                    Intent i = new Intent(OTPActivity.this, SelectLocationActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                Toast.makeText(OTPActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void receivedSms(final String message) {
        Log.i("SMSReceiv", message);
        valueotp.setText(message);
    }
}
