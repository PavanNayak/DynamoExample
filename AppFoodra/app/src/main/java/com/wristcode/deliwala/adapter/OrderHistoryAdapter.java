package com.wristcode.deliwala.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.OrderHistoryActivity;
import com.wristcode.deliwala.Pojo.OrderHistoryItems;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.Pojo.OrderHistory;
import com.wristcode.deliwala.TrackActivity;

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
import java.util.HashMap;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    ArrayList<HashMap<String, String>> contactList;
    OrderHistoryActivity subCatList;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<OrderHistory> moviesList;
    private List<OrderHistoryItems> moviesList1;
    private Context mContext;
    OrderHistoryItemAdapter mAdapter1;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtorderid, txtorderdate, txtresname, txtorderitems, txtpaytype, valpaytype, txtgrandtotal, valgrandtotal, txtstatus;
        ImageView imgres;
        RecyclerView recyclerView1;
        Button btnTrack;

        public MyViewHolder(View view)
        {
            super(view);
            contactList = new ArrayList<>();
            subCatList = new OrderHistoryActivity();
            txtorderid = (TextView) view.findViewById(R.id.txtorderid);
            txtorderdate = (TextView) view.findViewById(R.id.txtorderdate);
            txtresname = (TextView) view.findViewById(R.id.txtresname);
            txtorderitems = (TextView) view.findViewById(R.id.txtorderitems);
            txtpaytype = (TextView) view.findViewById(R.id.txtpaytype);
            valpaytype = (TextView) view.findViewById(R.id.valpaytype);
            txtgrandtotal = (TextView) view.findViewById(R.id.txtgrandtotal);
            valgrandtotal = (TextView) view.findViewById(R.id.valgrandtotal);
            txtstatus = (TextView) view.findViewById(R.id.txtstatus);
            imgres = (ImageView) view.findViewById(R.id.imgres);
            btnTrack = (Button) view.findViewById(R.id.btnTrack);
            recyclerView1 = (RecyclerView) view.findViewById(R.id.itemsRecycler);
        }
    }

    public OrderHistoryAdapter(Context mContext, List<OrderHistory> moviesList)
    {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        OrderHistory movie = moviesList.get(position);
        holder.txtorderid.setText("Order ID: #" +movie.getoId());
        holder.txtorderdate.setText("Date: " + movie.getoDate());
        holder.txtresname.setText(movie.getoResName());
        holder.valpaytype.setText(movie.getoPayType());
        holder.valgrandtotal.setText("₹ "+ movie.getoTotal());
        holder.txtstatus.setText(movie.getoStatus());

        if (movie.getoStatus().toString().equals("received"))
        {
            holder.txtstatus.setText("Your order has been received!!!");
        }
        else if (movie.getoStatus().toString().equals("dispatched"))
        {
            holder.txtstatus.setText("Your order has been dispatched!!!");
        }

        Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/"+movie.getoResImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgres);

        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtorderid.setTypeface(font1);
        holder.txtorderdate.setTypeface(font1);
        holder.txtresname.setTypeface(font1);
        holder.txtorderitems.setTypeface(font1);
        holder.txtpaytype.setTypeface(font1);
        holder.valpaytype.setTypeface(font2);
        holder.txtgrandtotal.setTypeface(font1);
        holder.valgrandtotal.setTypeface(font2);
        holder.txtstatus.setTypeface(font1);
        holder.btnTrack.setTypeface(font2);

        JSONArray jArray1 = null;
        moviesList1 = new ArrayList<>();
        try
        {
            jArray1 = new JSONArray(movie.getoItems());
            for (int j = 0; j < jArray1.length(); j++)
            {
                JSONObject json_data1 = jArray1.getJSONObject(j);
                OrderHistoryItems fishData1 = new OrderHistoryItems();
                fishData1.ohItemname = json_data1.getString("itemName");
                fishData1.ohItemqty = json_data1.getString("quantity");
                fishData1.ohItemprice = json_data1.getString("actualAmount");
                moviesList1.add(fishData1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter1 = new OrderHistoryItemAdapter(mContext, moviesList1);
        holder.recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recyclerView1.setAdapter(mAdapter1);
        mAdapter1.notifyDataSetChanged();

        holder.btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(mContext, TrackActivity.class);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}