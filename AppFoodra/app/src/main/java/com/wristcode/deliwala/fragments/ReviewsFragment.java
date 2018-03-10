package com.wristcode.deliwala.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wristcode.deliwala.CartActivity;
import com.wristcode.deliwala.HotelActivity;
import com.wristcode.deliwala.NavDrawer;
import com.wristcode.deliwala.Pojo.Reviews;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.ReviewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 28-Feb-18.
 */

public class ReviewsFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerReview;
    private List<Reviews> reviewsList;
    ReviewsAdapter adapter;

    public ReviewsFragment() {}

    public static ReviewsFragment newInstance(String param1, String param2)
    {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);
        recyclerReview = v.findViewById(R.id.recyclerReview);
        reviewsList = new ArrayList<>();
        prepareAlbums();
        return v;
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.userimg,
                R.drawable.userimg,
                R.drawable.userimg,
                R.drawable.userimg
        };

        Reviews a = new Reviews("", "Pavan Nayak", "Loved the restaurant!!!", covers[0]);
        reviewsList.add(a);
        a = new Reviews("", "Srikanth", "Tasty Food, Would love to visit again!!", covers[1]);
        reviewsList.add(a);
        a = new Reviews("", "Shailesh", "Good..", covers[2]);
        reviewsList.add(a);
        a = new Reviews("", "Prashanth", "Wonderful...", covers[3]);
        reviewsList.add(a);

        adapter = new ReviewsAdapter(getActivity(), reviewsList);
        recyclerReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerReview.setNestedScrollingEnabled(false);
        recyclerReview.setFocusable(false);
        recyclerReview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
