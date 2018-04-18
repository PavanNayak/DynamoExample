package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Category;
import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.adapter.CategoryAdapter;
import com.wristcode.deliwala.adapter.RestaurantsAdapter;
import com.wristcode.deliwala.extra.IConstants;
import com.wristcode.deliwala.fragments.HomeFragment;

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
 * Created by Ajay Jagadish on 02-Apr-18.
 */

public class TagRestaurantsActivity extends AppCompatActivity implements IConstants
{
    RecyclerView tagrecycler, resrecycler;
    TextView txtrestaurant;
    CategoryAdapter adapter;
    RestaurantsAdapter adapter1;
    LinearLayoutManager HorizontalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tag_restaurants);
        txtrestaurant = (TextView) findViewById(R.id.txtrestaurant);
        tagrecycler = (RecyclerView) findViewById(R.id.tagrecycler);
        resrecycler = (RecyclerView) findViewById(R.id.resrecycler);
        new AsyncTags().execute();
        new AsyncMenu().execute(getIntent().getStringExtra("catid").toString());
    }

    private class AsyncTags extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(TagRestaurantsActivity.this);
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/restaurant-tags");
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
            List<Category> data = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true")) {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        Category catData = new Category();
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

                    adapter = new CategoryAdapter(TagRestaurantsActivity.this, data, adapter1, resrecycler);
                    HorizontalLayout = new LinearLayoutManager(TagRestaurantsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    tagrecycler.setLayoutManager(HorizontalLayout);
                    tagrecycler.setNestedScrollingEnabled(false);
                    tagrecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(TagRestaurantsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncMenu extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/restaurant-with-tags");
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
                        .appendQueryParameter("tagId", params[0]);
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
        protected void onPostExecute(String result)
        {
            //pdLoading.dismiss();
            List<Restaurants> data = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject json_data = jArray.getJSONObject(i);
                        JSONArray jsonArray = json_data.getJSONArray("restaurant");
                        if(jsonArray.toString().equals("[]"))
                        {
                            Toast.makeText(TagRestaurantsActivity.this, "No Restaurants Found!!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject json_data1 = jsonArray.getJSONObject(j);
                                Restaurants resData = new Restaurants();
                                resData.resid = json_data1.getString("id");
                                resData.resname = json_data1.getString("restaurantName");
                                if (json_data1.has("description")) {
                                    resData.resdescp = json_data1.getString("description");
                                } else {
                                    resData.resdescp = "No Description";
                                }
                                if (json_data1.has("restaurantAddress")) {
                                    resData.resadd = json_data1.getString("restaurantAddress");
                                } else {
                                    resData.resadd = "No Address";
                                }
                                resData.reslat = json_data1.getString("restaurantLat");
                                resData.reslong = json_data1.getString("restaurantLong");
                                resData.resmob = json_data1.getString("primaryMobile");
                                resData.resopentime = json_data1.getString("openTime");
                                resData.resclosetime = json_data1.getString("closeTime");
                                resData.resisopen = json_data1.getString("isOpen");
                                resData.respop = json_data1.getString("popularity");
                                if (json_data1.has("iconImage")) {
                                    resData.resimg = json_data1.getString("iconImage");
                                } else {
                                    resData.resimg = " ";
                                }
                                JSONArray jsonArray1 = json_data1.getJSONArray("restaurantType");
                                if(!(jsonArray1.length() == 0))
                                {
                                    for(int k=0; k<jsonArray1.length(); k++)
                                    {
                                        JSONObject jobject2 = jsonArray1.getJSONObject(k);
                                        resData.restags.add(jobject2.getString("typeName"));
                                    }
                                }
                                data.add(resData);
                            }
                        }
                    }
                    adapter1 = new RestaurantsAdapter(TagRestaurantsActivity.this, data, resrecycler);
                    resrecycler.setLayoutManager(new LinearLayoutManager(TagRestaurantsActivity.this));
                    resrecycler.setNestedScrollingEnabled(true);
                    resrecycler.setFocusable(false);
                    resrecycler.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(TagRestaurantsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backButton(View v) {
        Intent i = new Intent(TagRestaurantsActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TagRestaurantsActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }
}
