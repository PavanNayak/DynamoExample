package com.wristcode.deliwala.fragments;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wristcode.deliwala.Pojo.Category;
import com.wristcode.deliwala.Pojo.Offers;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.CategoryAdapter;
import com.wristcode.deliwala.adapter.OffersAdapter;

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
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView text1,text2;
    RecyclerView menurecycler, offerrecycler;
    private List<Category> categoriesList;
    private List<Offers> categoriesList1;
    CategoryAdapter adapter;
    OffersAdapter adapter1;
    LinearLayoutManager HorizontalLayout;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        text1 = (TextView) v.findViewById(R.id.text1);
        text2 = (TextView) v.findViewById(R.id.text2);
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(),"GT-Walsheim-Regular.ttf");
        text1.setTypeface(font1);
        text2.setTypeface(font2);
        menurecycler = v.findViewById(R.id.menurecycler);
        categoriesList = new ArrayList<>();
        prepareAlbums();
        offerrecycler = v.findViewById(R.id.offerrecycler);
        categoriesList1 = new ArrayList<>();
        prepareAlbums1();
        return v;
    }

    private void prepareAlbums()
    {
        int[] covers = new int[]
                {
                        R.drawable.noodles,
                        R.drawable.taco,
                        R.drawable.hamburger,
                        R.drawable.donut,
                        R.drawable.pizza
                };

        Category a = new Category("", "CHINESE", covers[0]);
        categoriesList.add(a);
        a = new Category("", "TACOS", covers[1]);
        categoriesList.add(a);
        a = new Category("", "BURGER", covers[2]);
        categoriesList.add(a);
        a = new Category("", "DONUT", covers[3]);
        categoriesList.add(a);
        a = new Category("", "PIZZA", covers[4]);
        categoriesList.add(a);

        adapter = new CategoryAdapter(getActivity(), categoriesList);
        HorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        menurecycler.setLayoutManager(HorizontalLayout);
        menurecycler.setNestedScrollingEnabled(false);
        menurecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void prepareAlbums1()
    {
        int[] covers = new int[]
                {
                        R.drawable.hotel,
                        R.drawable.hotel1,
                        R.drawable.hotel2,
                        R.drawable.hotel
                };

        Offers a = new Offers("Spice n Ice","Chinese, Italian, Arabian",covers[0],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a =   new Offers("Hot n Spicy","Chinese, Italian, Arabian",covers[1],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a = new Offers("Mexican Burrito","Chinese, Italian, Arabian",covers[2],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a = new Offers("Spice n Ice","Chinese, Italian, Arabian",covers[3],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);
        a = new Offers("Spice n Ice","Chinese, Italian, Arabian",covers[0],"4.1 km","10 AM - 12 AM");
        categoriesList1.add(a);

        adapter1 = new OffersAdapter(getActivity(), categoriesList1);
        offerrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        offerrecycler.setNestedScrollingEnabled(false);
        offerrecycler.setFocusable(false);
        offerrecycler.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
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
