package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.HotelActivity;

import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.R;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder> {
    public static final String mypreference = "mypref";
    public String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag = 0;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Restaurants> moviesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, txtdesc, txtkm, txttime;
        RelativeLayout relativehotel;
        ImageView image;
        ImageView thumbnail;
        RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            txtname = view.findViewById(R.id.txtname);
            txtdesc = view.findViewById(R.id.txtdesc);
            txttime = view.findViewById(R.id.txttime);
            ratingBar = view.findViewById(R.id.ratingBar);
            image = view.findViewById(R.id.image);
            relativehotel = view.findViewById(R.id.relativehotel);
        }
    }

    public RestaurantsAdapter(Context mContext, List<Restaurants> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Restaurants movie = moviesList.get(position);
        holder.txtname.setText(movie.getResname());
        holder.txtdesc.setText("Chinese, Arabian, Italian");
        holder.txttime.setText("10AM - 10PM");
        holder.ratingBar.setRating(Float.valueOf(movie.getRespop()));

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font);
        holder.txtdesc.setTypeface(font2);
        holder.txttime.setTypeface(font2);

        Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/"+movie.getResimg())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.image);

        //Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/"+movie.getResimg()).into(holder.image);

        holder.relativehotel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext, HotelActivity.class);
                i.putExtra("id",movie.getResid());
                i.putExtra("address",movie.getResadd());
                i.putExtra("isOpen",movie.getResisopen());
                i.putExtra("descp",movie.getResdescp());
                i.putExtra("img",movie.getResimg());
                i.putExtra("pop",movie.getRespop());
                i.putExtra("name",movie.getResname());
                mContext.startActivity(i);
            }
        });
    }
    public void updateList(List<Restaurants> list){
        moviesList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}