package com.wristcode.deliwala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.extra.IConstants;

import org.json.JSONArray;
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
 * Created by Ajay Jagadish on 20-Apr-18.
 */

public class ProfileActivity extends AppCompatActivity implements IConstants
{
    TextView titleText, txtusername, contact, txtcontact, emailid, txtemail, address, txtaddress;
    SharedPreferences pref;
    String add;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_profile);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        titleText = findViewById(R.id.titleText);
        txtusername = findViewById(R.id.txtusername);
        contact = findViewById(R.id.contact);
        txtcontact = findViewById(R.id.txtcontact);
        emailid = findViewById(R.id.emailid);
        txtemail = findViewById(R.id.txtemail);
        address = findViewById(R.id.address);
        txtaddress = findViewById(R.id.txtaddress);

        Typeface font = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");

        titleText.setTypeface(font);
        txtusername.setTypeface(font);
        contact.setTypeface(font);
        txtcontact.setTypeface(font1);
        emailid.setTypeface(font);
        txtemail.setTypeface(font1);
        address.setTypeface(font);
        txtaddress.setTypeface(font1);

        txtusername.setText(pref.getString("Name", "").toString());
        txtcontact.setText(pref.getString("PhoneNo","").toString());
        txtemail.setText(pref.getString("Email","").toString());

        new AsyncAddress().execute(pref.getString("Id", "").toString());
    }

    private class AsyncAddress extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(API_PATH+"customer/get-profile");
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
                        .appendQueryParameter("apiKey", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    if (jArray.length() == 0)
                    {
                        txtaddress.setText("");
                    }
                    else
                    {
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject json_data = jArray.getJSONObject(i);
                            JSONArray jArray1 = json_data.getJSONArray("billingAddress");
                            if(jArray1.length() == 0)
                            {
                                txtaddress.setText("");
                            }
                            else
                            {
                                for (int j = 0; j < jArray1.length(); j++)
                                {
                                    JSONObject json_data1 = jArray1.getJSONObject(j);
                                    add = json_data1.getString("address");
                                    txtaddress.setText(txtaddress.getText().toString()+ add+ "\n\n");
                                }
                            }
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backButton(View v) {
        Intent i = new Intent(ProfileActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }
}
