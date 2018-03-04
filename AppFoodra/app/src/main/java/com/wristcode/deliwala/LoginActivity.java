package com.wristcode.deliwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wristcode.deliwala.extra.CustomVolleyRequest;

/**
 * Created by Ajay Jagadish on 20-Feb-18.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    TextView txtwelcome, txtsignin, txtphone;
    EditText valuephone;
    Button verifyButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signIn();
    }

    private static final int RC_SIGN_IN = 9001;

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == LoginActivity.RESULT_OK)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor1 = preferences1.edit();
            editor1.putString("flag", "1");
            editor1.apply();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor = pref.edit();
            String name = acct.getDisplayName().toString();
            name = name.replaceAll(" ", "_");
            editor.putString("name", name.toString());
            editor.putString("email", acct.getEmail().toString().trim());
            editor.putString("Profile", acct.getPhotoUrl().toString());
            editor.apply();
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public void verifyButton(View v)
    {
        if (valuephone.getText().toString().matches(""))
        {
            Toast.makeText(LoginActivity.this, "You must enter your mobile number!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i = new Intent(LoginActivity.this, OTPActivity.class);
            startActivity(i);
            finish();
        }
    }
}
