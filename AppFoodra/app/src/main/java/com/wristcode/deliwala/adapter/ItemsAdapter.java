package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wristcode.deliwala.CartActivity;
import com.wristcode.deliwala.HotelActivity;
import com.wristcode.deliwala.ItemActivity;
import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.fragments.MainDishesFragment;
import com.wristcode.deliwala.fragments.MenuFragment;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    private List<Items> moviesList;
    private Context mContext;
    MenuFragment fragment;
    ExampleDBHelper dbHelper;
    SharedPreferences pref;
    public  List<String> VnameList=new ArrayList<>();
    public  List<String> VPriceList=new ArrayList<>();
    public  List<String> VIdList=new ArrayList<>();

    String subname, subresname, imgpath;
    int subresid, subqty = 0, subprice = 0, qty = 0, price = 0, TOTAL = 0, RATE = 0, itemprice;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtid, txtresid, txtresname, txttype, txtname, txtdesc, txtprice, txtminus, txtplus, txtadd, prodqty, txtimg,spinnerId;
        RelativeLayout relative;
        ImageView image, image1;

        Spinner spinnerPriceVariation;

        public MyViewHolder(View view) {
            super(view);
            spinnerId = view.findViewById(R.id.spinnerId);
            txtid = view.findViewById(R.id.txtid);
            txtresid = view.findViewById(R.id.txtresid);
            txtresname = view.findViewById(R.id.txtresname);
            txttype = view.findViewById(R.id.txttype);
            txtname = view.findViewById(R.id.txtname);
            txtdesc = view.findViewById(R.id.txtdesc);
            txtprice = view.findViewById(R.id.txtprice);
            txtminus = view.findViewById(R.id.txtminus);
            txtminus.setOnClickListener(this);
            txtplus = view.findViewById(R.id.txtplus);
            txtplus.setOnClickListener(this);
            txtadd = view.findViewById(R.id.txtadd);
            txtadd.setOnClickListener(this);
            prodqty = view.findViewById(R.id.prodqty);
            txtimg = view.findViewById(R.id.txtimg);
            relative = view.findViewById(R.id.relative);
            image = view.findViewById(R.id.image);
            image1 = view.findViewById(R.id.image1);
            fragment = new MenuFragment();
            dbHelper = new ExampleDBHelper(mContext);
            pref = PreferenceManager.getDefaultSharedPreferences(mContext);

            spinnerPriceVariation=(Spinner)view.findViewById(R.id.spinnerPriceVariation);

        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.txtadd:
                    int val1 = Integer.parseInt(txtresid.getText().toString());
                    Boolean val2 = dbHelper.checkresid(val1);
                    //Toast.makeText(mContext, "ResId: "+txtresid.getText().toString()+ " ID: "+val2.toString()+ " Flag: "+pref.getString("fg","").toString(), Toast.LENGTH_SHORT).show();
                    if ((val2.equals(true) && pref.getString("fg","").toString().equals("1")) || (val2.equals(false) && pref.getString("fg","").toString().equals("0")))
                    {
                        txtadd.setVisibility(View.GONE);
                        txtminus.setVisibility(View.VISIBLE);
                        txtplus.setVisibility(View.VISIBLE);
                        prodqty.setVisibility(View.VISIBLE);
                        prodqty.setText("1");
                        dbHelper.insertItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtresid.getText().toString()), txtresname.getText().toString(), txtname.getText().toString(), Integer.parseInt(prodqty.getText().toString()), Integer.parseInt(txtprice.getText().toString()), Integer.parseInt(txtprice.getText().toString()), txtimg.getText().toString());

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("fg", "1");
                        editor.apply();
                    }
                    else
                    {
                        //Toast.makeText(mContext, "Different Restaurant", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle("You can order only from one hotel");
                        alertDialog.setMessage("Are you sure you want to clear your cart?");
                        alertDialog.setIcon(R.drawable.delete);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dbHelper.deleteAllData();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("fg", "0");
                                editor.apply();
                                passval(0);
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
                    }

                    TOTAL = dbHelper.gettotalqty();
                    RATE = dbHelper.gettotalprice();
                    passprice(RATE);
                    passval(TOTAL);

                    break;

                case R.id.txtplus:
                    int i = Integer.parseInt(prodqty.getText().toString());
                    i++;
                    prodqty.setText(String.valueOf(i));

                    int value1 = Integer.parseInt(txtid.getText().toString());
                    Boolean value2 = dbHelper.checksubid(value1);
                    if (value2.equals(true)) {
                        Cursor rs = dbHelper.getItem(Integer.parseInt(txtid.getText().toString()));
                        while (rs.moveToNext()) {
                            subresid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                            subresname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                            subname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                            subqty = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                            subprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                            itemprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                            imgpath = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                            qty = subqty + 1;
                            price = subprice + Integer.parseInt(txtprice.getText().toString());
                            dbHelper.updateItem(value1, subresid, subresname, txtname.getText().toString(), qty, price, itemprice, imgpath);
                        }
                    } else {
                        dbHelper.insertItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtresid.getText().toString()), txtresname.getText().toString(), txtname.getText().toString(), Integer.parseInt(prodqty.getText().toString()), Integer.parseInt(txtprice.getText().toString()), Integer.parseInt(txtprice.getText().toString()), txtimg.getText().toString());
                    }

//                    SharedPreferences.Editor editor1 = pref.edit();
//                    editor1.putString("fg", "1");
//                    editor1.apply();

                    TOTAL = dbHelper.gettotalqty();
                    RATE = dbHelper.gettotalprice();
                    passprice(RATE);
                    passval(TOTAL);
                    break;


                case R.id.txtminus:
                    i = Integer.parseInt(prodqty.getText().toString());
                    if (i > 1)
                    {
                        i--;
                        prodqty.setText(String.valueOf(i));
                        int value3 = Integer.parseInt(txtid.getText().toString());
                        Boolean value4 = dbHelper.checksubid(value3);
                        if (value4.equals(true)) {
                            Cursor rs = dbHelper.getItem(Integer.parseInt(txtid.getText().toString()));
                            while (rs.moveToNext()) {
                                subresid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                                subresname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                                subname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                                subqty = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                                subprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                                itemprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                                imgpath = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                                qty = subqty - 1;
                                price = subprice - Integer.parseInt(txtprice.getText().toString());
                                if (qty == 0) {
                                    dbHelper.deleteItem(Integer.parseInt(txtid.getText().toString()));
                                } else {
                                    dbHelper.updateItem(value3, subresid, subresname, txtname.getText().toString(), qty, price, itemprice, imgpath);
                                }
                            }
                        } else {

                        }
                    }
                    else if (i <= 0 || i == 1)
                    {
                        dbHelper.deleteItem(Integer.parseInt(txtid.getText().toString()));
                        txtminus.setVisibility(View.GONE);
                        txtplus.setVisibility(View.GONE);
                        prodqty.setVisibility(View.GONE);
                        txtadd.setVisibility(View.VISIBLE);
                    }

                    if(dbHelper.gettotalqty() == 0)
                    {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("fg", "0");
                        editor.apply();
                    }

                    TOTAL = dbHelper.gettotalqty();
                    RATE = dbHelper.gettotalprice();
                    passprice(RATE);
                    passval1(TOTAL);
                    break;
            }
            dbHelper.close();
        }
    }

    public void passval(int val)
    {
        ((HotelActivity) mContext).setCart(val);
        //Toast.makeText(mContext, "Val: "+String.valueOf(val), Toast.LENGTH_SHORT).show();
        //this.fragment.setCart(val);
    }

    public void passval1(int val)
    {
        ((HotelActivity) mContext).setCart(val);
        //this.fragment.setCart(val);
    }

    public void passprice(int val) {
        //((MenuFragment) fragment).setPrice(val);
    }

    public ItemsAdapter(Context mContext, List<Items> moviesList, MenuFragment fragment) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Items movie = moviesList.get(position);

        holder.txtresid.setText(movie.getResid());
        holder.txtresname.setText(movie.getResname());
        holder.txttype.setText(movie.getType());
        holder.txtname.setText(movie.getName());
        holder.txtdesc.setText(movie.getDescp());






        if(holder.txttype.getText().toString().equals("veg"))
        {
            Glide.with(mContext).load(R.drawable.veg)
                    .placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo)
                    .into(holder.image1);
        }
        else if(holder.txttype.getText().toString().equals("non"))
        {
            Glide.with(mContext).load(R.drawable.non)
                    .placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo)
                    .into(holder.image1);
        }


        VnameList=movie.getVname();
        VPriceList=movie.getVprice();
        VIdList=movie.getVid();


        if(movie.getVid().size()==0) {
            holder.spinnerPriceVariation.setVisibility(View.GONE);
            holder.txtprice.setText(movie.getPrice());
            holder.txtid.setText(movie.getId());
            holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()))));


            if (dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString())) > 0) {
                holder.txtadd.setVisibility(View.GONE);
                holder.txtminus.setVisibility(View.VISIBLE);
                holder.txtplus.setVisibility(View.VISIBLE);
                holder.prodqty.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.txtadd.setVisibility(View.VISIBLE);
                holder.txtminus.setVisibility(View.GONE);
                holder.txtplus.setVisibility(View.GONE);
                holder.prodqty.setVisibility(View.GONE);
            }
        }
        else {

            List<String> Vname =  new ArrayList<String>();
            final List<String> VPrice =  new ArrayList<String>();
            final List<String> Vid =  new ArrayList<String>();
            holder.spinnerPriceVariation.setVisibility(View.VISIBLE);

            for(int i=0;i<movie.getVname().size();i++){
                Vname.add(VnameList.get(i));
                VPrice.add(VPriceList.get(i));
                Vid.add(VIdList.get(i));
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    mContext, android.R.layout.simple_spinner_item, Vname);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerPriceVariation.setAdapter(adapter);


          //  holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()))));


            holder.spinnerPriceVariation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                  //  Toast.makeText(mContext,String.valueOf(VPriceList.get(i)), Toast.LENGTH_SHORT).show();
                    holder.txtprice.setText(VPrice.get(i));
                    holder.txtid.setText(Vid.get(i));
                    holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()))));


                    if (dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString())) > 0) {
                        holder.txtadd.setVisibility(View.GONE);
                        holder.txtminus.setVisibility(View.VISIBLE);
                        holder.txtplus.setVisibility(View.VISIBLE);
                        holder.prodqty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.txtadd.setVisibility(View.VISIBLE);
                        holder.txtminus.setVisibility(View.GONE);
                        holder.txtplus.setVisibility(View.GONE);
                        holder.prodqty.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    holder.txtprice.setText(VPrice.get(0));
                    holder.txtid.setText(Vid.get(0));
                    holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()))));


                    if (dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString())) > 0) {
                        holder.txtadd.setVisibility(View.GONE);
                        holder.txtminus.setVisibility(View.VISIBLE);
                        holder.txtplus.setVisibility(View.VISIBLE);
                        holder.prodqty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.txtadd.setVisibility(View.VISIBLE);
                        holder.txtminus.setVisibility(View.GONE);
                        holder.txtplus.setVisibility(View.GONE);
                        holder.prodqty.setVisibility(View.GONE);
                    }
                }
            });

        }
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.txtname.setTypeface(font);
        holder.txtdesc.setTypeface(font2);
        holder.txtprice.setTypeface(font2);
        holder.txtadd.setTypeface(font2);
        holder.prodqty.setTypeface(font2);

        //  Glide.with(mContext).load(movie.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}