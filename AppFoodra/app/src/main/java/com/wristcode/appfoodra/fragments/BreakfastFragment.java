package com.wristcode.appfoodra.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wristcode.appfoodra.Pojo.Items;
import com.wristcode.appfoodra.R;
import com.wristcode.appfoodra.adapter.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 28-Feb-18.
 */

public class BreakfastFragment extends Fragment
{
    RecyclerView recyclerMenu;
    private List<Items> categoriesList;
    ItemsAdapter adapter;

    public BreakfastFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        recyclerMenu = v.findViewById(R.id.recyclerMenu);
        categoriesList = new ArrayList<>();
        prepareAlbums();
        return v;
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.upma,
                R.drawable.chapatti,
                R.drawable.chapatti
        };

        Items a = new Items("Upma", "Tasty Rava Upma mixed with Tomato and Onion ", covers[0], "₹ 50");
        categoriesList.add(a);
        a = new Items("Idli Vada", "Piece of two idli with sambar and one vada", covers[1], "₹ 70");
        categoriesList.add(a);
        a = new Items("Chapati", "Two chapathis with kurma and onions", covers[2], "₹ 10");
        categoriesList.add(a);

        adapter = new ItemsAdapter(getActivity(), categoriesList);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMenu.setNestedScrollingEnabled(false);
        recyclerMenu.setFocusable(false);
        recyclerMenu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
