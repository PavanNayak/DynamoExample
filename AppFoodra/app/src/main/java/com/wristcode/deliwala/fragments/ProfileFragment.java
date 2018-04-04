package com.wristcode.deliwala.fragments;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    TextView txtusername, contact, txtcontact, emailid, txtemail, address, txtaddress;
    SharedPreferences pref;

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
        txtusername = v.findViewById(R.id.txtusername);
        contact = v.findViewById(R.id.contact);
        txtcontact = v.findViewById(R.id.txtcontact);
        emailid = v.findViewById(R.id.emailid);
        txtemail = v.findViewById(R.id.txtemail);
        address = v.findViewById(R.id.address);
        txtaddress = v.findViewById(R.id.txtaddress);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"GT-Walsheim-Medium.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(),"GT-Walsheim-Regular.ttf");

        txtusername.setTypeface(font);
        contact.setTypeface(font);
        txtcontact.setTypeface(font1);
        emailid.setTypeface(font);
        txtemail.setTypeface(font1);
        address.setTypeface(font);
        txtaddress.setTypeface(font1);

        txtusername.setText(pref.getString("Name", "").toString());
        txtcontact.setText(pref.getString("PhoneNo","").toString());
        txtemail.setText(pref.getString("Email","").toString());
        txtaddress.setText(pref.getString("Address","").toString());
        return v;
    }
}
