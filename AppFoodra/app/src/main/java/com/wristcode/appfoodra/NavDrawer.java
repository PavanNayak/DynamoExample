package com.wristcode.appfoodra;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.wristcode.appfoodra.adapter.MenuAdapters;
import com.wristcode.appfoodra.fragments.HomeFragment;

import java.util.ArrayList;
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

/**
 * Created by Ajay Jagadish on 27-Feb-18.
 */

public class NavDrawer extends AppCompatActivity implements DuoMenuView.OnMenuClickListener
{
    private MenuAdapters mMenuAdapter;

    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();
    Animation animSlideDown,animSlideUp,animSlideDown1,animSlideDown2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animSlideDown1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_downbg);
        animSlideDown2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.leftanim);
        mViewHolder = new ViewHolder();

        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new HomeFragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
    }
    private void handleToolbar() {
        //   setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer()
    {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this, mViewHolder.mDuoDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
    }

    private void handleMenu()
    {
        mMenuAdapter = new MenuAdapters(mTitles);
        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            default:
                goToFragment(new HomeFragment(), false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        public DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        // private Toolbar mToolbar;
        ImageView menu;
        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            //mToolbar = (Toolbar) findViewById(R.id.toolbar);
            menu=(ImageView)findViewById(R.id.menu);
            //  mDuoDrawerLayout.closeDrawer();
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mDuoDrawerLayout.isDrawerOpen()) {
                        mDuoDrawerLayout.closeDrawer();
                    }
                    else {
                        mDuoDrawerLayout.openDrawer();
                    }
                }
            });
            mDuoDrawerLayout.openDrawer();
        }
    }


    public void onClickNext(View v){

//        Intent i =new Intent(NavDrawer.this,VehicleListActivity.class);
//        startActivity(i);
    }


}
