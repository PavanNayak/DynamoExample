package com.wristcode.deliwala;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wristcode.deliwala.Pojo.Category;
import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.adapter.CategoryAdapter;
import com.wristcode.deliwala.adapter.OffersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 26-Feb-18.
 */

public class HomeActivity extends AppCompatActivity
{
    TextView text1,text2;
    RecyclerView menurecycler, offerrecycler;
    private List<Category> categoriesList;
    private List<Restaurants> categoriesList1;
    CategoryAdapter adapter;
    OffersAdapter adapter1;
    LinearLayoutManager HorizontalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        Typeface font1 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");
        text1.setTypeface(font1);
        text2.setTypeface(font2);
        menurecycler = (RecyclerView) findViewById(R.id.menurecycler);
        categoriesList = new ArrayList<>();
        prepareAlbums();
        offerrecycler = (RecyclerView) findViewById(R.id.offerrecycler);
        categoriesList1 = new ArrayList<>();
        prepareAlbums1();
    }

    private void prepareAlbums()
    {
        int[] covers = new int[]
                {
                        R.drawable.noodles,
                        R.drawable.taco,
                        R.drawable.hamburger,
                        R.drawable.donut,
                        R.drawable.pizza
                };

        Category a = new Category("", "CHINESE", covers[0]);
        categoriesList.add(a);
        a = new Category("", "TACOS", covers[1]);
        categoriesList.add(a);
        a = new Category("", "BURGER", covers[2]);
        categoriesList.add(a);
        a = new Category("", "DONUT", covers[3]);
        categoriesList.add(a);
        a = new Category("", "PIZZA", covers[4]);
        categoriesList.add(a);

        adapter = new CategoryAdapter(HomeActivity.this, categoriesList);
        menurecycler.setAdapter(adapter);
        HorizontalLayout = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        menurecycler.setLayoutManager(HorizontalLayout);
        adapter.notifyDataSetChanged();
    }

    private void prepareAlbums1()
    {
        int[] covers = new int[]
                {
                        R.drawable.hotel,
                        R.drawable.hotel1,
                        R.drawable.hotel2,
                        R.drawable.hotel
                };

        Restaurants a = new Restaurants("Spice n Ice","Chinese, Italian, Arabian",covers[0],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a =   new Restaurants("Hot n Spicy","Chinese, Italian, Arabian",covers[1],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a = new Restaurants("Mexican Burrito","Chinese, Italian, Arabian",covers[2],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a = new Restaurants("Spice n Ice","Chinese, Italian, Arabian",covers[3],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a = new Restaurants("Spice n Ice","Chinese, Italian, Arabian",covers[0],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);

        adapter1 = new OffersAdapter(HomeActivity.this, categoriesList1);
        offerrecycler.setFocusable(false);
        offerrecycler.setAdapter(adapter1);
        offerrecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter1.notifyDataSetChanged();

    }
}
