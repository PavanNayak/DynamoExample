package com.wristcode.deliwala.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.ItemsAdapter;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 28-Feb-18.
 */

public class MainDishesFragment extends Fragment
{
    RecyclerView recyclerMenu;
    private List<Items> categoriesList;
    ItemsAdapter adapter;
    String jsonString;
    TextView cart_badge;
    ExampleDBHelper dh;

    public MainDishesFragment() {}

    @SuppressLint("ValidFragment")
    public MainDishesFragment(String s) {

        this.jsonString = s;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        categoriesList = new ArrayList<>();
        try
        {
            JSONObject jobject = new JSONObject(jsonString);
            Items data = new Items();
            data.id = jobject.getString("id");
            data.name=jobject.getString("itemName");
            data.descp=jobject.getString("itemShortDescription");
            data.price=jobject.getString("regularPrice");
            categoriesList.add(data);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        recyclerMenu = v.findViewById(R.id.recyclerMenu);
        adapter = new ItemsAdapter(getActivity(), categoriesList, MainDishesFragment.this);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMenu.setNestedScrollingEnabled(false);
        recyclerMenu.setFocusable(false);
        recyclerMenu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }

    public void setCart(int item)
    {
        //cart_badge.setText(String.valueOf(item));
    }

    public void setPrice(int total)
    {
        //txtitemtotal.setText("₹" + String.valueOf(total));
    }
}
