package com.wristcode.deliwala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wristcode.deliwala.sqlite.ExampleDBHelper;

/**
 * Created by Ajay Jagadish on 26-Feb-18.
 */

public class OrderConfirmActivity extends AppCompatActivity
{
    TextView txtorder, txtorder1;
    Button trackorder;
    ExampleDBHelper dh;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        txtorder = (TextView) findViewById(R.id.txtorder);
        txtorder1 = (TextView) findViewById(R.id.txtorder1);
        trackorder = (Button) findViewById(R.id.trackorder);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        dh = new ExampleDBHelper(OrderConfirmActivity.this);
        Typeface font1 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");
        txtorder.setTypeface(font1);
        txtorder1.setTypeface(font2);
        trackorder.setTypeface(font2);

        trackorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dh.deleteAllData();
                SharedPreferences.Editor editor1 = pref.edit();
                editor1.putString("fg","0");
                editor1.apply();
                Intent i = new Intent(OrderConfirmActivity.this, OrderHistoryActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        //Do nothing
    }
}
