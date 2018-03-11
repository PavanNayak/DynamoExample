package com.wristcode.deliwala;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ajay Jagadish on 26-Feb-18.
 */

public class OrderConfirmActivity extends AppCompatActivity
{
    TextView txtorder, txtorder1;
    Button trackorder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        txtorder = (TextView) findViewById(R.id.txtorder);
        txtorder1 = (TextView) findViewById(R.id.txtorder1);
        trackorder = (Button) findViewById(R.id.trackorder);
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
                Intent i = new Intent(OrderConfirmActivity.this, OrderHistoryActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OrderConfirmActivity.this, OrderConfirmActivity.class);
        startActivity(i);
        finish();
    }
}
