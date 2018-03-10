package com.wristcode.deliwala.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wristcode.deliwala.R;

/**
 * Created by Ajay Jagadish on 09-Mar-18.
 */

public class ProfileFragment extends Fragment {

    EditText profilename,profilenumber;
    TextView changeAddress,txtaddress;
    Button btn_update;

    public static ProfileFragment newInstance(){
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profilename = v.findViewById(R.id.profilename);
        profilenumber = v.findViewById(R.id.profilenumber);
        changeAddress = v.findViewById(R.id.changeAddress);
        txtaddress = v.findViewById(R.id.txtaddress);
        btn_update = v.findViewById(R.id.btn_update);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(),"GT-Walsheim-Regular.ttf");

        profilename.setTypeface(font1);
        profilenumber.setTypeface(font1);
        changeAddress.setTypeface(font);
        txtaddress.setTypeface(font1);
        btn_update.setTypeface(font1);
        return v;
    }
}
