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
import com.wristcode.deliwala.Pojo.Category;
import com.wristcode.deliwala.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Category> moviesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catname;
        RelativeLayout relative;
        ImageView catimg;

        public MyViewHolder(View view) {
            super(view);
            catname = view.findViewById(R.id.catname);
            relative = view.findViewById(R.id.relative);
            catimg = view.findViewById(R.id.catimg);
        }
    }

    public CategoryAdapter(Context mContext, List<Category> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category movie = moviesList.get(position);
        holder.catname.setText(movie.getName());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.catname.setTypeface(font2);

        Glide.with(mContext).load(movie.getImg())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.catimg);

        Glide.with(mContext).load(movie.getImg()).into(holder.catimg);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}