package com.wristcode.appfoodra.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wristcode.appfoodra.R;

public class OverviewFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView txthotelname,txtlocation,txtdesc,txttime,txtdistance,txtdelivery;
    private OnFragmentInteractionListener mListener;

    public OverviewFragment() {}

    public static OverviewFragment newInstance(String param1, String param2)
    {
        OverviewFragment fragment = new OverviewFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_overview, container, false);
        txthotelname=(TextView)v.findViewById(R.id.txthotelname);
        txtlocation=(TextView)v.findViewById(R.id.txtlocation);
        txtdesc=(TextView)v.findViewById(R.id.txtdesc);
        txttime=(TextView)v.findViewById(R.id.txttime);
        txtdistance=(TextView)v.findViewById(R.id.txtdistance);
        txtdelivery=(TextView)v.findViewById(R.id.txtdelivery);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Regular.ttf");

        txthotelname.setTypeface(font2);
        txtlocation.setTypeface(font2);
        txtdesc.setTypeface(font2);
        txttime.setTypeface(font2);
        txtdistance.setTypeface(font2);
        txtdelivery.setTypeface(font2);

        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(txthotelname.getText().toString());
        return v;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
