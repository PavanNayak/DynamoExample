package com.wristcode.deliwala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;

public class SplashscreenActivity extends AppCompatActivity
{
    Animation animSlideDown;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        imageView = findViewById(R.id.imageView);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        imageView.startAnimation(animSlideDown);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                SharedPreferences.Editor editor1 = preferences.edit();
                editor1.putString("tokenId",refreshedToken);
                editor1.apply();

                if(preferences.getString("flag", "").equals("1"))
                {
                    Intent i = new Intent(SplashscreenActivity.this, NavDrawer.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashscreenActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);
    }
}