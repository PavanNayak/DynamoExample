package com.wristcode.deliwala.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wristcode.deliwala.R;

public class OverviewFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String id,name,descp,img,isOpen,pop,address;
    private String mParam1;
    private String mParam2;

    CollapsingToolbarLayout image;
    TextView txthotelname,txtlocation,txtdesc,txttime,txtdistance,txtdelivery,fab;
    private OnFragmentInteractionListener mListener;
    SharedPreferences pref;

    public OverviewFragment() {}

    @SuppressLint("ValidFragment")
    public OverviewFragment(String id, String name, String descp, String img, String isOpen, String pop, String address)
    {
        this.id=id;
        this.name=name;
        this.descp=descp;
        this.img=img;
        this.isOpen=isOpen;
        this.pop=pop;
        this.address=address;
    }

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_overview, container, false);
        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        txthotelname = v.findViewById(R.id.txthotelname);
        txtlocation = v.findViewById(R.id.txtlocation);
        txtdesc = v.findViewById(R.id.txtdesc);
        txttime = v.findViewById(R.id.txttime);
        txtdistance = v.findViewById(R.id.txtdistance);
        txtdelivery = v.findViewById(R.id.txtdelivery);
        fab = v.findViewById(R.id.fab);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Regular.ttf");

        txthotelname.setTypeface(font2);
        txtlocation.setTypeface(font2);
        txtdesc.setTypeface(font2);
        txttime.setTypeface(font2);
        txtdistance.setTypeface(font2);
        txtdelivery.setTypeface(font2);

        txthotelname.setText(pref.getString("name", "").toString());
        txtlocation.setText(pref.getString("address", "").toString());
        fab.setText(String.format("%.1f", Float.valueOf(pref.getString("pop", "").toString())));
        txtdesc.setText(pref.getString("descp", "").toString());
        txtdistance.setText(String.format("%.2f", Float.valueOf(pref.getString("distance", "").toString()))+" km");

        if(pref.getString("isOpen", "").toString().equals("true"))
        {
            txttime.setText("Open Now");
        }
        else {
            txttime.setText("Closed");
        }
        //txtdesc.setText(activity.descp);
        //txtdesc.setText(descp);

        image = (CollapsingToolbarLayout) v.findViewById(R.id.toolbar_layout);
       // image.setBackgroundResource(Integer.parseInt(img));


//        Toolbar toolbar = v.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).setTitle(null);
        return v;
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

    @Override
    public void onResume() {
        super.onResume();
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
