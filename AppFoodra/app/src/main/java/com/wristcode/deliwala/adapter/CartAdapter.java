package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wristcode.deliwala.CartActivity;
import com.wristcode.deliwala.HotelActivity;
import com.wristcode.deliwala.NavDrawer;
import com.wristcode.deliwala.Pojo.Cart;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    public static final String mypreference = "mypref";
    public String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag = 0;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Cart> moviesList;
    private Context mContext;
    ExampleDBHelper dh;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtid, txtvarid, txtvarname, txtresid, txtresname, txttype, txtname, txtqty, txtprice, txtplus, txtminus;
        RelativeLayout relative;
        ImageView image, delete;
        String subname, subname1, subvarname, subvarname1, subresname, subresname1, imgtype, imgtype1;
        int subvarid, subvarid1, subresid, subresid1, subqty, subqty1, subprice, subprice1, subitemprice, subitemprice1, qty, qty1, price, price1, TOTAL = 0, RATE = 0;

        public MyViewHolder(View view) {
            super(view);
            txtid = (TextView) view.findViewById(R.id.txtid);
            txtvarid = (TextView) view.findViewById(R.id.txtvarid);
            txtvarname = (TextView) view.findViewById(R.id.txtvarname);
            txtresid = (TextView) view.findViewById(R.id.txtresid);
            txtresname = (TextView) view.findViewById(R.id.txtresname);
            txtname = (TextView) view.findViewById(R.id.txtname);
            txtprice = (TextView) view.findViewById(R.id.txtprice);
            txtqty = (TextView) view.findViewById(R.id.txtqty);
            txttype = (TextView) view.findViewById(R.id.txttype);
            txtplus = view.findViewById(R.id.txtplus);
            txtplus.setOnClickListener(this);
            txtminus = view.findViewById(R.id.txtminus);
            txtminus.setOnClickListener(this);
            relative = (RelativeLayout) view.findViewById(R.id.relative);
            image = (ImageView) view.findViewById(R.id.image);
            delete = (ImageView) view.findViewById(R.id.delete);
            delete.setOnClickListener(this);
            dh = new ExampleDBHelper(mContext);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.delete:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Confirm Delete");
                    alertDialog.setMessage("Are you sure you want to delete this item from cart?");
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dh.deleteItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
                            if (dh.getProfilesCount() > 0)
                            {
                                Intent i = new Intent(mContext, CartActivity.class);
                                mContext.startActivity(i);
                            }
                            else
                            {
                                Intent i = new Intent(mContext, NavDrawer.class);
                                mContext.startActivity(i);
                            }
                        }
                    });

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = alertDialog.create();
                    alert.show();
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.rgb(103, 52, 185));
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.rgb(103, 52, 185));
                    break;

                case R.id.txtplus:
                    int i = Integer.parseInt(txtqty.getText().toString());
                    i++;
                    txtqty.setText(String.valueOf(i));
                    Cursor rs = dh.getItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
                    while (rs.moveToNext())
                    {
                        subvarid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID));
                        subvarname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARNAME));
                        subresid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                        subresname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                        subname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                        subqty = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                        subprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                        subitemprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                        imgtype = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                        qty = subqty + 1;
                        price = subprice + subitemprice;
                        dh.updateItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()), txtvarname.getText().toString(), Integer.parseInt(txtresid.getText().toString()), txtresname.getText().toString(), txtname.getText().toString(), qty, price, subitemprice, imgtype);
                        passVal();
                    }
                    break;

                case R.id.txtminus:
                    i = Integer.parseInt(txtqty.getText().toString());
                    if (i > 1)
                    {
                        i--;
                        txtqty.setText(String.valueOf(i));
                        Cursor rs1 = dh.getItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
                        while (rs1.moveToNext())
                        {
                            subvarid1 = rs1.getInt(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID));
                            subvarname1 = rs1.getString(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARNAME));
                            subresid1 = rs1.getInt(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                            subresname1 = rs1.getString(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                            subname1 = rs1.getString(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                            subqty1 = rs1.getInt(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                            subprice1 = rs1.getInt(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                            subitemprice1 = rs1.getInt(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                            imgtype1 = rs1.getString(rs1.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                            qty1 = subqty1 - 1;
                            price1 = subprice1 - subitemprice1;
                            dh.updateItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()), txtvarname.getText().toString(), Integer.parseInt(txtresid.getText().toString()), txtresname.getText().toString(), txtname.getText().toString(), qty1, price1, subitemprice1, imgtype1);
                            passVal();
                        }
                    }
                    else
                    {
                        dh.deleteItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
                        passVal();
                    }

                    break;
            }
            dh.close();
        }
    }

    public CartAdapter(Context mContext, List<Cart> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Cart movie = moviesList.get(position);
        holder.txtid.setText(movie.getId());
        holder.txtvarid.setText(movie.getVarid());
        holder.txtvarname.setText(movie.getVarname());
        holder.txtresid.setText(movie.getResid());
        holder.txtresname.setText(movie.getResname());
        holder.txttype.setText(movie.getType());
        holder.txtname.setText(movie.getName());
        holder.txtqty.setText(movie.getQty());
        holder.txtprice.setText(movie.getPrice());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font);
        holder.txtvarname.setTypeface(font1);
        holder.txtprice.setTypeface(font2);
        holder.txtqty.setTypeface(font2);

        if(holder.txttype.getText().toString().equals("veg"))
        {
            Glide.with(mContext).load(R.drawable.veg)
                    .placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo)
                    .into(holder.image);
        }
        else if(holder.txttype.getText().toString().equals("non"))
        {
            Glide.with(mContext).load(R.drawable.non)
                    .placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo)
                    .into(holder.image);
        }

//        Glide.with(mContext).load(movie.getImage())
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.image);
//
//        Glide.with(mContext).load(movie.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void passVal()
    {
        ((CartActivity)mContext).updateCartDetails();
    }
}