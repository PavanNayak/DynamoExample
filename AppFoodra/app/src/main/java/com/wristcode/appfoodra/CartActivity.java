package com.wristcode.appfoodra;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wristcode.appfoodra.Pojo.Items;
import com.wristcode.appfoodra.adapter.CartAdapter;
import com.wristcode.appfoodra.adapter.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager HorizontalLayout;
    CartAdapter adapter;
    private List<Items> categoriesList;
    TextView txtsubtotal, valsubtotal, txtdelivery, valdelivery, txtdeltip, valdeltip, txttotal, valtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtsubtotal = (TextView) findViewById(R.id.txtsubtotal);
        valsubtotal = (TextView) findViewById(R.id.valsubtotal);
        txtdelivery = (TextView) findViewById(R.id.txtdelivery);
        valdelivery = (TextView) findViewById(R.id.valdelivery);
        txtdeltip = (TextView) findViewById(R.id.txtdeltip);
        valdeltip = (TextView) findViewById(R.id.valdeltip);
        txttotal = (TextView) findViewById(R.id.txttotal);
        valtotal = (TextView) findViewById(R.id.valtotal);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txtsubtotal.setTypeface(font);
        valsubtotal.setTypeface(font1);
        txtdelivery.setTypeface(font);
        valdelivery.setTypeface(font1);
        txtdeltip.setTypeface(font);
        valdeltip.setTypeface(font1);
        txttotal.setTypeface(font);
        valtotal.setTypeface(font1);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCart);
        categoriesList = new ArrayList<>();
        prepareAlbums();
    }

    private void prepareAlbums()
    {
        int[] covers = new int[]
                {
                        R.drawable.chickenb,
                        R.drawable.muttonb,
                        R.drawable.eggb,
                        R.drawable.chickenb
                };

        Items a = new Items("Chicken Biriyani", "", covers[0], "₹ 150");
        categoriesList.add(a);
        a = new Items("Mutton Biriyani", "", covers[1], " ₹170");
        categoriesList.add(a);
        a = new Items("Egg Biriyani", "", covers[2], "₹ 100");
        categoriesList.add(a);

        adapter = new CartAdapter(CartActivity.this, categoriesList);
        recyclerView.setAdapter(adapter);
        HorizontalLayout = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        adapter.notifyDataSetChanged();
    }

    public void onClickProceed(View v)
    {
        Intent i = new Intent(CartActivity.this, OrderConfirmActivity.class);
        startActivity(i);
    }
}