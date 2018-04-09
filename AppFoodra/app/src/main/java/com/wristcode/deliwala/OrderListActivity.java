package com.wristcode.deliwala;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.OrderHistory;
import com.wristcode.deliwala.Pojo.OrderHistoryItems;
import com.wristcode.deliwala.adapter.OrderHistoryAdapter;
import com.wristcode.deliwala.adapter.OrderHistoryItemAdapter;
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
 * Created by Ajay Jagadish on 12-Mar-18.
 */

public class OrderListActivity extends AppCompatActivity implements IConstants
{
    TextView txtorderid, txtorderdate, txtresname, txtorderitems, txtpaytype, valpaytype, txtgrandtotal, valgrandtotal, txtstatus, valstatus;
    RecyclerView itemsRecycler;
    Button btnTrack;
    private List<OrderHistoryItems> moviesList1;
    OrderHistoryItemAdapter mAdapter1;
    String rateItem;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_order_list);
        txtorderid = (TextView) findViewById(R.id.txtorderid);
        txtorderdate = (TextView) findViewById(R.id.txtorderdate);
        txtresname = (TextView) findViewById(R.id.txtresname);
        txtorderitems = (TextView) findViewById(R.id.txtorderitems);
        txtpaytype = (TextView) findViewById(R.id.txtpaytype);
        valpaytype = (TextView) findViewById(R.id.valpaytype);
        txtgrandtotal = (TextView) findViewById(R.id.txtgrandtotal);
        valgrandtotal = (TextView) findViewById(R.id.valgrandtotal);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        valstatus = (TextView) findViewById(R.id.valstatus);
        itemsRecycler = (RecyclerView) findViewById(R.id.itemsRecycler);
        btnTrack = (Button) findViewById(R.id.btnTrack);
        preferences = PreferenceManager.getDefaultSharedPreferences(OrderListActivity.this);

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");

        txtorderid.setTypeface(font1);
        txtorderdate.setTypeface(font1);
        txtresname.setTypeface(font);
        txtpaytype.setTypeface(font1);
        valpaytype.setTypeface(font2);
        txtgrandtotal.setTypeface(font1);
        valgrandtotal.setTypeface(font2);
        txtstatus.setTypeface(font1);
        valstatus.setTypeface(font2);
        btnTrack.setTypeface(font2);

        txtorderid.setText(getIntent().getStringExtra("orderid").toString());
        txtorderdate.setText(getIntent().getStringExtra("date").toString());
        txtresname.setText(getIntent().getStringExtra("resname").toString());
        valpaytype.setText(getIntent().getStringExtra("paymenttype").toString());
        valgrandtotal.setText(getIntent().getStringExtra("grandtotal").toString());
        valstatus.setText(getIntent().getStringExtra("status").toString());

        JSONArray jArray1 = null;
        moviesList1 = new ArrayList<>();
        try
        {
            jArray1 = new JSONArray(getIntent().getStringExtra("items").toString());
            for (int j = 0; j < jArray1.length(); j++)
            {
                JSONObject json_data1 = jArray1.getJSONObject(j);
                OrderHistoryItems fishData1 = new OrderHistoryItems();
                fishData1.ohItemname = json_data1.getString("itemName");
                fishData1.ohItemqty = json_data1.getString("quantity");
                fishData1.ohItemprice = json_data1.getString("actualAmount");
                if(json_data1.has("priceVariavtion"))
                {
                    fishData1.ohItemvariation = json_data1.getString("priceVariavtion");
                }
                else
                {
                    fishData1.ohItemvariation = "";
                }
                moviesList1.add(fishData1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter1 = new OrderHistoryItemAdapter(OrderListActivity.this, moviesList1);
        itemsRecycler.setLayoutManager(new LinearLayoutManager(OrderListActivity.this));
        itemsRecycler.setAdapter(mAdapter1);
        mAdapter1.notifyDataSetChanged();

        if(valstatus.getText().toString().equals("Delivered"))
        {
            LayoutInflater inflater = LayoutInflater.from(OrderListActivity.this);
            View alertLayout = inflater.inflate(R.layout.rating_dialog, null);
            final RatingBar ratingBarItem = (RatingBar) alertLayout.findViewById(R.id.ratingBarItem);
            final EditText txtreview = (EditText) alertLayout.findViewById(R.id.txtreview);
            final Button btnSubmit = (Button) alertLayout.findViewById(R.id.btnSubmit);

            AlertDialog.Builder alert = new AlertDialog.Builder(OrderListActivity.this);
            alert.setTitle("Add Review");
            alert.setView(alertLayout);
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();

            ratingBarItem.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
            {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
                {
                    rateItem = String.valueOf(rating);
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    new AsyncReviews().execute(getIntent().getStringExtra("resid").toString(), preferences.getString("Id", "").toString(), rateItem, txtreview.getText().toString());
                    dialog.dismiss();
                }
            });
        }

//        if (valstatus.getText().toString().equals("On The Way"))
//        {
//            btnTrack.setVisibility(View.VISIBLE);
//            btnTrack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v)
//                {
//                    Intent i = new Intent(OrderListActivity.this, TrackActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            });
//        }
    }

    private class AsyncReviews extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OrderListActivity.this);
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/restaurant/ratings");
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
                        .appendQueryParameter("restId", params[0])
                        .appendQueryParameter("apiKey", params[1])
                        .appendQueryParameter("ratings", params[2])
                        .appendQueryParameter("reviews", params[3]);
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
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    //Toast.makeText(OrderListActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Toast.makeText(OrderListActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backButton(View v) {
        Intent i = new Intent(OrderListActivity.this, OrderHistoryActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OrderListActivity.this, OrderHistoryActivity.class);
        startActivity(i);
        finish();
    }
}
