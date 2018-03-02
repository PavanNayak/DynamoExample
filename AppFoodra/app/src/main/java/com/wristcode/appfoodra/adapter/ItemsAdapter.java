package com.wristcode.appfoodra.adapter;

/**
 * Created by nayak on 02-08-2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.appfoodra.ItemActivity;
import com.wristcode.appfoodra.Pojo.Items;
import com.wristcode.appfoodra.R;

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
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    public static final String mypreference = "mypref";
    public  String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag=0;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Items> moviesList;
    private Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtname,txtdesc,txtprice, txtminus, txtplus, txtadd, prodqty;
        RelativeLayout relative;
        ImageView image;

        ImageView thumbnail;

        public MyViewHolder(View view)
        {
            super(view);
            txtname = view.findViewById(R.id.txtname);
            txtdesc = view.findViewById(R.id.txtdesc);
            txtprice = view.findViewById(R.id.txtprice);
            txtminus = view.findViewById(R.id.txtminus);
            txtplus = view.findViewById(R.id.txtplus);
            txtadd = view.findViewById(R.id.txtadd);
            prodqty = view.findViewById(R.id.prodqty);
            relative = view.findViewById(R.id.relative);
            image = view.findViewById(R.id.image);
        }

    }

    public ItemsAdapter(Context mContext, List<Items> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        Items movie = moviesList.get(position);
        holder.txtname.setText(movie.getName());
        holder.txtdesc.setText(movie.getDescp());
        holder.txtprice.setText(movie.getPrice());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font);
        holder.txtdesc.setTypeface(font2);
        holder.txtprice.setTypeface(font2);
        holder.txtadd.setTypeface(font2);
        holder.prodqty.setTypeface(font2);

        Glide.with(mContext).load(movie.getImage()).into(holder.image);

        holder.txtadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                holder.txtadd.setVisibility(View.GONE);
                holder.txtminus.setVisibility(View.VISIBLE);
                holder.txtplus.setVisibility(View.VISIBLE);
                holder.prodqty.setVisibility(View.VISIBLE);
            }
        });

        holder.txtminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(holder.prodqty.getText().toString().equals("1"))
                {
                    holder.txtminus.setVisibility(View.GONE);
                    holder.txtplus.setVisibility(View.GONE);
                    holder.prodqty.setVisibility(View.GONE);
                    holder.txtadd.setVisibility(View.VISIBLE);
                }
                else
                {
                    int i = Integer.parseInt(holder.prodqty.getText().toString());
                    i--;
                    holder.prodqty.setText(String.valueOf(i));
                }
            }
        });

        holder.txtplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int i = Integer.parseInt(holder.prodqty.getText().toString());
                i++;
                holder.prodqty.setText(String.valueOf(i));
            }
        });

        holder.relative.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext, ItemActivity.class);
                mContext.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }







}