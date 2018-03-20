package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Address;
import com.wristcode.deliwala.adapter.AddressAdapter;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 10-Mar-18.
 */

public class AddAddressActivity extends AppCompatActivity
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    RecyclerView recyclerView;
    AddressAdapter mAdapter;
    TextView titletext, txttitleadd, txtaddaddress;
    Button addAddress;
    SharedPreferences preferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_address);
        titletext = (TextView) findViewById(R.id.titletext);
        txttitleadd = (TextView) findViewById(R.id.txttitleadd);
        txtaddaddress = (TextView) findViewById(R.id.txtaddaddress);
        addAddress = (Button) findViewById(R.id.addAddress);
        recyclerView = (RecyclerView) findViewById(R.id.addressRecycler);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        titletext.setTypeface(font);
        txttitleadd.setTypeface(font);
        addAddress.setTypeface(font1);
        preferences1 = PreferenceManager.getDefaultSharedPreferences(AddAddressActivity.this);
        new AsyncAddress().execute(preferences1.getString("Id", "").toString());

        txtaddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(AddAddressActivity.this);
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putString("AddressFlag", "0");
                editor1.apply();

                Intent i = new Intent(AddAddressActivity.this, AddressActivity.class);
                startActivity(i);
                finish();
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(preferences1.getString("Position","").toString().equals("-1"))
                {
                    Toast.makeText(AddAddressActivity.this, "Please select an address..", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(AddAddressActivity.this, PaymentActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private class AsyncAddress extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddAddressActivity.this);
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/customer/get-profile");
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
            pdLoading.dismiss();
            List<Address> data = new ArrayList<>();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    if (jArray.length() == 0)
                    {
                        addAddress.setVisibility(View.GONE);
                    }
                    else
                    {
                        addAddress.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject json_data = jArray.getJSONObject(i);
                            JSONArray jArray1 = json_data.getJSONArray("billingAddress");
                            for (int j = 0; j < jArray1.length(); j++)
                            {
                                JSONObject json_data1 = jArray1.getJSONObject(j);
                                Address fishData = new Address();
                                fishData.userid = json_data1.getString("id");
                                fishData.username = json_data1.getString("fullName");
                                fishData.useraddress = json_data1.getString("address");
                                fishData.userlat = json_data1.getString("lattitude");
                                fishData.userlong = json_data1.getString("logingitude");
                                data.add(fishData);
                            }
                        }
                    }
                }

                mAdapter = new AddressAdapter(AddAddressActivity.this, data);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddAddressActivity.this));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
            catch (JSONException e)
            {
                Toast.makeText(AddAddressActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backButton(View v)
    {
        Intent i = new Intent(AddAddressActivity.this, CartActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(AddAddressActivity.this, CartActivity.class);
        startActivity(i);
        finish();
    }
}
