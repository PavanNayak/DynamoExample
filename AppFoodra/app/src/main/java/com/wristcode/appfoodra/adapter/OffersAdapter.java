package com.wristcode.appfoodra.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wristcode.appfoodra.HotelActivity;

import com.wristcode.appfoodra.Pojo.Offers;
import com.wristcode.appfoodra.R;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {
    public static final String mypreference = "mypref";
    public String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag = 0;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Offers> moviesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, txtdesc, txtkm, txttime;
        RelativeLayout relativehotel;
        ImageView image;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtdesc = (TextView) view.findViewById(R.id.txtdesc);
            txttime = (TextView) view.findViewById(R.id.txttime);
            image = (ImageView) view.findViewById(R.id.image);
            relativehotel = (RelativeLayout) view.findViewById(R.id.relativehotel);
        }
    }

    public OffersAdapter(Context mContext, List<Offers> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offers movie = moviesList.get(position);
        holder.txtname.setText(movie.getName());
        holder.txtdesc.setText(movie.getDescp());
        holder.txttime.setText(movie.getTime());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font);
        holder.txtdesc.setTypeface(font2);
        holder.txttime.setTypeface(font2);

        Glide.with(mContext).load(movie.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.image);

        Glide.with(mContext).load(movie.getImage()).into(holder.image);

        holder.relativehotel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext, HotelActivity.class);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}