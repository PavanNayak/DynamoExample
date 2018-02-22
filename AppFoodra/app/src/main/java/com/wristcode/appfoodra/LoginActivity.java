package com.wristcode.appfoodra;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ajay Jagadish on 20-Feb-18.
 */

public class LoginActivity extends AppCompatActivity
{
    TextView txtwelcome, txtsignin, emailtxt, passtxt, txtforgotpass, txtnewuser;
    EditText txtemail, txtpassword;
    Button loginButton;
    ImageView hidepass;
    int flag = 0;

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
        hidepass = (ImageView) findViewById(R.id.hidepass);
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
        txtpassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        hidepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(flag == 0)
                {
                    txtpassword.setTransformationMethod(null);
                    hidepass.setImageResource(R.drawable.visible);
                    flag = 1;
                }
                else if(flag == 1)
                {
                    txtpassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    hidepass.setImageResource(R.drawable.invisible);
                    flag = 0;
                }
            }
        });
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

    public void loginButton(View v)
    {
        Intent i =new Intent(LoginActivity.this, HotelListActivity.class);
        startActivity(i);
        finish();
    }
}
