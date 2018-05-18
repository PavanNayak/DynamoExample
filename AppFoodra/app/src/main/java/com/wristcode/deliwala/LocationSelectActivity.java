package com.wristcode.deliwala;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wristcode.deliwala.extra.PlaceDetailsJSONParser;
import com.wristcode.deliwala.extra.PlaceJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocationSelectActivity extends AppCompatActivity implements TextWatcher, AdapterView.OnItemClickListener
{
    String items;
    ListView recyclerlocation;
    DownloadTask placesDownloadTask;
    DownloadTask placeDetailsDownloadTask;
    ParserTask placesParserTask;
    ParserTask placeDetailsParserTask;
    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    AutoCompleteTextView editLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);
        recyclerlocation = findViewById(R.id.recyclerlocation);
        editLocation = findViewById(R.id.editLocation);
        editLocation.addTextChangedListener(this);
        editLocation.setOnItemClickListener(this);
        recyclerlocation.setOnItemClickListener(this);
    }

    public void goBack(View v) {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        intent.putExtra("LAT", "");
        intent.putExtra("LONGI", "");
        setResult(2, intent);
        finish();
    }

    public void currentLocation(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        intent.putExtra("LAT", "1");
        intent.putExtra("LONGI", "1");
        setResult(2, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        placesDownloadTask = new DownloadTask(PLACES);

        // Getting url to the Google Places Autocomplete api
        String url = getAutoCompleteUrl(charSequence.toString());

        // Start downloading Google Places
        // This causes to execute doInBackground() of DownloadTask class
        placesDownloadTask.execute(url);
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        if (view.equals(R.id.recyclerlocation))
        {
            ListView lv = (ListView) adapterView;
            SimpleAdapter adapter = (SimpleAdapter) adapterView.getAdapter();

            HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(i);

            // item= String.valueOf(adapter.getItem(i));

            // Creating a DownloadTask to download Places details of the selected place
            placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

            // Getting url to the Google Places details api
            String url = getPlaceDetailsUrl(hm.get("reference"));


            // Start downloading Google Place Details
            // This causes to execute doInBackground() of DownloadTask class
            placeDetailsDownloadTask.execute(url);
            //  new MainActivity().setAddress1("asdnas");
        } else {
            ListView lv = (ListView) adapterView;
            SimpleAdapter adapter = (SimpleAdapter) adapterView.getAdapter();
            //item= String.valueOf(adapter.getItem(i));
            HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(i);

            // Creating a DownloadTask to download Places details of the selected place
            placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

            // Getting url to the Google Places details api
            String url = getPlaceDetailsUrl(hm.get("reference"));

            // Start downloading Google Place Details
            // This causes to execute doInBackground() of DownloadTask class
            placeDetailsDownloadTask.execute(url);
            // new MainActivity().setAddress1("asdnas");
        }
    }


    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyCybZnEJwr_Rf92Pjr8gnwsmB3KPeI5CbM";

        // place to be be searched
        String input = "input=" + place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
    }

    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyCybZnEJwr_Rf92Pjr8gnwsmB3KPeI5CbM";

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};


                    int[] to = new int[]{android.R.id.text1};

                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                    // Setting the adapter
                    //editLocation.setAdapter(adapter);
                    recyclerlocation.setAdapter(adapter);
                    break;
                case PLACES_DETAILS:

                    String[] from1 = new String[]{"description"};


                    int[] to1 = new int[]{android.R.id.text1};
                    //   int[] to1 = new int[] { android.R.id.text1 };
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));
                    // Toast.makeText(LocationSelectActivity.this,String.valueOf(latitude), Toast.LENGTH_SHORT).show();
                    // Toast.makeText(LocationSelectActivity.this,String.valueOf(longitude), Toast.LENGTH_SHORT).show();


//                    if (getIntent().getStringExtra("flag").equals("activity")) {
                        //Toast.makeText(LocationSelectActivity.this,to1.toString(), Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putString("Latitude", String.valueOf(latitude));
                        editor1.putString("Longitiude", String.valueOf(longitude));
                        editor1.apply();
//                    }


                    // activity.setAddress("asfbsjkbf");


                    String lol = getAddressFromLatLng(latitude, longitude);

                    Intent intent = new Intent(LocationSelectActivity.this, AddressActivity.class);
                    intent.putExtra("MESSAGE", lol);
                    intent.putExtra("LAT", String.valueOf(latitude));
                    intent.putExtra("LONGI", String.valueOf(longitude));
                    setResult(2, intent);
                    finish();
                    break;
            }
        }
    }

    public String getAddressFromLatLng(double lat, double lng) {
        Geocoder geocoder = new Geocoder(LocationSelectActivity.this, Locale.getDefault());
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

            items = obj.getLocality() + " " + obj.getAdminArea() + " " + obj.getCountryName();
            Log.v("IGA", "Address" + add);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return items;
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        intent.putExtra("LAT", "");
        intent.putExtra("LONGI", "");
        setResult(2, intent);
        finish();

    }
}
