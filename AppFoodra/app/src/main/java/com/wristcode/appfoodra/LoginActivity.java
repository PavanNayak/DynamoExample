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
 * Created by Ajay Jagadish on 20-Feb-18.
 */

public class LoginActivity extends AppCompatActivity
{
    TextView txtwelcome, txtsignin, txtphone;
    EditText valuephone;
    Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtwelcome = (TextView) findViewById(R.id.txtwelcome);
        txtsignin = (TextView) findViewById(R.id.txtsignin);
        txtphone = (TextView) findViewById(R.id.txtphone);
        valuephone = (EditText) findViewById(R.id.valuephone);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txtwelcome.setTypeface(font);
        txtsignin.setTypeface(font1);
        txtphone.setTypeface(font1);
        valuephone.setTypeface(font1);
        verifyButton.setTypeface(font1);
    }

    public void verifyButton(View v)
    {
        Intent i = new Intent(LoginActivity.this, OTPActivity.class);
        startActivity(i);
        finish();
    }
}
