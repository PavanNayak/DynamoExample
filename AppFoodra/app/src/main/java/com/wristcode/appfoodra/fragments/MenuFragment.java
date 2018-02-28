package com.wristcode.appfoodra.fragments;

import android.content.Context;
import android.net.Uri;
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

public class MenuFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    ItemsAdapter adapter;
    private List<Items> categoriesList;
    private List<Items> categoriesList1;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerMenu);
        recyclerView1 = (RecyclerView) v.findViewById(R.id.recyclerMenu1);
        categoriesList = new ArrayList<>();
        categoriesList1 = new ArrayList<>();

        prepareAlbums();
        prepareAlbums1();
        return v;
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.chickenb,
                R.drawable.muttonb,
                R.drawable.eggb,
                R.drawable.chickenb
        };

        Items a = new Items("Chicken Biriyani", "Spicy Chicken Biriyani flavoured with Rice ", covers[0], "₹ 150");
        categoriesList.add(a);

        a = new Items("Mutton Biriyani", "Spicy Mutton Biriyani flavoured with Rice ", covers[1], " ₹170");
        categoriesList.add(a);
        a = new Items("Egg Biriyani", "Spicy Egg Biriyani flavoured with Rice ", covers[2], "₹ 100");
        categoriesList.add(a);

        adapter = new ItemsAdapter(getActivity(), categoriesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void prepareAlbums1() {
        int[] covers = new int[]{
                R.drawable.upma,
                R.drawable.chapatti,
                R.drawable.chapatti
        };

        Items a = new Items("Upma", "Tasty Rava Upma mixed with Tomato and Onion ", covers[0], "₹ 50");
        categoriesList1.add(a);
        a = new Items("Idli Vada", "Piece of two idli with sambar and one vada", covers[1], "₹ 70");
        categoriesList1.add(a);
        a = new Items("Chapati", "Two chapathis with the kurma and onions", covers[2], "₹ 10");
        categoriesList1.add(a);

        adapter = new ItemsAdapter(getActivity(), categoriesList1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView1.setFocusable(false);
        recyclerView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}