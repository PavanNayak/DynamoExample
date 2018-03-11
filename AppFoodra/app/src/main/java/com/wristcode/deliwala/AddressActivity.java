package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AddressActivity extends AppCompatActivity
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    TextView txtdelivery, txtfulladdress, txtaddress, txtchange, txttagaddress, txthome, txtwork, txtothers, txtname, txtmobileno;
    EditText txtflatno, txtlandmark;
    Button saveAddress;
    LinearLayout linearhome, linearwork, linearothers;
    SharedPreferences pref1;
    ImageView imghome, imgwork, imgothers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_address);
        pref1 = PreferenceManager.getDefaultSharedPreferences(AddressActivity.this);
        txtdelivery = (TextView) findViewById(R.id.txtdelivery);
        txtfulladdress = (TextView) findViewById(R.id.txtfulladdress);
        txtaddress = (TextView) findViewById(R.id.txtaddress);
        txtchange = (TextView) findViewById(R.id.txtchange);
        txtflatno = (EditText) findViewById(R.id.txtflatno);
        txtlandmark = (EditText) findViewById(R.id.txtlandmark);
        txttagaddress = (TextView) findViewById(R.id.txttagaddress);
        txthome = (TextView) findViewById(R.id.txthome);
        txtwork = (TextView) findViewById(R.id.txtwork);
        txtothers = (TextView) findViewById(R.id.txtothers);
        imghome = (ImageView) findViewById(R.id.imghome);
        imgwork = (ImageView) findViewById(R.id.imgwork);
        imgothers = (ImageView) findViewById(R.id.imgothers);
        txtname = (TextView) findViewById(R.id.txtname);
        txtmobileno = (TextView) findViewById(R.id.txtmobileno);
        saveAddress = (Button) findViewById(R.id.saveAddress);
        linearhome = (LinearLayout) findViewById(R.id.linearhome);
        linearwork = (LinearLayout) findViewById(R.id.linearwork);
        linearothers = (LinearLayout) findViewById(R.id.linearothers);

        txtaddress.setText(pref1.getString("Address", "").toString());
        txtflatno.setText(pref1.getString("HouseNumber", "").toString());
        txtlandmark.setText(pref1.getString("Landmark", "").toString());
        txtname.setText(pref1.getString("Name", "").toString());
        txtmobileno.setText(pref1.getString("PhoneNo", "").toString());

        linearhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imghome.setColorFilter(Color.parseColor("#DC143C"));
                txthome.setTextColor(Color.parseColor("#DC143C"));
                imgwork.setColorFilter(Color.parseColor("#C0C0C0"));
                txtwork.setTextColor(Color.parseColor("#C0C0C0"));
                imgothers.setColorFilter(Color.parseColor("#C0C0C0"));
                txtothers.setTextColor(Color.parseColor("#C0C0C0"));
            }
        });

        linearwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imghome.setColorFilter(Color.parseColor("#C0C0C0"));
                txthome.setTextColor(Color.parseColor("#C0C0C0"));
                imgwork.setColorFilter(Color.parseColor("#DC143C"));
                txtwork.setTextColor(Color.parseColor("#DC143C"));
                imgothers.setColorFilter(Color.parseColor("#C0C0C0"));
                txtothers.setTextColor(Color.parseColor("#C0C0C0"));
            }
        });

        linearothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imghome.setColorFilter(Color.parseColor("#C0C0C0"));
                txthome.setTextColor(Color.parseColor("#C0C0C0"));
                imgwork.setColorFilter(Color.parseColor("#C0C0C0"));
                txtwork.setTextColor(Color.parseColor("#C0C0C0"));
                imgothers.setColorFilter(Color.parseColor("#DC143C"));
                txtothers.setTextColor(Color.parseColor("#DC143C"));
            }
        });

        txtchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AddressActivity.this, LocationSelectActivity.class);
                startActivity(i);
            }
        });
        //new AsyncAddress().execute(preferences1.getString("Id", "").toString());

//        txtchange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent i = new Intent(AddressActivity.this, LocationSelectActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });

        saveAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class AsyncAddress extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddressActivity.this);
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
                        //setContentView(R.layout.activity_order_history_empty);
                    }
                    else
                    {
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

//                mAdapter = new AddressAdapter(AddAddressActivity.this, data);
//                recyclerView.setLayoutManager(new LinearLayoutManager(AddAddressActivity.this));
//                recyclerView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();

            }
            catch (JSONException e)
            {
                Toast.makeText(AddressActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backButton(View v)
    {
        Intent i = new Intent(AddressActivity.this, AddAddressActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(AddressActivity.this, AddAddressActivity.class);
        startActivity(i);
        finish();
    }
}
