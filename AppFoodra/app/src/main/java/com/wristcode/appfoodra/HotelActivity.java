package com.wristcode.appfoodra;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wristcode.appfoodra.fragments.MenuFragment;
import com.wristcode.appfoodra.fragments.OverviewFragment;

public class HotelActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private MenuFragment fragment;
    private OverviewFragment fragment1;
    private OverviewFragment fragment3;
    int flag=0;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.overview:
                    fragment1 = new OverviewFragment();
                    flag = 0;
                    //   mTextMessage.setText(R.string.title_home);
                    break;
                case R.id.menu:
                    fragment = new MenuFragment();
                    flag = 1;
                    break;
                case R.id.reviews:
                    // mTextMessage.setText(R.string.title_notifications);
                    break;
            }

            if (flag == 0) {
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment1).commit();

            } else if (flag == 1) {
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment).commit();

            }


        return true;
    }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        fragmentManager = getSupportFragmentManager();
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        fragment3=new OverviewFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment3).commit();



    }

}
