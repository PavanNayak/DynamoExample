package com.wristcode.deliwala.adapter;

import android.content.Context;
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
import com.wristcode.deliwala.Pojo.Reviews;
import com.wristcode.deliwala.R;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>
{
    private List<Reviews> reviewList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username, userdesc;
        RelativeLayout relativereview;
        ImageView image;
        RatingBar ratingBar;

        public MyViewHolder(View view)
        {
            super(view);
            username = view.findViewById(R.id.username);
            userdesc = view.findViewById(R.id.userdesc);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            image = view.findViewById(R.id.image);
            relativereview = view.findViewById(R.id.relativereview);
        }
    }

    public ReviewsAdapter(Context mContext, List<Reviews> categoryList) {
        this.mContext = mContext;
        this.reviewList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reviews review = reviewList.get(position);
        holder.username.setText(review.getName());
        holder.userdesc.setText(review.getReviews());
        holder.ratingBar.setRating(Float.valueOf(review.getRatings()));

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.username.setTypeface(font);
        holder.userdesc.setTypeface(font2);

//        Glide.with(mContext).load(review.getImage())
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.image);
//
//        Glide.with(mContext).load(review.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}