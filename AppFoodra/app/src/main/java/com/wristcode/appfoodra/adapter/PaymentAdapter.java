package com.wristcode.appfoodra.adapter;

/**
 * Created by nayak on 02-08-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.appfoodra.ItemActivity;
import com.wristcode.appfoodra.Pojo.Category;
import com.wristcode.appfoodra.Pojo.Items;
import com.wristcode.appfoodra.Pojo.Payment;
import com.wristcode.appfoodra.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    public static final String mypreference = "mypref";
    public String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag = 0;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Payment> moviesList;
    private Context mContext;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView payname;
        RelativeLayout relative;
        ImageView payimg;
        RadioButton radiobutton;

        public MyViewHolder(View view) {
            super(view);
            payname = view.findViewById(R.id.payname);
            relative = view.findViewById(R.id.relative);
            payimg = view.findViewById(R.id.payimg);
            radiobutton = view.findViewById(R.id.radiobutton);

            radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = getAdapterPosition();
                    payimg.setColorFilter(Color.parseColor("#DC143C"));
                    payname.setTextColor(Color.parseColor("#DC143C"));
                    notifyDataSetChanged();
                }
            });
        }
    }

    public PaymentAdapter(Context mContext, List<Payment> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Payment movie = moviesList.get(position);
        holder.payname.setText(movie.getName());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.payname.setTypeface(font2);

        Glide.with(mContext).load(movie.getImg())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.payimg);

        Glide.with(mContext).load(movie.getImg()).into(holder.payimg);
        holder.radiobutton.setChecked(lastSelectedPosition == position);

        int colorImg = (position == lastSelectedPosition)? Color.parseColor("#DC143C") : Color.parseColor("#C0C0C0");
        holder.payimg.setColorFilter(colorImg);
        int colorTitle = (position == lastSelectedPosition)? Color.parseColor("#DC143C") : Color.parseColor("#C0C0C0");
        holder.payname.setTextColor(colorTitle);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}