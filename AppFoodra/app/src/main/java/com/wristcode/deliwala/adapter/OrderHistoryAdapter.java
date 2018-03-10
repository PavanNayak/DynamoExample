package com.wristcode.deliwala.adapter;

import android.app.ProgressDialog;
import android.content.Context;
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
        public TextView txtorderid, txtorderdate, txtresname, txtpaytype, valpaytype, txtgrandtotal, valgrandtotal, txtstatus;
        ImageView imgres;
        RecyclerView recyclerView1;

        public MyViewHolder(View view)
        {
            super(view);
            contactList = new ArrayList<>();
            subCatList = new OrderHistoryActivity();
            txtorderid = (TextView) view.findViewById(R.id.txtorderid);
            txtorderdate = (TextView) view.findViewById(R.id.txtorderdate);
            txtresname = (TextView) view.findViewById(R.id.txtresname);
            txtpaytype = (TextView) view.findViewById(R.id.txtpaytype);
            valpaytype = (TextView) view.findViewById(R.id.valpaytype);
            txtgrandtotal = (TextView) view.findViewById(R.id.txtgrandtotal);
            valgrandtotal = (TextView) view.findViewById(R.id.valgrandtotal);
            txtstatus = (TextView) view.findViewById(R.id.txtstatus);
            imgres = (ImageView) view.findViewById(R.id.imgres);
            recyclerView1 = (RecyclerView) view.findViewById(R.id.itemsRecycler);
        }
    }

    public OrderHistoryAdapter(Context mContext, List<OrderHistory> moviesList, List<OrderHistoryItems> moviesList1)
    {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.moviesList1 = moviesList1;
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
        Toast.makeText(mContext, movie.getoResName(), Toast.LENGTH_SHORT).show();
        holder.txtorderid.setText("Order ID: #" +movie.getoId());
        holder.txtorderdate.setText("Date: " + movie.getoDate());
        holder.txtresname.setText(movie.getoResName());
        holder.valpaytype.setText(movie.getoPayType());
        holder.valgrandtotal.setText("₹ "+ movie.getoTotal());
        holder.txtstatus.setText(movie.getoStatus());

        Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/"+movie.getoResImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgres);

        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtorderid.setTypeface(font1);
        holder.txtorderdate.setTypeface(font1);
        holder.txtresname.setTypeface(font1);
        holder.txtpaytype.setTypeface(font1);
        holder.valpaytype.setTypeface(font2);
        holder.txtgrandtotal.setTypeface(font1);
        holder.valgrandtotal.setTypeface(font2);
        holder.txtstatus.setTypeface(font1);

        mAdapter1 = new OrderHistoryItemAdapter(mContext, moviesList1);
        holder.recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recyclerView1.setAdapter(mAdapter1);
        mAdapter1.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}