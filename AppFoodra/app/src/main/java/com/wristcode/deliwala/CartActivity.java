package com.wristcode.deliwala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wristcode.deliwala.Pojo.Cart;
import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.adapter.CartAdapter;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager HorizontalLayout;
    CartAdapter adapter;
    TextView txtresname, txttitle, txtsubtotal, valsubtotal, txtdelivery, valdelivery, txttotal, valtotal;
    EditText txtinstruction;
    Button placeorder, btnordernow;
    ExampleDBHelper dh;
    List<Cart> data = new ArrayList<>();
    int grandTotal;
    SharedPreferences pref;
    String rname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dh = new ExampleDBHelper(CartActivity.this);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        txttitle = findViewById(R.id.txttitle);
        txtresname  = findViewById(R.id.txtresname);
        txtsubtotal = findViewById(R.id.txtsubtotal);
        valsubtotal = findViewById(R.id.valsubtotal);
        txtdelivery = findViewById(R.id.txtdelivery);
        valdelivery = findViewById(R.id.valdelivery);
        txttotal = findViewById(R.id.txttotal);
        valtotal = findViewById(R.id.valtotal);
        txtinstruction = findViewById(R.id.txtinstruction);
        placeorder = findViewById(R.id.placeorder);
        btnordernow = findViewById(R.id.btnordernow);

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");

        txttitle.setTypeface(font);
        txtresname.setTypeface(font);
        txtsubtotal.setTypeface(font);
        valsubtotal.setTypeface(font1);
        txtdelivery.setTypeface(font);
        valdelivery.setTypeface(font1);
        txttotal.setTypeface(font);
        valtotal.setTypeface(font1);
        txtinstruction.setTypeface(font1);
        placeorder.setTypeface(font1);

        if (dh.gettotalprice() <= 0)
        {
            setContentView(R.layout.activity_empty_cart);
        }
        else
        {
            Cursor c = dh.getAllItems();
            while (c.moveToNext())
            {
                Cart fishData = new Cart();
                fishData.resid = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                rname = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                fishData.resname = rname;
                fishData.name = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                fishData.qty = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                fishData.price = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                fishData.varid = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID));
                fishData.varname = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARNAME));
                fishData.id = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ID));
                fishData.type = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                data.add(fishData);
            }
            txtresname.setText(rname.toString());
            valsubtotal.setText("₹ " + dh.gettotalprice());
            grandTotal = (dh.gettotalprice());
            valtotal.setText("₹ " + grandTotal);
            recyclerView = findViewById(R.id.recyclerCart);
            adapter = new CartAdapter(CartActivity.this, data);
            HorizontalLayout = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, AddAddressActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void orderNow(View v)
    {
        SharedPreferences.Editor editor1 = pref.edit();
        editor1.putString("fg","0");
        editor1.apply();
        Intent i = new Intent(CartActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }

    public void backButton(View v) {
        Intent i = new Intent(CartActivity.this, HotelActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CartActivity.this, HotelActivity.class);
        startActivity(i);
        finish();
    }

    public void updateCartDetails()
    {
        if (dh.gettotalprice() <= 0)
        {
            setContentView(R.layout.activity_empty_cart);
        }
        else
        {
            List<Cart> dataplus = new ArrayList<>();
            Cursor c = dh.getAllItems();
            while (c.moveToNext())
            {
                Cart fishData = new Cart();
                fishData.resid = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESID));
                rname = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_RESNAME));
                fishData.resname = rname;
                fishData.name = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_NAME));
                fishData.qty = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_QUANTITY));
                fishData.price = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_PRICE));
                fishData.varid = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARID));
                fishData.varname = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_VARNAME));
                fishData.id = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_ID));
                fishData.type = c.getString(c.getColumnIndex(ExampleDBHelper.SUBCAT_COLUMN_IMAGE));
                dataplus.add(fishData);
            }
            txtresname.setText(rname.toString());
            valsubtotal.setText("₹ " + dh.gettotalprice());
            grandTotal = (dh.gettotalprice());
            valtotal.setText("₹ " + grandTotal);
            recyclerView = findViewById(R.id.recyclerCart);
            adapter = new CartAdapter(CartActivity.this, dataplus);
            HorizontalLayout = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}