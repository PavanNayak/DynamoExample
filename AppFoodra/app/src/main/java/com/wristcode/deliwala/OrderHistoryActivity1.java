package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.OrderHistory;
import com.wristcode.deliwala.adapter.OrderHistoryAdapter;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 19-Apr-18.
 */

public class OrderHistoryActivity1 extends AppCompatActivity implements IConstants
{
    RecyclerView recyclerView;
    OrderHistoryAdapter mAdapter;
    SharedPreferences preferences1;
    TextView txtnotify;
    ImageView imgnotify;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_order_history1);
        preferences1 = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.orderRecycler);
        imgnotify = findViewById(R.id.imgnotify);
        txtnotify = findViewById(R.id.txtnotify);

        if(isNetworkAvailable())
        {
            new AsyncOrderHistory().execute(preferences1.getString("Id", "").toString());
        }
        else
        {
            Toast.makeText(OrderHistoryActivity1.this, "Check your internet connection and try again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncOrderHistory extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OrderHistoryActivity1.this);
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
                url = new URL(API_PATH+"customer/order/list");
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
            List<OrderHistory> data = new ArrayList<>();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    if (jArray.length() == 0)
                    {
                        recyclerView.setVisibility(View.GONE);
                        imgnotify.setVisibility(View.VISIBLE);
                        txtnotify.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        boolean orderFlag = false;
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject json_data = jArray.getJSONObject(i);
                            OrderHistory fishData = new OrderHistory();
                            if(json_data.getString("orderStatus").equals("delivered") || json_data.getString("orderStatus").equals("cancelled"))
                            {
                                orderFlag = true;
                                fishData.oId = json_data.getString("id");
                                fishData.oDate = json_data.getString("orderDate");
                                fishData.oTotal = json_data.getString("orderAmount");
                                fishData.oPayType = json_data.getString("paymentType");
                                fishData.oStatus = json_data.getString("orderStatus");
                                fishData.oCusAdd = json_data.getString("address");
                                fishData.oItems = json_data.getJSONArray("customerOrderItems").toString();

                                JSONObject jObject = json_data.getJSONObject("restaurant");
                                fishData.oResId = jObject.getString("id");
                                fishData.oResName = jObject.getString("restaurantName");
                                fishData.oResImage = jObject.getString("iconImage");
                                data.add(fishData);
                            }
                        }
                        if(orderFlag == false)
                        {
                            recyclerView.setVisibility(View.GONE);
                            imgnotify.setVisibility(View.VISIBLE);
                            txtnotify.setVisibility(View.VISIBLE);
                        }
                    }
                }

                mAdapter = new OrderHistoryAdapter(OrderHistoryActivity1.this, data);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity1.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Toast.makeText(OrderHistoryActivity1.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void backButton(View v) {
        Intent i = new Intent(OrderHistoryActivity1.this, OrderHistoryActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OrderHistoryActivity1.this, OrderHistoryActivity.class);
        startActivity(i);
        finish();
    }
}
