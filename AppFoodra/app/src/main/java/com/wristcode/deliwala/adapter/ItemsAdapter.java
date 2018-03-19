package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.ItemActivity;
import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.fragments.MainDishesFragment;
import com.wristcode.deliwala.fragments.MenuFragment;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    private List<Items> moviesList;
    private Context mContext;
    MenuFragment fragment;
    ExampleDBHelper dbHelper;
    SharedPreferences pref;
    String subname, imgpath;
    int subresid, subqty = 0, subprice = 0, qty = 0, price = 0, TOTAL = 0, RATE = 0, itemprice;
    ;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtid, txtresid, txtname, txtdesc, txtprice, txtminus, txtplus, txtadd, prodqty, txtimg;
        RelativeLayout relative;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            txtid = view.findViewById(R.id.txtid);
            txtresid = view.findViewById(R.id.txtresid);
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
            fragment = new MenuFragment();
            dbHelper = new ExampleDBHelper(mContext);
            pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txtadd:
                    if(pref.getString("id",""))

                    txtadd.setVisibility(View.GONE);
                    txtminus.setVisibility(View.VISIBLE);
                    txtplus.setVisibility(View.VISIBLE);
                    prodqty.setVisibility(View.VISIBLE);
                    prodqty.setText("1");
                    dbHelper.insertItem(Integer.parseInt(txtid.getText().toString()), 2, txtname.getText().toString(), Integer.parseInt(prodqty.getText().toString()), Integer.parseInt(txtprice.getText().toString()), Integer.parseInt(txtprice.getText().toString()), txtimg.getText().toString());

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("fg", "1");
                    editor.apply();

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
                    if (value2.equals(true))
                    {
                        Cursor rs = dbHelper.getItem(Integer.parseInt(txtid.getText().toString()));
                        while (rs.moveToNext())
                        {
                            subresid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                            subname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                            subqty = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                            subprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                            itemprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                            imgpath = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                            qty = subqty + 1;
                            price = subprice + Integer.parseInt(txtprice.getText().toString());
                            dbHelper.updateItem(value1, subresid, txtname.getText().toString(), qty, price, itemprice, imgpath);
                        }
                    }
                    else
                    {
                        dbHelper.insertItem(Integer.parseInt(txtid.getText().toString()), 2, txtname.getText().toString(), Integer.parseInt(prodqty.getText().toString()), Integer.parseInt(txtprice.getText().toString()), Integer.parseInt(txtprice.getText().toString()), txtimg.getText().toString());
                    }

                    SharedPreferences.Editor editor1 = pref.edit();
                    editor1.putString("fg", "1");
                    editor1.apply();

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
                        if (value4.equals(true))
                        {
                            Cursor rs = dbHelper.getItem(Integer.parseInt(txtid.getText().toString()));
                            while (rs.moveToNext())
                            {
                                subresid = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                                subname = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                                subqty = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                                subprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                                itemprice = rs.getInt(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ACTUALPRICE));
                                imgpath = rs.getString(rs.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                                qty = subqty - 1;
                                price = subprice - Integer.parseInt(txtprice.getText().toString());
                                if (qty == 0)
                                {
                                    dbHelper.deleteItem(Integer.parseInt(txtid.getText().toString()));
                                }
                                else
                                {
                                    dbHelper.updateItem(value3, subresid, txtname.getText().toString(), qty, price, itemprice, imgpath);
                                }
                            }
                        }
                        else
                        {

                        }

                        TOTAL = dbHelper.gettotalqty();
                        RATE = dbHelper.gettotalprice();
                        passprice(RATE);
                        passval1(TOTAL);
                    }
                    else if (i <= 0 || i == 1)
                    {
                        txtminus.setVisibility(View.GONE);
                        txtplus.setVisibility(View.GONE);
                        prodqty.setVisibility(View.GONE);
                        txtadd.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            dbHelper.close();
        }
    }

    public void passval(int val)
    {
        //Toast.makeText(mContext, "Val: "+String.valueOf(val), Toast.LENGTH_SHORT).show();
        //this.fragment.setCart(val);
    }

    public void passval1(int val)
    {
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
        holder.txtid.setText(movie.getId());
        holder.txtname.setText(movie.getName());
        holder.txtdesc.setText(movie.getDescp());
        holder.txtprice.setText(movie.getPrice());
        holder.prodqty.setText(String.valueOf(dbHelper.getQuantity(Integer.parseInt(movie.getId()))));

        if (dbHelper.getQuantity(Integer.parseInt(movie.getId())) > 0)
        {
            holder.txtadd.setVisibility(View.GONE);
            holder.txtminus.setVisibility(View.VISIBLE);
            holder.txtplus.setVisibility(View.VISIBLE);
            holder.prodqty.setVisibility(View.VISIBLE);
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

//        holder.txtadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.txtadd.setVisibility(View.GONE);
//                holder.txtminus.setVisibility(View.VISIBLE);
//                holder.txtplus.setVisibility(View.VISIBLE);
//                holder.prodqty.setVisibility(View.VISIBLE);
//            }
//        });
//
//        holder.txtminus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.prodqty.getText().toString().equals("1")) {
//                    holder.txtminus.setVisibility(View.GONE);
//                    holder.txtplus.setVisibility(View.GONE);
//                    holder.prodqty.setVisibility(View.GONE);
//                    holder.txtadd.setVisibility(View.VISIBLE);
//                } else {
//                    int i = Integer.parseInt(holder.prodqty.getText().toString());
//                    i--;
//                    holder.prodqty.setText(String.valueOf(i));
//                }
//            }
//        });
//
//        holder.txtplus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int i = Integer.parseInt(holder.prodqty.getText().toString());
//                i++;
//                holder.prodqty.setText(String.valueOf(i));
//            }
//        });

//        holder.relative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(mContext, ItemActivity.class);
//                mContext.startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}