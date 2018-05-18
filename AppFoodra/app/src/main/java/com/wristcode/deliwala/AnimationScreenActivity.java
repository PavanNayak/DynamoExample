package com.wristcode.deliwala;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnimationScreenActivity extends AppCompatActivity
{
    Animation animSlideDown;
    int i=0;
    List<String> str;
    TextView txt;
    TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_screen);
        str = new ArrayList<>();
        str.add("Hungry?");
        str.add("Unexpected Guests?");
        str.add("Game night?");
        str.add("Cooking gone wrong?");
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        txt = findViewById(R.id.txt);
        txt1 = findViewById(R.id.txt1);
        Typeface font2 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");
        txt.setTypeface(font2);
        txt1.setTypeface(font2);
        animation(2000);
    }

    public void animation(int i)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                txt.setText(str.get(0));
                txt.startAnimation(animSlideDown);
            }
        }, i);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                txt.setText(str.get(1));
                txt.startAnimation(animSlideDown);
            }
        }, 4000);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                txt.setText(str.get(2));
                txt.startAnimation(animSlideDown);
            }
        }, 6000);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                txt.setText(str.get(3));
                txt.startAnimation(animSlideDown);
            }
        }, 8000);
    }

    public void onClickLogin(View v)
    {
        Intent i =new Intent(AnimationScreenActivity.this,LoginActivity.class);
        startActivity(i);
    }
}
