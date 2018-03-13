package com.wristcode.deliwala.adapter;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.OrderHistoryActivity;
import com.wristcode.deliwala.OrderListActivity;
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
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtorderid, txtorderdate, txtresid, txtresname, valpaytype, valgrandtotal, txtdetails, txtstatus;
        ImageView imgres;

        public MyViewHolder(View view)
        {
            super(view);
            contactList = new ArrayList<>();
            subCatList = new OrderHistoryActivity();
            txtorderid = (TextView) view.findViewById(R.id.txtorderid);
            txtorderdate = (TextView) view.findViewById(R.id.txtorderdate);
            txtresid = (TextView) view.findViewById(R.id.txtresid);
            txtresname = (TextView) view.findViewById(R.id.txtresname);
            valpaytype = (TextView) view.findViewById(R.id.valpaytype);
            valgrandtotal = (TextView) view.findViewById(R.id.valgrandtotal);
            txtstatus = (TextView) view.findViewById(R.id.txtstatus);
            txtdetails = (TextView) view.findViewById(R.id.txtdetails);
            imgres = (ImageView) view.findViewById(R.id.imgres);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_row1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final OrderHistory movie = moviesList.get(position);
        holder.txtorderid.setText("Order ID: #" +movie.getoId());
        holder.txtorderdate.setText("Date: " + movie.getoDate());
        holder.txtresid.setText(movie.getoResId());
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
        else if (movie.getoStatus().toString().equals("delivered"))
        {
            holder.txtstatus.setText("Your order has been delivered!!!");
        }
        else if (movie.getoStatus().toString().equals("proceesing"))
        {
            holder.txtstatus.setText("Your order is in process!!!");
        }

        Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/"+movie.getoResImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgres);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtorderid.setTypeface(font1);
        holder.txtorderdate.setTypeface(font1);
        holder.txtresname.setTypeface(font);
        holder.valpaytype.setTypeface(font2);
        holder.valgrandtotal.setTypeface(font2);
        holder.txtstatus.setTypeface(font1);
        holder.txtdetails.setTypeface(font1);

        holder.txtdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(mContext, OrderListActivity.class);
                i.putExtra("orderid", holder.txtorderid.getText().toString());
                i.putExtra("date", holder.txtorderdate.getText().toString());
                i.putExtra("resid", holder.txtresid.getText().toString());
                i.putExtra("resname", holder.txtresname.getText().toString());
                i.putExtra("items", movie.getoItems().toString());
                i.putExtra("paymenttype", holder.valpaytype.getText().toString());
                i.putExtra("grandtotal", holder.valgrandtotal.getText().toString());
                i.putExtra("status", holder.txtstatus.getText().toString());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}