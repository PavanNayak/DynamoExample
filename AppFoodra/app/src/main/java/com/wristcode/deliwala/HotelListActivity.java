package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Hotels;
import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.adapter.HotelAdapter;
import com.wristcode.deliwala.adapter.RestaurantsAdapter;
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

public class HotelListActivity extends AppCompatActivity implements IConstants
{
    RecyclerView recyclerView;
    List<Restaurants> data;
    EditText EditSearch;
    HotelAdapter adapter;
    private List<Hotels> categoriesList;
    RestaurantsAdapter adapter1;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hotel_list);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        EditSearch = findViewById(R.id.EditSearch);
        EditSearch.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        EditSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter(s.toString());
                // filter your list from your input

                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        recyclerView = findViewById(R.id.recyclerHotel);
        categoriesList = new ArrayList<>();
        new AsyncRestaurants().execute(pref.getString("Latitude","").toString(), pref.getString("Longitiude", "").toString(), radiusKm);
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Manipal");
        spinnerArray.add("Udupi");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.spinner1);
        sItems.setAdapter(adapter);
    }

    void filter(String text)
    {
        List<Restaurants> temp = new ArrayList();
        for(Restaurants d: data)
        {
            if(d.getResname().contains(text))
            {
                temp.add(d);
            }
        }
        adapter1.updateList(temp);
    }

    private class AsyncRestaurants extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(HotelListActivity.this);
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
                url = new URL(API_PATH+"all-restaurant");
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

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("latitude", params[0])
                        .appendQueryParameter("longitude", params[1])
                        .appendQueryParameter("radiusKm", params[2]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
            data = new ArrayList<>();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject jData = jArray.getJSONObject(i);
                        JSONObject json_data = jData.getJSONObject("0");
                        Restaurants resData = new Restaurants();
                        resData.resid = json_data.getString("id");
                        resData.resname = json_data.getString("restaurantName");
                        if(json_data.has("description")) {
                            resData.resdescp = json_data.getString("description");
                        }
                        else {
                            resData.resdescp = "No Description";
                        }
                        if(json_data.has("restaurantAddress")) {
                            resData.resadd = json_data.getString("restaurantAddress");
                        }
                        else
                        {
                            resData.resadd = "No Address";
                        }
                        resData.reslat = json_data.getString("restaurantLat");
                        resData.reslong = json_data.getString("restaurantLong");
                        resData.resmob = json_data.getString("primaryMobile");
                        resData.resopentime = json_data.getString("openTime");
                        resData.resclosetime = json_data.getString("closeTime");
                        resData.resisopen = json_data.getString("isOpen");
                        resData.respop = json_data.getString("popularity");
                        if(json_data.has("iconImage")) {
                            resData.resimg = json_data.getString("iconImage");
                        }
                        else
                        {
                            resData.resimg = " ";
                        }
                        JSONArray jsonArray = json_data.getJSONArray("restaurantType");
                        if(!(jsonArray.length() == 0))
                        {
                            for(int j=0;j<jsonArray.length();j++)
                            {
                                JSONObject jobject2 = jsonArray.getJSONObject(j);
                                resData.restags.add(jobject2.getString("typeName"));
                            }
                        }
                        resData.resdist = jData.getString("distance");
                        data.add(resData);
                    }
                    adapter1 = new RestaurantsAdapter(HotelListActivity.this, data, recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HotelListActivity.this));
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setFocusable(false);
                    recyclerView.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(HotelListActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HotelListActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }
}
