package com.wristcode.deliwala.adapter;

/**
 * Created by nayak on 02-08-2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    public static final String mypreference = "mypref";
    public String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag = 0;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Items> moviesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, txtqty, txtprice, txtplus, txtminus;
        RelativeLayout relative;
        ImageView image;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtprice = (TextView) view.findViewById(R.id.txtprice);
            txtqty = (TextView) view.findViewById(R.id.txtqty);
            txtplus = view.findViewById(R.id.txtplus);
            txtminus = view.findViewById(R.id.txtminus);
            relative = (RelativeLayout) view.findViewById(R.id.relative);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public CartAdapter(Context mContext, List<Items> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Items movie = moviesList.get(position);
        holder.txtname.setText(movie.getName());
        holder.txtprice.setText(movie.getPrice());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font);
        holder.txtprice.setTypeface(font2);
        holder.txtqty.setTypeface(font2);

        Glide.with(mContext).load(movie.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.image);

        Glide.with(mContext).load(movie.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}