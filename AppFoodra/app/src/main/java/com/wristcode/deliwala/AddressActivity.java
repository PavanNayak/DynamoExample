package com.wristcode.deliwala;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

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
import java.util.Locale;

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
    String lati, longi;
    private LocationManager mLocationManager;
    double latitude, longitude;

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

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (pref1.getString("AddressFlag", "").toString().equals("1"))
        {
            txtflatno.setText(pref1.getString("HouseNumber", "").toString());
            txtlandmark.setText(pref1.getString("Landmark", "").toString());
            txtname.setText(pref1.getString("Name", "").toString());
            txtmobileno.setText(pref1.getString("PhoneNo", "").toString());

            if(getIntent().getStringExtra("FLAG").toString().equals("1"))
            {
                txtaddress.setText(pref1.getString("Address", "").toString());
            }
            else
            {
                txtaddress.setText(getIntent().getStringExtra("MESSAGE").toString());
            }
        }

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

        txtchange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AddressActivity.this, LocationSelectActivity.class);
                i.putExtra("flag","activity");
                startActivityForResult(i, 2);
            }
        });
        //new AsyncAddress().execute(preferences1.getString("Id", "").toString());

        saveAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fulladdress = txtflatno.getText().toString()+", "+txtlandmark.getText().toString()+", "+txtaddress.getText().toString();

                SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(AddressActivity.this);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putString("Address", fulladdress.toString());
                editor1.putString("Latitude", String.valueOf(latitude));
                editor1.putString("Longitiude", String.valueOf(longitude));
                editor1.apply();

                new AsyncAddAddress().execute(pref1.getString("Id", "").toString(), pref1.getString("Name", "").toString(), pref1.getString("Address", "").toString(), pref1.getString("Longitiude", "").toString(), pref1.getString("Latitude", "").toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            if(data.getStringExtra("LAT").toString().equals("1"))
            {
                getLastKnownLocation();
            }
            else
            {
                String message = data.getStringExtra("MESSAGE");
                txtaddress.setText(message);
                lati = data.getStringExtra("LAT");
                longi = data.getStringExtra("LONGI");
            }
        }


//        switch (requestCode) {
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        // All required changes were successfully made
//                        getLocation();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        // The user was asked to change settings, but chose not to
//                        break;
//                    default:
//                        break;
//                }
//                break;
//        }
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers)
        {
            Location l = mLocationManager.getLastKnownLocation(provider);
            //ALog.d("last known location, provider: %s, location: %s", provider,
            //   l);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                //  ALog.d("found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }

        latitude = bestLocation.getLatitude();
        longitude = bestLocation.getLongitude();


        getAddressFromLatLng(latitude,longitude);


        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(AddressActivity.this);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putString("Latitude", String.valueOf(latitude));
        editor1.putString("Longitiude", String.valueOf(longitude));
        editor1.apply();


        //  Toast.makeText(this,String.valueOf(latitude), Toast.LENGTH_SHORT).show();
        // Toast.makeText(this,String.valueOf(longitude), Toast.LENGTH_SHORT).show();
//        getAddress();


        return bestLocation;
    }





    public void getAddressFromLatLng(double lat, double lng) {
        Geocoder geocoder = new Geocoder(AddressActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();


            Toast.makeText(this,obj.getLocality()+" "+obj.getAdminArea()+" "+obj.getCountryName(), Toast.LENGTH_SHORT).show();
            txtaddress.setText(obj.getLocality()+" "+obj.getAdminArea()+" "+obj.getCountryName());
            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncAddAddress extends AsyncTask<String, String, String> {
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/customer/address/add-new");
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
                        .appendQueryParameter("apiKey", params[0])
                        .appendQueryParameter("fullName", params[1])
                        .appendQueryParameter("address", params[2])
                        .appendQueryParameter("longitude", params[3])
                        .appendQueryParameter("lattitude", params[4]);
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
            Toast.makeText(AddressActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences(AddressActivity.this);
                    SharedPreferences.Editor editor1 = p1.edit();
                    editor1.putString("AddressId", jsonObject.getString("addressId").toString());
                    editor1.apply();

                    Intent i = new Intent(AddressActivity.this, AddAddressActivity.class);
                    startActivity(i);
                    finish();
                }

            } catch (JSONException e) {
                Toast.makeText(AddressActivity.this, "OOPS! Something went wrong. Retry", Toast.LENGTH_LONG).show();
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
