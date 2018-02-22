package com.wristcode.appfoodra;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {


    TextView txthotelname,txtprice,txtdesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        txthotelname=(TextView)findViewById(R.id.txthotelname);
        txtprice=(TextView)findViewById(R.id.txtprice);
        txtdesc=(TextView)findViewById(R.id.txtdesc);

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");


        txthotelname.setTypeface(font2);
        txtprice.setTypeface(font2);
        txtdesc.setTypeface(font2);


    }

    public void onClickCart(View v)
    {
        Intent i =new Intent(ItemActivity.this,CartActivity.class);
        startActivity(i);
    }
}
