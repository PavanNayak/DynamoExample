package com.wristcode.appfoodra;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wristcode.appfoodra.R;

/**
 * Created by Ajay Jagadish on 20-Feb-18.
 */

public class LoginActivity extends AppCompatActivity
{
    TextView txtwelcome, txtsignin, emailtxt, passtxt, txtforgotpass, txtnewuser;
    EditText txtemail, txtpassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtwelcome = (TextView) findViewById(R.id.txtwelcome);
        txtsignin = (TextView) findViewById(R.id.txtsignin);
        emailtxt = (TextView) findViewById(R.id.emailtxt);
        txtemail = (EditText) findViewById(R.id.txtemail);
        passtxt = (TextView) findViewById(R.id.passtxt);
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        txtforgotpass = (TextView) findViewById(R.id.txtforgotpass);
        txtnewuser = (TextView) findViewById(R.id.txtnewuser);
        loginButton = (Button) findViewById(R.id.loginButton);
        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");
        txtwelcome.setTypeface(font);
        txtsignin.setTypeface(font1);
        emailtxt.setTypeface(font1);
        txtemail.setTypeface(font1);
        passtxt.setTypeface(font1);
        txtpassword.setTypeface(font1);
        txtforgotpass.setTypeface(font1);
        txtnewuser.setTypeface(font1);
        loginButton.setTypeface(font1);
    }

    public void loginButton(View v)
    {
        Intent i =new Intent(LoginActivity.this, HotelListActivity.class);
        startActivity(i);
        finish();
    }
}
