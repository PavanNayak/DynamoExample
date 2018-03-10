package com.wristcode.deliwala.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wristcode.deliwala.R;

/**
 * Created by Ajay Jagadish on 09-Mar-18.
 */

public class NotificationFragment extends Fragment {
    TextView txtnotify;

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        txtnotify = v.findViewById(R.id.txtnotify);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Medium.ttf");

        txtnotify.setTypeface(font);
        return v;
    }
}
