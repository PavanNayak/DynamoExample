package com.wristcode.deliwala.fragments;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.HotelActivity;
import com.wristcode.deliwala.R;

public class OverviewFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

   // HotelActivity activity;
    String id,name,descp,img,isOpen,pop,address;
    private String mParam1;
    private String mParam2;

    CollapsingToolbarLayout image;
    TextView txthotelname,txtlocation,txtdesc,txttime,txtdistance,txtdelivery,fab;
    private OnFragmentInteractionListener mListener;

    public OverviewFragment() {}

<<<<<<< HEAD
    @SuppressLint("ValidFragment")
    public OverviewFragment(String id,String name,String descp,String img,String isOpen,String pop,String address) {

        this.id=id;
        this.name=name;
        this.descp=descp;
        this.img=img;
        this.isOpen=isOpen;
        this.pop=pop;
        this.address=address;
    }




    public static OverviewFragment newInstance(String param1, String param2)
    {
=======
    public static OverviewFragment newInstance() {
>>>>>>> e3265697dd49481084099900bd5326d8163b2711
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
        txthotelname = v.findViewById(R.id.txthotelname);
        txtlocation = v.findViewById(R.id.txtlocation);
        txtdesc = v.findViewById(R.id.txtdesc);
        txttime = v.findViewById(R.id.txttime);
        txtdistance = v.findViewById(R.id.txtdistance);
        txtdelivery = v.findViewById(R.id.txtdelivery);
        fab = v.findViewById(R.id.fab);


       // activity=new HotelActivity();


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Regular.ttf");

        txthotelname.setTypeface(font2);
        txtlocation.setTypeface(font2);
        txtdesc.setTypeface(font2);
        txttime.setTypeface(font2);
        txtdistance.setTypeface(font2);
        txtdelivery.setTypeface(font2);

      //  Toast.makeText(getActivity(),String.valueOf(activity.name), Toast.LENGTH_SHORT).show();

        txthotelname.setText(getArguments().getString("name"));
        txtlocation.setText(getArguments().getString("address"));
        fab.setText(getArguments().getString("pop"));
        txtdesc.setText(getArguments().getString("descp"));

        if(getArguments().getString("isOpen").toString().equals("true"))
            txttime.setText("Open Now");
        else
            txttime.setText("Closed");
        //txtdesc.setText(activity.descp);
        //txtdesc.setText(descp);

        image=(CollapsingToolbarLayout)v.findViewById(R.id.toolbar_layout);
       // image.setBackgroundResource(Integer.parseInt(img));


        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(null);
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
}
