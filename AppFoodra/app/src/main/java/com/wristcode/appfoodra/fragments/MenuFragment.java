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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    ItemsAdapter adapter;
    private List<Items> categoriesList;
    private List<Items> categoriesList1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerMenu);
        recyclerView1 = (RecyclerView)v.findViewById(R.id.recyclerMenu1);
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

        Items a = new Items("Chicken Biriyani","Spicy Chicken Biriyani flavoured with Rice ",covers[0],"₹ 150");
        categoriesList.add(a);

        a =  new Items("Mutton Biriyani","Spicy Mutton Biriyani flavoured with Rice ",covers[1]," ₹170");
        categoriesList.add(a);
        a = new Items("Egg Biriyani","Spicy Egg Biriyani flavoured with Rice ",covers[2],"₹ 100");
        categoriesList.add(a);

//        a = new Items("Veg Biriyani","Spicy Veg Biriyani flavoured with Rice ",covers[3]);
//        categoriesList.add(a);
//
//
//        a =  new Items("Mutton Biriyani","Spicy Mutton Biriyani flavoured with Rice ",covers[1]);
//        categoriesList.add(a);






        adapter=new ItemsAdapter(getActivity(),categoriesList);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();








    }



    private void prepareAlbums1() {
        int[] covers = new int[]{
                R.drawable.upma,
                R.drawable.chapatti,
                R.drawable.chapatti
        };

        Items a = new Items("Upma","Tasty Rava Upma mixed with Tomato and Onion ",covers[0],"₹ 50");
        categoriesList1.add(a);

        a =  new Items("Idli Vada","Piece of two idli with sambar and one vada",covers[1],"₹ 70");
        categoriesList1.add(a);
        a = new Items("Chapati","Two chapathis with the kurma and onions",covers[2],"₹ 10");
        categoriesList1.add(a);






        adapter=new ItemsAdapter(getActivity(),categoriesList1);


        recyclerView1.setAdapter(adapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();








    }



    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
