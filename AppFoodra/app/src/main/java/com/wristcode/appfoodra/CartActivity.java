package com.wristcode.appfoodra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wristcode.appfoodra.Pojo.Items;
import com.wristcode.appfoodra.adapter.CartAdapter;
import com.wristcode.appfoodra.adapter.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager HorizontalLayout;
    CartAdapter adapter;
    private List<Items> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCart);
        categoriesList = new ArrayList<>();
        prepareAlbums();
    }

    private void prepareAlbums() {
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

    public void onClickProceed(View v) {
        Intent i = new Intent(CartActivity.this, TrackActivity.class);
        startActivity(i);
    }
}