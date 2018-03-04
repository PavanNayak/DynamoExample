package com.wristcode.deliwala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wristcode.deliwala.Pojo.Hotels;
import com.wristcode.deliwala.adapter.HotelAdapter;

import java.util.ArrayList;
import java.util.List;

public class HotelListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    HotelAdapter adapter;
    private List<Hotels> categoriesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerHotel);

        categoriesList = new ArrayList<>();

        prepareAlbums();
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Manipal");
        spinnerArray.add("Udupi");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner1);
        sItems.setAdapter(adapter);
    }




    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.hotel,
                R.drawable.hotel1,
                R.drawable.hotel2,
                R.drawable.hotel
        };

        Hotels a = new Hotels("Spice n Ice","Chineese, Italian, Arabian",covers[0],"4.1 km","10 AM - 12 AM");
        categoriesList.add(a);

        a =   new Hotels("Hot n Spicy","Chineese, Italian, Arabian",covers[1],"4.1 km","10 AM - 12 AM");
        categoriesList.add(a);
        a = new Hotels("Mexican Burrito","Chineese, Italian, Arabian",covers[2],"4.1 km","10 AM - 12 AM");
        categoriesList.add(a);

        a = new Hotels("Spice n Ice","Chineese, Italian, Arabian",covers[3],"4.1 km","10 AM - 12 AM");
        categoriesList.add(a);

        a = new Hotels("Spice n Ice","Chineese, Italian, Arabian",covers[0],"4.1 km","10 AM - 12 AM");
        categoriesList.add(a);

//        a = new Items("Veg Biriyani","Spicy Veg Biriyani flavoured with Rice ",covers[3]);
//        categoriesList.add(a);
//
//
//        a =  new Items("Mutton Biriyani","Spicy Mutton Biriyani flavoured with Rice ",covers[1]);
//        categoriesList.add(a);






        adapter=new HotelAdapter(this,categoriesList);

        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();








    }




}
