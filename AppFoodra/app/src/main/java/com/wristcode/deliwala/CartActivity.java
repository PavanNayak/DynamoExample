package com.wristcode.deliwala;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager HorizontalLayout;
    CartAdapter adapter;
    private List<Items> categoriesList;
    TextView txttitle, txtsubtotal, valsubtotal, txtdelivery, valdelivery, txtdeltip, valdeltip, txttotal, valtotal;
    Button placeorder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txttitle = (TextView) findViewById(R.id.txttitle);
        txtsubtotal = (TextView) findViewById(R.id.txtsubtotal);
        valsubtotal = (TextView) findViewById(R.id.valsubtotal);
        txtdelivery = (TextView) findViewById(R.id.txtdelivery);
        valdelivery = (TextView) findViewById(R.id.valdelivery);
        txtdeltip = (TextView) findViewById(R.id.txtdeltip);
        valdeltip = (TextView) findViewById(R.id.valdeltip);
        txttotal = (TextView) findViewById(R.id.txttotal);
        valtotal = (TextView) findViewById(R.id.valtotal);
        placeorder = (Button) findViewById(R.id.placeorder);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txttitle.setTypeface(font);
        txtsubtotal.setTypeface(font);
        valsubtotal.setTypeface(font1);
        txtdelivery.setTypeface(font);
        valdelivery.setTypeface(font1);
        txtdeltip.setTypeface(font);
        valdeltip.setTypeface(font1);
        txttotal.setTypeface(font);
        valtotal.setTypeface(font1);
        placeorder.setTypeface(font1);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCart);
        categoriesList = new ArrayList<>();
        prepareAlbums();

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(i);
            }
        });
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

        Items a = new Items("1", "Chicken Biriyani", "₹ 150", "", covers[0]);
        categoriesList.add(a);
        a = new Items("2", "Mutton Biriyani", "₹170", "", covers[1]);
        categoriesList.add(a);
        a = new Items("3", "Egg Biriyani", "₹100", "", covers[2]);
        categoriesList.add(a);

        adapter = new CartAdapter(CartActivity.this, categoriesList);
        HorizontalLayout = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}