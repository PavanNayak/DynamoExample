package com.wristcode.appfoodra;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wristcode.appfoodra.Pojo.Category;
import com.wristcode.appfoodra.Pojo.Offers;
import com.wristcode.appfoodra.Pojo.Payment;
import com.wristcode.appfoodra.adapter.CategoryAdapter;
import com.wristcode.appfoodra.adapter.OffersAdapter;
import com.wristcode.appfoodra.adapter.PaymentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 01-Mar-18.
 */

public class PaymentActivity extends AppCompatActivity
{
    RecyclerView recyclerpayment;
    private List<Payment> paymentList;
    PaymentAdapter adapter;
    LinearLayoutManager HorizontalLayout;
    TextView txttotal, valuetotal, txtpromo;
    EditText valuepromo;
    Button pay, applypromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        txttotal = (TextView) findViewById(R.id.txttotal);
        valuetotal = (TextView) findViewById(R.id.valuetotal);
        txtpromo = (TextView) findViewById(R.id.txtpromo);
        valuepromo = (EditText) findViewById(R.id.valuepromo);
        applypromo = (Button) findViewById(R.id.applypromo);
        pay = (Button) findViewById(R.id.pay);
        Typeface font1 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(),"GT-Walsheim-Regular.ttf");
        txttotal.setTypeface(font1);
        valuetotal.setTypeface(font2);
        txtpromo.setTypeface(font1);
        valuepromo.setTypeface(font2);
        applypromo.setTypeface(font2);
        pay.setTypeface(font2);
        recyclerpayment = (RecyclerView) findViewById(R.id.recyclerpayment);
        paymentList = new ArrayList<>();
        prepareAlbums();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(PaymentActivity.this, OrderConfirmActivity.class);
                startActivity(i);
            }
        });
    }

    private void prepareAlbums()
    {
        int[] covers = new int[]
                {
                        R.drawable.cash,
                        R.drawable.card,
                        R.drawable.pickup,
                        R.drawable.dinein,
                };

        Payment a = new Payment("CASH ON DELIVERY", covers[0]);
        paymentList.add(a);
        a = new Payment("CARD", covers[1]);
        paymentList.add(a);
        a = new Payment("PICKUP", covers[2]);
        paymentList.add(a);
        a = new Payment("DINE IN", covers[3]);
        paymentList.add(a);

        adapter = new PaymentAdapter(PaymentActivity.this, paymentList);
        HorizontalLayout = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerpayment.setLayoutManager(HorizontalLayout);
        recyclerpayment.setNestedScrollingEnabled(false);
        recyclerpayment.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
