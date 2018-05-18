package com.wristcode.deliwala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.luseen.spacenavigation.SpaceNavigationView;
import com.wristcode.deliwala.fragments.MenuFragment;
import com.wristcode.deliwala.fragments.OverviewFragment;
import com.wristcode.deliwala.fragments.ReviewsFragment;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

public class HotelActivity extends AppCompatActivity
{
    SharedPreferences preferences;
    public  String id, name, descp, img, isOpen, pop, address, distance;
    TextView txttitle, cartbadge;
    FrameLayout itemcart;
    ExampleDBHelper dh;
    ImageView imglogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        dh = new ExampleDBHelper(HotelActivity.this);
        preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        txttitle = findViewById(R.id.txttitle);
        cartbadge = findViewById(R.id.cartbadge);
        itemcart = findViewById(R.id.itemcart);
        imglogo = findViewById(R.id.imglogo);

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");

        if (dh.gettotalqty() > 0 || dh.gettotalprice() > 0)
        {
            cartbadge.setText(String.valueOf(dh.gettotalqty()));
        }
        else
        {
            cartbadge.setText("0");
        }
        setupNavigationView();

        id = preferences.getString("id", "").toString();
        name = preferences.getString("name", "").toString();
        descp = preferences.getString("descp", "").toString();
        img = preferences.getString("img", "").toString();
        isOpen = preferences.getString("isOpen", "").toString();
        pop = preferences.getString("pop", "").toString();
        address = preferences.getString("address", "").toString();
        distance = preferences.getString("distance", "").toString();

        txttitle.setText(preferences.getString("name", "").toString());
        txttitle.setTypeface(font);

        itemcart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(HotelActivity.this, CartActivity.class);
                startActivity(i);
                finish();
            }
        });

        imglogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(HotelActivity.this, NavDrawer.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setupNavigationView()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        if (bottomNavigationView != null)
        {
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(1));
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    selectFragment(item);
                    return false;
                }
            });
        }
    }

    protected void selectFragment(MenuItem item)
    {
        item.setChecked(true);
        switch (item.getItemId())
        {
            case R.id.overview:
                pushFragment(new OverviewFragment());
                break;
            case R.id.menu:
                pushFragment(new MenuFragment());
                break;
            case R.id.reviews:
                pushFragment(new ReviewsFragment());
                break;
        }
    }

    protected void pushFragment(Fragment fragment)
    {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null)
            {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("name", name);
                bundle.putString("nimg", img);
                bundle.putString("isOpen", isOpen);
                bundle.putString("pop", pop);
                bundle.putString("descp", descp);
                bundle.putString("address", address);
                fragment.setArguments(bundle);

                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        }
    }

    public void setCart(int item)
    {
        if (dh.gettotalqty() > 0 || dh.gettotalprice() > 0)
        {
            cartbadge.setText(String.valueOf(item));
        }
        else
        {
            cartbadge.setText("0");
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(HotelActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }
}