package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wristcode.deliwala.AddAddressActivity;
import com.wristcode.deliwala.AddressActivity;
import com.wristcode.deliwala.LoginActivity;
import com.wristcode.deliwala.PaymentActivity;
import com.wristcode.deliwala.Pojo.Address;
import com.wristcode.deliwala.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ajay Jagadish on 10-Mar-18.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder>
{
    ArrayList<HashMap<String, String>> contactList;
    AddAddressActivity addList;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Address> moviesList;
    private Context mContext;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtid, txtname, txtaddress, txtlat, txtlong, txtedit;
        RadioButton raddress;

        public MyViewHolder(View view)
        {
            super(view);
            contactList = new ArrayList<>();
            addList = new AddAddressActivity();
            txtid = (TextView) view.findViewById(R.id.txtid);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtaddress = (TextView) view.findViewById(R.id.txtaddress);
            txtlat = (TextView) view.findViewById(R.id.txtlat);
            txtlong = (TextView) view.findViewById(R.id.txtlong);
            txtedit = (TextView) view.findViewById(R.id.txtedit);
            raddress = (RadioButton) view.findViewById(R.id.raddress);

            txtedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(mContext, AddressActivity.class);
                    i.putExtra("MESSAGE", "");
                    i.putExtra("FLAG", "1");
                    mContext.startActivity(i);
                }
            });

            raddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = getAdapterPosition();

                    SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("AddressId", txtid.getText().toString());
                    editor1.putString("Address", txtaddress.getText().toString());
                    editor1.putString("Latitude", txtlat.getText().toString());
                    editor1.putString("Longitiude", txtlong.getText().toString());
                    editor1.apply();

                    notifyDataSetChanged();
                }
            });
        }
    }

    public AddressAdapter(Context mContext, List<Address> moviesList)
    {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_address_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Address movie = moviesList.get(position);
        holder.txtid.setText(movie.getUserid());
        holder.txtname.setText(movie.getUsername());
        holder.txtaddress.setText(movie.getUseraddress());
        holder.txtlat.setText(movie.getUserlat());
        holder.txtlong.setText(movie.getUserlong());

        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font1);
        holder.txtaddress.setTypeface(font2);

        holder.raddress.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
