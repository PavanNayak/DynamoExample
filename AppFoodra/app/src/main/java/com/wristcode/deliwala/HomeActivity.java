package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.Pojo.Tags;
import com.wristcode.deliwala.adapter.RestaurantsAdapter;
import com.wristcode.deliwala.adapter.TagsAdapter;
import com.wristcode.deliwala.extra.IConstants;
import com.wristcode.deliwala.extra.OnLoadMoreListener;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ajay Jagadish on 26-Feb-18.
 */

public class HomeActivity extends AppCompatActivity implements IConstants
{
    TextView text1, text2, txtoffer, userlocation, editSearch;
    RecyclerView menurecycler, offerrecycler;
    TagsAdapter adapter;
    RestaurantsAdapter adapter1;
    LinearLayoutManager HorizontalLayout;
    SharedPreferences pref;
    ExampleDBHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        txtoffer = findViewById(R.id.txtoffer);
        userlocation = findViewById(R.id.userlocation);
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        text1.setTypeface(font1);
        text2.setTypeface(font2);
        userlocation.setTypeface(font2);
        txtoffer.setTypeface(font2);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        Date slot1 = null;
        Date slot2 = null;
        Date slot3 = null;
        Date slot4 = null;
        Date slot5 = null;

        try
        {
            slot1 = time.parse("05:00");
            slot2 = time.parse("12:00");
            slot3 = time.parse("16:00");
            slot4 = time.parse("19:00");
            slot5 = time.parse("00:00");

            Date CurrentTime = time.parse(time.format(new Date()));
            if ((CurrentTime.after(slot1) && CurrentTime.before(slot2)))
            {
                text1.setText("Good Morning, "+pref.getString("Name","").toString());
                text2.setText("It's time for breakfast");
            }
            else if ((CurrentTime.after(slot2) && CurrentTime.before(slot3)))
            {
                text1.setText("Good Afternoon, "+pref.getString("Name","").toString());
                text2.setText("It's time for lunch");
            }
            else if ((CurrentTime.after(slot3) && CurrentTime.before(slot4)))
            {
                text1.setText("Good Evening, "+pref.getString("Name","").toString());
                text2.setText("It's time for snacks");
            }
            else if ((CurrentTime.after(slot4) && CurrentTime.before(slot5)))
            {
                text1.setText("Good Night, "+pref.getString("Name","").toString());
                text2.setText("It's time for dinner");
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        userlocation.setText(pref.getString("Address","").toString());
        dh = new ExampleDBHelper(HomeActivity.this);
        offerrecycler = findViewById(R.id.offerrecycler);
        menurecycler = findViewById(R.id.menurecycler);
        editSearch = findViewById(R.id.editSearch);
        editSearch.setTypeface(font2);
        editSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(HomeActivity.this, HotelListActivity.class);
                startActivity(i);
            }
        });
        new AsyncRestaurants().execute(pref.getString("Latitude","").toString(), pref.getString("Longitiude", "").toString(), radiusKm);
        new AsyncTags().execute();
    }

    private class AsyncTags extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);
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
                url = new URL(API_PATH+"restaurant-tags");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
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
            List<Tags> data = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true")) {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        Tags catData = new Tags();
                        catData.id = json_data.getString("id");
                        catData.name = json_data.getString("typeName");
                        if(json_data.has("iconImage")) {
                            catData.img = json_data.getString("iconImage");
                        }
                        else
                        {
                            catData.img = " ";
                        }
                        data.add(catData);
                    }

                    adapter = new TagsAdapter(HomeActivity.this, data, adapter1, offerrecycler);
                    HorizontalLayout = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    menurecycler.setLayoutManager(HorizontalLayout);
                    menurecycler.setNestedScrollingEnabled(false);
                    menurecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncRestaurants extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);
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
                url = new URL(API_PATH+"all-restaurant");
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
            final List<Restaurants> data = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true")) {
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
                        if(json_data.has("iconImage"))
                        {
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
                        data.add(resData);
                    }
                    adapter1 = new RestaurantsAdapter(HomeActivity.this, data, offerrecycler);
                    offerrecycler.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    offerrecycler.setNestedScrollingEnabled(false);
                    offerrecycler.setFocusable(false);
                    offerrecycler.setAdapter(adapter1);

                    adapter1.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override public void onLoadMore() {
                            Log.e("haint", "Load More");
                            data.add(null);
                            adapter1.notifyItemInserted(data.size() - 1);
                            //Load more data for reyclerview
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("haint", "Load More 2");
                                    //Remove loading item
                                    data.remove(data.size() - 1);
                                    adapter1.notifyItemRemoved(data.size());
                                    //Load data
                                    int index = data.size();
                                    int end = index + 20;
                                    for (int i = index; i < end; i++)
                                    {
                                        Restaurants resData = new Restaurants();
                                        resData.setResname("Name " + i);
                                        resData.setResdescp("alibaba" + i + "@gmail.com");
                                        data.add(resData);
                                    }
                                    adapter1.notifyDataSetChanged();
                                    adapter1.setLoaded();
                                }
                            }, 5000);
                        }
                    });

                    adapter1.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
