package com.wristcode.deliwala;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wristcode.deliwala.NavDrawer;
import com.wristcode.deliwala.extra.AndroidPermissions;
import com.wristcode.deliwala.extra.GPSTracker;
import com.wristcode.deliwala.R;

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

public class SelectLocationActivity extends AppCompatActivity implements GPSTracker.UpdateLocationListener, OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Bundle mBundle;
    private GoogleMap mGoogleMap;
    private GPSTracker gpsTracker;
    private Marker locationMarker;
    private MapView mMapView;
    private TextView mAddress, txtmanually;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    EditText housenumber, landmark, houseno, streetname;
    Spinner spinner;
    private LayoutInflater inflater;
    String mobilenum;
    public static final String mypreference = "mypref";
    private AlertDialog progressDialog;
    SharedPreferences pref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_selectlocation);
        init();

        inflater = LayoutInflater.from(SelectLocationActivity.this);
        txtmanually = (TextView) findViewById(R.id.txtmanually);
        txtmanually.setOnClickListener(this);
        gpsTracker = new GPSTracker(SelectLocationActivity.this);
        gpsTracker.setLocationListener(this);

        try {
            MapsInitializer.initialize(SelectLocationActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.onCreate(mBundle);
        mMapView.getMapAsync(this);

        housenumber = (EditText) findViewById(R.id.housenumber);
        landmark = (EditText) findViewById(R.id.landmark);
        pref1 = PreferenceManager.getDefaultSharedPreferences(SelectLocationActivity.this);
        housenumber.setText(pref1.getString("HouseNumber", "").toString());
        landmark.setText(pref1.getString("Landmark", "").toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (gpsTracker.checkLocationState()) {
            gpsTracker.startLocationUpdates();
            setMap(googleMap);
            LatLng latLang = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            if (gpsTracker.getLatitude() == 0) {
                latLang = new LatLng(13.3409, 74.7421);
                gpsTracker.setDefaultAddress("Udupi, Karnataka");
            }

            if (mGoogleMap != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 17);
                mGoogleMap.animateCamera(cameraUpdate);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);
                updatePickUPMarker(latLang, icon);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void init() {
        mMapView = (MapView) findViewById(R.id.map);
        mAddress = (TextView) findViewById(R.id.address);
    }

    private void updateLocation() {
        if (gpsTracker != null) {
            gpsTracker.onResume();
            if (Build.VERSION.SDK_INT >= 23) {
                if (!AndroidPermissions.getInstance().checkLocationPermission(SelectLocationActivity.this)) {
                    AndroidPermissions.getInstance().displayLocationPermissionAlert(SelectLocationActivity.this);
                }
            }
            gpsTracker.startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LatLng latLng;
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            updateMapWithLocationFirstTime(latLng);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        gpsTracker.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
        updateLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        gpsTracker.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        gpsTracker.onStop();
    }

    private void setMap(GoogleMap mGoogleMap) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    private void updateMapWithLocationFirstTime(LatLng latLang) {
        if (gpsTracker.getLatitude() != 0) {
            if (mGoogleMap != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 17);
                mGoogleMap.animateCamera(cameraUpdate);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);
                updatePickUPMarker(latLang, icon);
            }
        }
    }

    private void updatePickUPMarker(LatLng latLng, BitmapDescriptor icon) {
        if (locationMarker != null) {
            locationMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("")
                .icon(icon)
                .draggable(true);
        locationMarker = mGoogleMap.addMarker(markerOptions);
        mAddress.setText(getCompleteAddressString(latLng.latitude, latLng.longitude, SelectLocationActivity.this));
        locationMarker.setDraggable(false);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strAdd = addresses.get(0).getAddressLine(0).toString().trim();
                Log.e("My Current address", "" + strReturnedAddress.toString());
            } else {
                Log.e("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current  address", "Cannot get Address!");
        }
        return strAdd;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AndroidPermissions.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation();
                } else {
                    AndroidPermissions.getInstance().displayAlert(SelectLocationActivity.this, AndroidPermissions.REQUEST_LOCATION);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onSave(View v) {
        if (housenumber.getText().toString().matches("") || landmark.getText().toString().matches("")) {
            Toast.makeText(this, "House Number and Landmark are Mandatory!!!", Toast.LENGTH_SHORT).show();
        } else {
            String loc = mAddress.getText().toString();
            loc = loc.replaceAll(" ", "_");
            String fulladdress = housenumber.getText().toString().trim() + ",_" + landmark.getText().toString().trim() + ",_" + loc.trim();
            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(SelectLocationActivity.this);
            SharedPreferences.Editor editor1 = preferences1.edit();

            editor1.putString("address", fulladdress);
            editor1.apply();

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(SelectLocationActivity.this);
            SharedPreferences.Editor editor2 = preferences2.edit();
            editor2.putString("HouseNumber", housenumber.getText().toString().trim());
            editor2.putString("Landmark", landmark.getText().toString().trim());
            editor2.apply();

            new AsyncGetMenu().execute(pref1.getString("name", "").toString(), pref1.getString("phno", "").toString(), pref1.getString("email", "").toString(), pref1.getString("address", "").toString());
        }
    }

    public void onSave1(View v) {
        if (houseno.getText().toString().matches("") || streetname.getText().toString().matches("")) {
            Toast.makeText(this, "House Number and Street Name are Mandatory!!!", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(SelectLocationActivity.this);
            SharedPreferences.Editor editor1 = preferences1.edit();
            editor1.putString("address", houseno.getText().toString().trim() + ",_" + streetname.getText().toString().trim() + ",_" + spinner.getSelectedItem().toString().trim());
            editor1.apply();

            new AsyncGetMenu().execute(pref1.getString("name", "").toString(), pref1.getString("phno", "").toString(), pref1.getString("email", "").toString(), pref1.getString("address", "").toString());
        }
    }

    private class AsyncGetMenu extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SelectLocationActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tRegistering...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://mexicanaburrito.com/mexican/api/v1/register_user.php");
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
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("mobile", params[1])
                        .appendQueryParameter("email", params[2])
                        .appendQueryParameter("address", params[3]);
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
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    if (json_data.getString("status").toString().equals("1")) {
                        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(SelectLocationActivity.this);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putString("id", json_data.getString("id").toString());
                        editor1.apply();

                        Intent i1 = new Intent(SelectLocationActivity.this, NavDrawer.class);
                        startActivity(i1);
                        finish();
                    } else if (json_data.getInt("status") == 0) {
                        Toast.makeText(SelectLocationActivity.this, json_data.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SelectLocationActivity.this, json_data.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(SelectLocationActivity.this, "OOPS! Something went wrong. Retry", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        final View view = inflater.inflate(R.layout.activity_location_spinner, null);
        houseno = view.findViewById(R.id.houseno);
        streetname = view.findViewById(R.id.streetname);
        final BottomSheetDialog dialog = new BottomSheetDialog(SelectLocationActivity.this);

        dialog.setContentView(view);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SelectLocationActivity.this.getWindow().setStatusBarColor(SelectLocationActivity.this.getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        dialog.show();
        spinner = view.findViewById(R.id.spinner1);
        List<String> categories = new ArrayList<>();
        categories.add("Udupi");
        categories.add("Manipal");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinner.getSelectedItem().toString().equals("Udupi")) {
            List<String> categories = new ArrayList<>();
            categories.add("Adi Udupi");
            categories.add("Ananthakrishna Nagar");
            categories.add("Bailoor");
            categories.add("Bhima Nagar");
            categories.add("Bolje");
            categories.add("Brahmagiri");
            categories.add("Chitpady");
            categories.add("CPC Layout");
            categories.add("Doddanagudde");
            categories.add("Hanumantha Nagar");
            categories.add("Hayagriva Nagar");
            categories.add("Indira Nagar");
            categories.add("Indrali");
            categories.add("Kadekar");
            categories.add("Kadekoppala");
            categories.add("Kadiyali");
            categories.add("Karamballi");
            categories.add("Kasturba Nagar");
            categories.add("Kidiyoor");
            categories.add("Kinnimulki");
            categories.add("Kodakoor");
            categories.add("Korangrapady");
            categories.add("Kukkikatte");
            categories.add("Kunjibettu");
            categories.add("Kuthpady");
            categories.add("Malpe");
            categories.add("Marpalli");
            categories.add("Nittur");
            categories.add("Pandubettu");
            categories.add("Rudrapriya Nagar");
            categories.add("Thenkapet");
            categories.add("Udyavara");
            categories.add("VP Nagar");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else {
            List<String> categories = new ArrayList<>();
            categories.add("Adarsha Nagar");
            categories.add("ALN Layout");
            categories.add("Anantha Kalyani Nagar");
            categories.add("Indrali");
            categories.add("Dasharath Nagar");
            categories.add("End Point Road");
            categories.add("Eshwar Nagar");
            categories.add("Industrial Area");
            categories.add("Lakshmindra Nagar");
            categories.add("Manipal-Udupi Road");
            categories.add("MIT Hostel");
            categories.add("MIT KC");
            categories.add("MIT Venugopal Temple");
            categories.add("MIT Main Gate");
            categories.add("Perampalli");
            categories.add("Sapthagiri Nagar");
            categories.add("TAPMI");
            categories.add("Tiger Circle");
            categories.add("VP Nagar");
            categories.add("Vidyaratna Nagar");
            categories.add("Saralebettu");
            categories.add("Syndicate Circle");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}