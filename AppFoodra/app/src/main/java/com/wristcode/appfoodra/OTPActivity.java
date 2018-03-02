package com.wristcode.appfoodra;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ajay Jagadish on 26-Feb-18.
 */

public class OTPActivity extends AppCompatActivity
{
    TextView tvtitle, tvsubtitle, tvotp;
    EditText valueotp;
    Button btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        tvtitle = (TextView)findViewById(R.id.tvtitle);
        tvsubtitle = (TextView)findViewById(R.id.tvsubtitle);
        tvotp = (TextView) findViewById(R.id.txtotp);
        valueotp = (EditText)findViewById(R.id.valueotp);
        btn_verify = (Button)findViewById(R.id.btn_verify);

        Typeface font = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");

        tvtitle.setTypeface(font);
        tvsubtitle.setTypeface(font2);
        tvotp.setTypeface(font2);
        valueotp.setTypeface(font2);
        btn_verify.setTypeface(font2);

        btn_verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(OTPActivity.this, NavDrawer.class);
                startActivity(i);
                finish();
            }
        });
    }
}