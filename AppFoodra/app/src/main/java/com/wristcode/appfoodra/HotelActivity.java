package com.wristcode.appfoodra;

import android.content.Intent;
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
import android.widget.TextView;

import com.wristcode.appfoodra.fragments.MenuFragment;
import com.wristcode.appfoodra.fragments.OverviewFragment;
import com.wristcode.appfoodra.fragments.ReviewsFragment;

public class HotelActivity extends AppCompatActivity
{
    private OverviewFragment fragment1;
    private MenuFragment fragment2;
    private ReviewsFragment fragment3;
    int flag = 0;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        setupNavigationView();
    }

    private void setupNavigationView()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if (bottomNavigationView != null)
        {
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));
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
            if (ft != null) {
                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        }
    }

    public void onClickCart(View v) {
        Intent i = new Intent(HotelActivity.this, CartActivity.class);
        startActivity(i);
    }
}