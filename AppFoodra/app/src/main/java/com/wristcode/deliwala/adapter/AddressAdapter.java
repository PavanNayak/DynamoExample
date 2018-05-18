package com.wristcode.deliwala.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.AddAddressActivity;
import com.wristcode.deliwala.AddressActivity;
import com.wristcode.deliwala.Pojo.Address;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.extra.IConstants;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ajay Jagadish on 10-Mar-18.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> implements IConstants
{
    ArrayList<HashMap<String, String>> contactList;
    AddAddressActivity addList;
    private List<Address> moviesList;
    private Context mContext;
    private int lastSelectedPosition = -1;
    SharedPreferences preference;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtid, txtname, txttype, txtaddress, txtlat, txtlong, txtedit, txtdelete;
        RadioButton raddress;
        ImageView imgadd;

        public MyViewHolder(View view)
        {
            super(view);
            contactList = new ArrayList<>();
            addList = new AddAddressActivity();
            txtid = (TextView) view.findViewById(R.id.txtid);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txttype = (TextView) view.findViewById(R.id.txttype);
            txtaddress = (TextView) view.findViewById(R.id.txtaddress);
            txtlat = (TextView) view.findViewById(R.id.txtlat);
            txtlong = (TextView) view.findViewById(R.id.txtlong);
            txtedit = (TextView) view.findViewById(R.id.txtedit);
            txtdelete = (TextView) view.findViewById(R.id.txtdelete);
            raddress = (RadioButton) view.findViewById(R.id.raddress);
            imgadd = (ImageView) view.findViewById(R.id.imgadd);
            preference = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

            raddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    SharedPreferences pref1 = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = pref1.edit();
                    editor2.putString("AddressId", txtid.getText().toString());
                    editor2.putString("Address", txtaddress.getText().toString());
                    editor2.putString("Latitude", txtlat.getText().toString());
                    editor2.putString("Longitiude", txtlong.getText().toString());
                    editor2.apply();
                }
            });

            txtedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    SharedPreferences pref2 = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = pref2.edit();
                    editor1.putString("AddressFlag", "1");
                    editor1.putString("AddressType", txttype.getText().toString());
                    editor1.putString("Address", txtaddress.getText().toString());
                    editor1.putString("AddressId", txtid.getText().toString());
                    editor1.apply();

                    Intent i = new Intent(mContext, AddressActivity.class);
                    i.putExtra("MESSAGE", "");
                    i.putExtra("FLAG", "1");
                    mContext.startActivity(i);
                }
            });

            txtdelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    new AsyncRemoveAddress().execute(preference.getString("Id","").toString(), txtid.getText().toString());
                }
            });
        }
    }

    public AddressAdapter(Context mContext, List<Address> moviesList)
    {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_address_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final Address movie = moviesList.get(position);
        holder.txtid.setText(movie.getUserid());
        holder.txtname.setText(movie.getUsername());
        holder.txttype.setText(movie.getUseraddtype());
        holder.txtaddress.setText(movie.getUseraddress());
        holder.txtlat.setText(movie.getUserlat());
        holder.txtlong.setText(movie.getUserlong());

        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font1);
        holder.txtaddress.setTypeface(font2);
        holder.txtedit.setTypeface(font2);
        holder.txtdelete.setTypeface(font2);

        if(holder.txttype.getText().toString().equals("Home"))
        {
            holder.imgadd.setImageResource(R.drawable.home);
        }
        else if(holder.txttype.getText().toString().equals("Work"))
        {
            holder.imgadd.setImageResource(R.drawable.work);
        }
        else
        {
            holder.imgadd.setImageResource(R.drawable.others);
        }

        holder.raddress.setChecked(lastSelectedPosition == position);

        SharedPreferences pref = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref.edit();
        editor1.putString("Position", String.valueOf(lastSelectedPosition));
        editor1.apply();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private class AsyncRemoveAddress extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(mContext);
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
                url = new URL(API_PATH+"customer/address/remove");
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
                        .appendQueryParameter("addressId", params[1]);
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
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    Intent i = new Intent(mContext, AddAddressActivity.class);
                    mContext.startActivity(i);
                }

            } catch (JSONException e) {
                Toast.makeText(mContext, "OOPS! Something went wrong. Retry", Toast.LENGTH_LONG).show();
            }
        }
    }

}
