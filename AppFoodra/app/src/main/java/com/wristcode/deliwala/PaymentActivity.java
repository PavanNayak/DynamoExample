package com.wristcode.deliwala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Payment;
import com.wristcode.deliwala.adapter.PaymentAdapter;
import com.wristcode.deliwala.extra.IConstants;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

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
 * Created by Ajay Jagadish on 01-Mar-18.
 */

public class PaymentActivity extends AppCompatActivity implements IConstants
{
    RecyclerView recyclerpayment;
    private List<Payment> paymentList;
    PaymentAdapter adapter;
    LinearLayoutManager HorizontalLayout;
    LinearLayout linearpromo;
    TextView txttotal, valuetotal, txtpaytype, txtpromo, txtapplypromo;
    EditText valuepromo;
    Button pay, applypromo;
    ExampleDBHelper dh;
    SharedPreferences pref1;
    List<String> arrayId;
    List<String> arrayVarId;
    List<String> arrayQty;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pref1 = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        txttotal = findViewById(R.id.txttotal);
        valuetotal = findViewById(R.id.valuetotal);
        txtpaytype = findViewById(R.id.txtpaytype);
        txtpromo = findViewById(R.id.txtpromo);
        linearpromo = findViewById(R.id.linearpromo);
        valuepromo = findViewById(R.id.valuepromo);
        applypromo = findViewById(R.id.applypromo);
        txtapplypromo = findViewById(R.id.txtapplypromo);
        pay = findViewById(R.id.pay);

        Typeface font1 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");

        txttotal.setTypeface(font1);
        valuetotal.setTypeface(font2);
        txtpaytype.setTypeface(font1);
        txtpromo.setTypeface(font1);
        valuepromo.setTypeface(font2);
        applypromo.setTypeface(font2);
        txtapplypromo.setTypeface(font2);
        pay.setTypeface(font2);
        dh = new ExampleDBHelper(getApplicationContext());
        arrayId = new ArrayList<>();
        arrayVarId = new ArrayList<>();
        arrayQty = new ArrayList<>();

        Cursor c = dh.getAllItems();
        while (c.moveToNext())
        {
            arrayQty.add(c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY)));
            arrayVarId.add(c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID)));
            arrayId.add(c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ID)));
        }

        valuetotal.setText("₹ "+dh.gettotalprice());
        pay.setText("PAY ₹ "+dh.gettotalprice());
        recyclerpayment = findViewById(R.id.recyclerpayment);
        paymentList = new ArrayList<>();
        prepareAlbums();

        applypromo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(valuepromo.getText().toString().equals(""))
                {
                    Toast.makeText(PaymentActivity.this, "Please enter a valid promocode!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new AsyncPromocode().execute(pref1.getString("Id", "").toString(), pref1.getString("id","").toString(), pref1.getString("AddressId", "").toString(), "Delivery", pref1.getString("PaymentType", "").toString(), valuepromo.getText().toString());

                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AsyncOrderDetails().execute(pref1.getString("Id", "").toString(), pref1.getString("id","").toString(), pref1.getString("AddressId", "").toString(), "Delivery", pref1.getString("PaymentType", "").toString(), valuepromo.getText().toString());
            }
        });
    }

    private class AsyncOrderDetails extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(PaymentActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tPlease Wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL(API_PATH+"order/new-order");
            }
            catch (MalformedURLException e)
            {
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
                        .appendQueryParameter("apiKey", params[0])
                        .appendQueryParameter("restId", params[1])
                        .appendQueryParameter("addressId", params[2])
                        .appendQueryParameter("orderType", params[3])
                        .appendQueryParameter("paymentType", params[4])
                        .appendQueryParameter("promocode", params[5]);
                for(int i=0;i<arrayId.size();i++) {
                    builder = builder.appendQueryParameter("items[" + i + "][productId]", arrayId.get(i).toString())
                      .appendQueryParameter("items[" + i + "][priceVariation]", arrayVarId.get(i).toString())
                      .appendQueryParameter("items[" + i + "][quantity]", arrayQty.get(i).toString());
                }
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
                    Intent i = new Intent(PaymentActivity.this, OrderConfirmActivity.class);
                    startActivity(i);
                    finish();
                }

            }
            catch (JSONException e)
            {
                Toast.makeText(PaymentActivity.this, "OOPS! Something went wrong. Retry", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncPromocode extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(PaymentActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tPlease Wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL(API_PATH+"order/new-order/promo-test");
            }
            catch (MalformedURLException e)
            {
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
                        .appendQueryParameter("apiKey", params[0])
                        .appendQueryParameter("restId", params[1])
                        .appendQueryParameter("addressId", params[2])
                        .appendQueryParameter("orderType", params[3])
                        .appendQueryParameter("paymentType", params[4])
                        .appendQueryParameter("promocode", params[5]);
                for(int i=0;i<arrayId.size();i++) {
                    builder = builder.appendQueryParameter("items[" + i + "][productId]", arrayId.get(i).toString())
                            .appendQueryParameter("items[" + i + "][priceVariation]", arrayVarId.get(i).toString())
                            .appendQueryParameter("items[" + i + "][quantity]", arrayQty.get(i).toString());
                }
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
                if (jsonObject.getString("promoError").equals("false"))
                {
                    linearpromo.setVisibility(View.GONE);
                    txtapplypromo.setVisibility(View.VISIBLE);
                    txtapplypromo.setText(valuepromo.getText().toString()+ " has been applied.");
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("PromoAmount", jsonObject.getString("promoAmount").toString());
                    editor1.apply();

                    pay.setText("PAY ₹ "+(dh.gettotalprice() - Integer.valueOf(pref1.getString("PromoAmount", "").toString())));
                }

            }
            catch (JSONException e)
            {
                Toast.makeText(PaymentActivity.this, "OOPS! Something went wrong. Retry", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void prepareAlbums()
    {
        int[] covers = new int[]
                {
                        R.drawable.cash,
                        R.drawable.card,
                };

        Payment a = new Payment("CASH ON DELIVERY", covers[0]);
        paymentList.add(a);
        a = new Payment("CARD", covers[1]);
        paymentList.add(a);

        adapter = new PaymentAdapter(PaymentActivity.this, paymentList);
        HorizontalLayout = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerpayment.setLayoutManager(HorizontalLayout);
        recyclerpayment.setNestedScrollingEnabled(false);
        recyclerpayment.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void backButton(View v) {
        Intent i = new Intent(PaymentActivity.this, AddAddressActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PaymentActivity.this, AddAddressActivity.class);
        startActivity(i);
        finish();
    }
}
