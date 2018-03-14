package com.wristcode.deliwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.wristcode.deliwala.adapter.MenuAdapters;
import com.wristcode.deliwala.fragments.HomeFragment;
import com.wristcode.deliwala.fragments.NotificationFragment;
import com.wristcode.deliwala.fragments.ProfileFragment;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

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
    private GoogleApiClient mGoogleApiClient;
    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();
    Animation animSlideDown,animSlideUp,animSlideDown1,animSlideDown2;
    TextView txttitle;
    ExampleDBHelper dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        dh = new ExampleDBHelper(NavDrawer.this);
        txttitle = findViewById(R.id.txttitle);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animSlideDown1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_downbg);
        animSlideDown2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.leftanim);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        txttitle.setTypeface(font);
        mViewHolder = new ViewHolder();

        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new HomeFragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
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

    private void goToFragment(Fragment fragment, boolean addToBackStack)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack)
        {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0:
                goToFragment(new HomeFragment(), false);
                break;
            case 1:
                Intent i = new Intent(NavDrawer.this, OrderHistoryActivity.class);
                startActivity(i);
                finish();
                break;
            case 2:
                Intent j = new Intent(NavDrawer.this, CartActivity.class);
                startActivity(j);
                finish();
                break;
            case 3:
                goToFragment(new NotificationFragment(),false);
                break;
            case 4:
                goToFragment(new ProfileFragment(),false);
                break;
            case 5:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
                {
                    @Override
                    public void onResult(Status status) {
                        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(NavDrawer.this);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putString("flag", "0");
                        editor1.apply();
                        dh.deleteAllData();
                        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NavDrawer.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                break;
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
            mDuoDrawerLayout.closeDrawer();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
