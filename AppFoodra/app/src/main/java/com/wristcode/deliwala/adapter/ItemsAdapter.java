package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wristcode.deliwala.HotelActivity;
import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.fragments.MenuFragment;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> implements Filterable
{
    private List<Items> moviesList;
    private List<Items> movieListFiltered;
    private Context mContext;
    MenuFragment fragment;
    ExampleDBHelper dbHelper;
    SharedPreferences pref;
    public  List<String> VnameList=new ArrayList<>();
    public  List<String> VPriceList=new ArrayList<>();
    public  List<String> VIdList=new ArrayList<>();

    String subname, subvarname, subresname, imgpath;
    int subvarid, subresid, subqty = 0, subprice = 0, qty = 0, price = 0, TOTAL = 0, RATE = 0, itemprice;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtid, txtvarid, txtvarname, txtresid, txtresname, txttype, txtname, txtdesc, txtprice, txtminus, txtplus, txtadd, prodqty, txtimg, spinnerId;
        RelativeLayout relative;
        ImageView image, image1;

        Spinner spinnerPriceVariation;

        public MyViewHolder(View view) {
            super(view);
            spinnerId = view.findViewById(R.id.spinnerId);
            txtid = view.findViewById(R.id.txtid);
            txtvarid = view.findViewById(R.id.txtvarid);
            txtvarname = view.findViewById(R.id.txtvarname);
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
            spinnerPriceVariation = view.findViewById(R.id.spinnerPriceVariation);
            fragment = new MenuFragment();
            dbHelper = new ExampleDBHelper(mContext);
            pref = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.txtadd:
                    int val1 = Integer.parseInt(txtresid.getText().toString());
                    int s1 = Integer.parseInt(txtvarid.getText().toString());
                    Boolean val2 = dbHelper.checkresid(val1);
                    if ((val2.equals(true) && pref.getString("fg","").toString().equals("1")) || (val2.equals(false) && pref.getString("fg","").toString().equals("0")))
                    {
                        txtadd.setVisibility(View.GONE);
                        txtminus.setVisibility(View.VISIBLE);
                        txtplus.setVisibility(View.VISIBLE);
                        prodqty.setVisibility(View.VISIBLE);
                        prodqty.setText("1");
                        dbHelper.insertItem(Integer.parseInt(txtid.getText().toString()), s1, txtvarname.getText().toString(), Integer.parseInt(txtresid.getText().toString()), txtresname.getText().toString(), txtname.getText().toString(), Integer.parseInt(prodqty.getText().toString()), Integer.parseInt(txtprice.getText().toString()), Integer.parseInt(txtprice.getText().toString()), txttype.getText().toString());

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("fg", "1");
                        editor.apply();
                    }
                    else
                    {
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
                    int v1 = Integer.parseInt(txtvarid.getText().toString());
                    Boolean value2 = dbHelper.checksubid(value1, v1);
                    if (value2.equals(true))
                    {
                        Cursor rs = dbHelper.getItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
                        while (rs.moveToNext())
                        {
                            subvarid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID));
                            subvarname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARNAME));
                            subresid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                            subresname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                            subname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                            subqty = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                            subprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                            itemprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                            imgpath = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                            qty = subqty + 1;
                            price = subprice + Integer.parseInt(txtprice.getText().toString());
                            dbHelper.updateItem(value1, subvarid, subvarname, subresid, subresname, txtname.getText().toString(), qty, price, itemprice, imgpath);
                        }
                    }
                    else
                    {
                        dbHelper.insertItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()), txtvarname.getText().toString(), Integer.parseInt(txtresid.getText().toString()), txtresname.getText().toString(), txtname.getText().toString(), Integer.parseInt(prodqty.getText().toString()), Integer.parseInt(txtprice.getText().toString()), Integer.parseInt(txtprice.getText().toString()), txttype.getText().toString());
                    }

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
                        int v3 = Integer.parseInt(txtvarid.getText().toString());
                        Boolean value4 = dbHelper.checksubid(value3, v3);
                        if (value4.equals(true)) {
                            Cursor rs = dbHelper.getItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
                            while (rs.moveToNext())
                            {
                                subvarid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID));
                                subvarname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARNAME));
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
                                    dbHelper.deleteItem(Integer.parseInt(txtid.getText().toString()), subvarid);
                                } else {
                                    dbHelper.updateItem(value3, subvarid, subvarname, subresid, subresname, txtname.getText().toString(), qty, price, itemprice, imgpath);
                                }
                            }
                        } else {

                        }
                    }
                    else if (i <= 0 || i == 1)
                    {
                        dbHelper.deleteItem(Integer.parseInt(txtid.getText().toString()), Integer.parseInt(txtvarid.getText().toString()));
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
    }

    public void passval1(int val)
    {
        ((HotelActivity) mContext).setCart(val);
    }

    public void passprice(int val)
    {
        //((MenuFragment) fragment).setPrice(val);
    }

    public ItemsAdapter(Context mContext, List<Items> moviesList, MenuFragment fragment) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.movieListFiltered = moviesList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Items movie = movieListFiltered.get(position);
        holder.txtid.setText(movie.getId());
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

        VnameList = movie.getVname();
        VPriceList = movie.getVprice();
        VIdList = movie.getVid();


        if(movie.getVid().size()==0)
        {
            holder.spinnerPriceVariation.setVisibility(View.GONE);
            holder.txtprice.setText(movie.getPrice());
            holder.txtvarname.setText("");
            holder.txtvarid.setText("0");
            holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()),Integer.parseInt(holder.txtvarid.getText().toString()))));


            if (dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()), Integer.parseInt(holder.txtvarid.getText().toString())) > 0) {
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

            final List<String> Vname =  new ArrayList<String>();
            final List<String> VPrice =  new ArrayList<String>();
            final List<String> Vid =  new ArrayList<String>();
            holder.spinnerPriceVariation.setVisibility(View.VISIBLE);

            for(int i=0;i<movie.getVname().size();i++)
            {
                Vname.add(VnameList.get(i));
                VPrice.add(VPriceList.get(i));
                Vid.add(VIdList.get(i));
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, Vname);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerPriceVariation.setAdapter(adapter);

            holder.spinnerPriceVariation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    holder.txtprice.setText(VPrice.get(i));
                    holder.txtvarid.setText(Vid.get(i));
                    holder.txtvarname.setText(Vname.get(i));
                    holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()), Integer.parseInt(holder.txtvarid.getText().toString()))));

                    if (dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()), Integer.parseInt(holder.txtvarid.getText().toString())) > 0) {
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
                public void onNothingSelected(AdapterView<?> adapterView)
                {
                    holder.txtprice.setText(VPrice.get(0));
                    holder.txtvarid.setText(Vid.get(0));
                    holder.txtvarname.setText(Vname.get(0));
                    holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()), Integer.parseInt(holder.txtvarid.getText().toString()))));

                    if (dbHelper.getQuantity(Integer.parseInt(holder.txtid.getText().toString()), Integer.parseInt(holder.txtvarid.getText().toString())) > 0) {
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
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    movieListFiltered = moviesList;
                }
                else
                {
                    List<Items> filteredList = new ArrayList<>();
                    for (Items row : moviesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row);
                        }
                    }
                    movieListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = movieListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                movieListFiltered = (ArrayList<Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return movieListFiltered.size();
    }
}