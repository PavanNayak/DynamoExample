package com.wristcode.deliwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

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
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                SharedPreferences.Editor editor1 = preferences.edit();
                editor1.putString("tokenId",refreshedToken);
                editor1.apply();
                Log.d("tokenId",refreshedToken);

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