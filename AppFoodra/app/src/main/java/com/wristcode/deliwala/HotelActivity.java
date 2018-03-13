package com.wristcode.deliwala;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.wristcode.deliwala.fragments.MenuFragment;
import com.wristcode.deliwala.fragments.OverviewFragment;
import com.wristcode.deliwala.fragments.ReviewsFragment;

public class HotelActivity extends AppCompatActivity
{
    private OverviewFragment fragment1;
    private MenuFragment fragment2;
    private ReviewsFragment fragment3;
    int flag = 0;
    private FragmentManager fragmentManager;

    private SpaceNavigationView spaceNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        setupNavigationView();


        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("OVERVIEW",0));
        spaceNavigationView.addSpaceItem(new SpaceItem("REVIEWS",0));
        spaceNavigationView.shouldShowFullBadgeText(true);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                spaceNavigationView.shouldShowFullBadgeText(true);
                pushFragment(new MenuFragment());

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                if(itemIndex==0){
                    pushFragment(new OverviewFragment());
                }
                else {

                    pushFragment(new ReviewsFragment());
                }
                Log.d("onItemClick ", "" + itemIndex + " " + itemName);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
                if(itemIndex==0){
                    pushFragment(new OverviewFragment());
                }
                else {

                    pushFragment(new ReviewsFragment());
                }
            }
        });

        spaceNavigationView.setSpaceOnLongClickListener(new SpaceOnLongClickListener() {
            @Override
            public void onCentreButtonLongClick() {
                Toast.makeText(HotelActivity.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(HotelActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupNavigationView()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
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
            if (ft != null) {
                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        }
    }

    public void onClickCart(View v) {
        Intent i = new Intent(HotelActivity.this, CartActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HotelActivity.this, NavDrawer.class);
        startActivity(i);
        finish();
    }
}