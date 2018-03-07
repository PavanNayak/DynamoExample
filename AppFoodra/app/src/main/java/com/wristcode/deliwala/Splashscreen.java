package com.wristcode.deliwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Splashscreen extends AppCompatActivity {
    Animation animSlideDown;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        imageView = (ImageView) findViewById(R.id.imageView);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        imageView.startAnimation(animSlideDown);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Splashscreen.this);
                if(preferences.getString("flag", "").toString().equals("1"))
                {
                    Intent i = new Intent(Splashscreen.this, NavDrawer.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(Splashscreen.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);
    }
}