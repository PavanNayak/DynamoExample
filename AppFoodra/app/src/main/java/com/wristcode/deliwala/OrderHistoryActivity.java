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
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.OrderHistoryItems;
import com.wristcode.deliwala.adapter.OrderHistoryAdapter;
import com.wristcode.deliwala.Pojo.OrderHistory;
import com.wristcode.deliwala.adapter.OrderHistoryItemAdapter;

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
 * Created by Ajay Jagadish on 09-Sep-17.
 */

public class OrderHistoryActivity extends AppCompatActivity
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    List<OrderHistoryItems> data1;
    RecyclerView recyclerView;
    OrderHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_order_history);
        recyclerView = (RecyclerView) findViewById(R.id.orderRecycler);
        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(OrderHistoryActivity.this);




        Toast.makeText(this,preferences1.getString("Id",""), Toast.LENGTH_SHORT).show();


        new AsyncOrderHistory().execute(preferences1.getString("Id", "").toString());
    }

    private class AsyncOrderHistory extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OrderHistoryActivity.this);
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/customer/order/list");
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
            List<OrderHistoryItems> data1 = new ArrayList<>();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    if (jArray.length() == 0)
                    {
                        setContentView(R.layout.activity_order_history_empty);
                    }
                    else
                    {
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject json_data = jArray.getJSONObject(i);
                            OrderHistory fishData = new OrderHistory();
                            fishData.oId = json_data.getString("id");
                            fishData.oDate = json_data.getString("orderDate");
                            fishData.oTotal = json_data.getString("actualAmount");
                            fishData.oPayType = json_data.getString("paymentType");
                            fishData.oStatus = json_data.getString("orderStatus");


                            JSONArray jArray1 = json_data.getJSONArray("customerOrderItems");
                            for (int j = 0; j < jArray1.length(); j++)
                            {
                                JSONObject json_data1 = jArray1.getJSONObject(j);
                                OrderHistoryItems fishData1 = new OrderHistoryItems();
                                fishData1.ohItemname = json_data1.getString("itemName");
                                fishData1.ohItemqty = json_data1.getString("quantity");
                                fishData1.ohItemprice = json_data1.getString("actualAmount");
                                data1.add(fishData1);
                            }

                            fishData.oItems = json_data.getJSONArray("customerOrderItems").toString();


                            fishData.oItems = json_data.getJSONArray("customerOrderItems").toString();


                            fishData.oItems = json_data.getJSONArray("customerOrderItems").toString();


                            JSONObject jObject = json_data.getJSONObject("restaurant");
                            fishData.oResId = jObject.getString("id");
                            fishData.oResName = jObject.getString("restaurantName");
                            fishData.oResImage = jObject.getString("iconImage");
                            data.add(fishData);
                        }
                    }
                }

                mAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, data);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Toast.makeText(OrderHistoryActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backButton(View v) {
        Intent i = new Intent(OrderHistoryActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OrderHistoryActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }
}
