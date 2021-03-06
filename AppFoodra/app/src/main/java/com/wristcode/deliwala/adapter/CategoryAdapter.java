package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.Pojo.Category;
import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.R;
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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements IConstants
{
    private List<Category> moviesList;
    private Context mContext;
    private int lastSelectedPosition = -1;
    RestaurantsAdapter adapter1;
    RecyclerView offerrecycler;
    SharedPreferences pref;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catid, catname;
        RelativeLayout relative;
        ImageView catimg;
        RadioButton radiobutton;

        public MyViewHolder(View view) {
            super(view);
            catid = view.findViewById(R.id.catid);
            catname = view.findViewById(R.id.catname);
            relative = view.findViewById(R.id.relative);
            catimg = view.findViewById(R.id.catimg);
            radiobutton = view.findViewById(R.id.radiobutton);
            pref = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            lastSelectedPosition = Integer.valueOf(pref.getString("Tagpos","").toString());

            radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = getAdapterPosition();
                    catimg.setColorFilter(Color.parseColor("#DC143C"));
                    catname.setTextColor(Color.parseColor("#DC143C"));
                    notifyDataSetChanged();

                    if(catid.getText().toString().equals("ALL"))
                    {
                        new AsyncRestaurants().execute(pref.getString("Latitude","").toString(), pref.getString("Longitiude", "").toString(), radiusKm);
                    }
                    else
                    {
                        new AsyncMenu().execute(pref.getString("Latitude","").toString(), pref.getString("Longitiude", "").toString(), radiusKm, catid.getText().toString());
                    }
                }
            });
        }
    }

    public CategoryAdapter(Context mContext, List<Category> moviesList, RestaurantsAdapter adapter1, RecyclerView offerrecycler) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.adapter1 = adapter1;
        this.offerrecycler = offerrecycler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category movie = moviesList.get(position);
        holder.catid.setText(movie.getId());
        holder.catname.setText(movie.getName());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.catname.setTypeface(font2);

        Glide.with(mContext).load("http://appfoodra.com/uploads/restauranttype/icons/"+movie.getImg())
                .placeholder(R.drawable.all)
                .error(R.drawable.all)
                .into(holder.catimg);

        holder.radiobutton.setChecked(lastSelectedPosition == position);

        int colorImg = (position == lastSelectedPosition)? Color.parseColor("#DC143C") : Color.parseColor("#C0C0C0");
        holder.catimg.setColorFilter(colorImg);
        int colorTitle = (position == lastSelectedPosition)? Color.parseColor("#DC143C") : Color.parseColor("#C0C0C0");
        holder.catname.setTextColor(colorTitle);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private class AsyncMenu extends AsyncTask<String, String, String>
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
                url = new URL(API_PATH+"restaurant-with-tags");
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
                        .appendQueryParameter("radiusKm", params[2])
                        .appendQueryParameter("tagId", params[3]);
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
            List<Restaurants> data = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject jData = jArray.getJSONObject(i);
                        JSONObject json_data = jData.getJSONObject("0");
                        JSONArray jsonArray = json_data.getJSONArray("restaurant");
                        if(jsonArray.toString().equals("[]"))
                        {
                            Toast.makeText(mContext, "No Restaurants Found!!!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                            for (int j = 0; j < jsonArray.length(); j++)
                            {
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
                                resData.resisopen = json_data1.getString("isOpen");
                                resData.resopentime = json_data1.getString("openTime");
                                resData.resclosetime = json_data1.getString("closeTime");
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
                                resData.resdist = jData.getString("distance");
                                data.add(resData);
                            }
                        }
                    }
                    adapter1 = new RestaurantsAdapter(mContext, data, offerrecycler);
                    offerrecycler.setLayoutManager(new LinearLayoutManager(mContext));
                    offerrecycler.setNestedScrollingEnabled(false);
                    offerrecycler.setFocusable(false);
                    offerrecycler.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncRestaurants extends AsyncTask<String, String, String>
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
                        resData.resdist = jData.getString("distance");
                        data.add(resData);
                    }
                    adapter1 = new RestaurantsAdapter(mContext, data, offerrecycler);
                    offerrecycler.setLayoutManager(new LinearLayoutManager(mContext));
                    offerrecycler.setNestedScrollingEnabled(false);
                    offerrecycler.setFocusable(false);
                    offerrecycler.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}