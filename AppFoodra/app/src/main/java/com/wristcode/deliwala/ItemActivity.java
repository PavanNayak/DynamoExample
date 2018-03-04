package com.wristcode.deliwala;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity
{
    TextView txthotelname,txtprice,txtdesc, txtitemqty;
    Button btnminus, btnplus, btnadd, proceedcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        txthotelname=(TextView)findViewById(R.id.txthotelname);
        txtprice=(TextView)findViewById(R.id.txtprice);
        txtdesc=(TextView)findViewById(R.id.txtdesc);
        txtitemqty = (TextView) findViewById(R.id.txtitemqty);
        proceedcart = (Button) findViewById(R.id.proceedcart);
        btnminus = (Button) findViewById(R.id.btnminus);
        btnplus = (Button) findViewById(R.id.btnplus);
        btnadd = (Button) findViewById(R.id.btnadd);

        Typeface font = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "GT-Walsheim-Regular.ttf");


        txthotelname.setTypeface(font2);
        txtprice.setTypeface(font2);
        txtdesc.setTypeface(font2);
        txtitemqty.setTypeface(font2);
        btnadd.setTypeface(font2);
        proceedcart.setTypeface(font2);

        btnadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnadd.setVisibility(View.GONE);
                btnminus.setVisibility(View.VISIBLE);
                txtitemqty.setVisibility(View.VISIBLE);
                btnplus.setVisibility(View.VISIBLE);
            }
        });

        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(txtitemqty.getText().toString().equals("1"))
                {
                    btnminus.setVisibility(View.GONE);
                    btnplus.setVisibility(View.GONE);
                    txtitemqty.setVisibility(View.GONE);
                    btnadd.setVisibility(View.VISIBLE);
                }
                else
                {
                    int i = Integer.parseInt(txtitemqty.getText().toString());
                    i--;
                    txtitemqty.setText(String.valueOf(i));
                }
            }
        });

        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int i = Integer.parseInt(txtitemqty.getText().toString());
                i++;
                txtitemqty.setText(String.valueOf(i));
            }
        });

        proceedcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ItemActivity.this,CartActivity.class);
                startActivity(i);
            }
        });


    }
}
