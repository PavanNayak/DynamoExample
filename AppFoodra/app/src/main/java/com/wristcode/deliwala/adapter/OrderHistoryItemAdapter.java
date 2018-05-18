package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wristcode.deliwala.OrderHistoryActivity;
import com.wristcode.deliwala.Pojo.OrderHistoryItems;
import com.wristcode.deliwala.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderHistoryItemAdapter extends RecyclerView.Adapter<OrderHistoryItemAdapter.MyViewHolder>
{
    ArrayList<HashMap<String, String>> contactList;
    OrderHistoryActivity subCatList;
    private List<OrderHistoryItems> moviesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView itemname, itemqty, itemprice;

        public MyViewHolder(View view)
        {
            super(view);
            contactList = new ArrayList<>();
            subCatList = new OrderHistoryActivity();
            itemname = view.findViewById(R.id.itemname);
            itemqty = view.findViewById(R.id.itemqty);
            itemprice = view.findViewById(R.id.itemprice);
        }
    }

    public OrderHistoryItemAdapter(Context mContext, List<OrderHistoryItems> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_items_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        OrderHistoryItems movie = moviesList.get(position);
        holder.itemname.setText(movie.getOhItemname()+ " "+ movie.getOhItemvariation());
        holder.itemprice.setText("₹ "+movie.getOhItemprice());
        holder.itemqty.setText("X"+movie.getOhItemqty());

        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.itemname.setTypeface(font1);
        holder.itemprice.setTypeface(font1);
        holder.itemqty.setTypeface(font1);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}