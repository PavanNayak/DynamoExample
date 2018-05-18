package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.OrderHistoryActivity;
import com.wristcode.deliwala.OrderListActivity;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.Pojo.OrderHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>
{
    ArrayList<HashMap<String, String>> contactList;
    OrderHistoryActivity subCatList;
    private List<OrderHistory> moviesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtorderid, txtorderdate, txtcusadd, txtresid, txtresname, valpaytype, valgrandtotal, txtdetails, txtstatus;
        ImageView imgres, imgnext;
        LinearLayout linearlayout;

        public MyViewHolder(View view) {
            super(view);
            contactList = new ArrayList<>();
            subCatList = new OrderHistoryActivity();
            txtorderid = view.findViewById(R.id.txtorderid);
            txtorderdate = view.findViewById(R.id.txtorderdate);
            txtcusadd = view.findViewById(R.id.txtcusadd);
            txtresid = view.findViewById(R.id.txtresid);
            txtresname = view.findViewById(R.id.txtresname);
            valpaytype = view.findViewById(R.id.valpaytype);
            valgrandtotal = view.findViewById(R.id.valgrandtotal);
            txtstatus = view.findViewById(R.id.txtstatus);
            txtdetails = view.findViewById(R.id.txtdetails);
            imgres = view.findViewById(R.id.imgres);
            imgnext = view.findViewById(R.id.imgnext);
            linearlayout = view.findViewById(R.id.linearlayout);
        }
    }

    public OrderHistoryAdapter(Context mContext, List<OrderHistory> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_row1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderHistory movie = moviesList.get(position);
        holder.txtorderid.setText("Order ID: #" + movie.getoId());
        holder.txtresid.setText(movie.getoResId());
        holder.txtresname.setText(movie.getoResName());
        holder.valpaytype.setText(movie.getoPayType());
        holder.valgrandtotal.setText("₹ " + movie.getoTotal());
        holder.txtstatus.setText(movie.getoStatus());

        String strCurrentDate = movie.getoDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(newDate);
        holder.txtorderdate.setText("Date: " + date);

        if (movie.getoStatus().toString().equals("received")) {
            holder.txtstatus.setText("Received");
        } else if (movie.getoStatus().toString().equals("dispatched")) {
            holder.txtstatus.setText("Processing");
        } else if (movie.getoStatus().toString().equals("delivered")) {
            holder.txtstatus.setText("Delivered");
        } else if (movie.getoStatus().toString().equals("processing")) {
            holder.txtstatus.setText("Processing");
        } else if (movie.getoStatus().toString().equals("ready")) {
            holder.txtstatus.setText("Processing");
        } else if (movie.getoStatus().toString().equals("onway")) {
            holder.txtstatus.setText("On The Way");
        } else if (movie.getoStatus().toString().equals("cancelled")) {
            holder.txtstatus.setText("Cancelled");
        }

        Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/" + movie.getoResImage())
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

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, OrderListActivity.class);
                i.putExtra("orderid", holder.txtorderid.getText().toString());
                i.putExtra("date", holder.txtorderdate.getText().toString());
                i.putExtra("cusaddress", movie.getoCusAdd());
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