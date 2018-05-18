package com.wristcode.deliwala.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.CartActivity;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.extra.IConstants;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements IConstants
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SharedPreferences preferences;
    ArrayList<String> strMenuTitle;
    ArrayList<ArrayList<String>> strJson;
    List<String> strMenuTitle1, strJson1;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    TextView cartbadge, txttitle;
    ExampleDBHelper dh;
    private FrameLayout layoutInner;

    public MenuFragment() {}

    public static MenuFragment newInstance()
    {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        preferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        layoutInner = (FrameLayout) v.findViewById(R.id.layoutInner);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        cartbadge = (TextView) v.findViewById(R.id.cartbadge);
        //txttitle = (TextView) v.findViewById(R.id.txttitle);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Medium.ttf");
        dh = new ExampleDBHelper(getActivity());
        strMenuTitle = new ArrayList<>();
        strMenuTitle1 = new ArrayList<>();
        strJson = new ArrayList<ArrayList<String>>();
        strJson1 = new ArrayList<>();

        //txttitle.setText(preferences.getString("name","").toString());
        //txttitle.setTypeface(font);
     //   Toast.makeText(getActivity(),preferences.getString("id","").toString(), Toast.LENGTH_SHORT).show();
        new AsyncGetData().execute(preferences.getString("id","").toString());
        viewPager = v.findViewById(R.id.simpleViewPager);

        tabLayout = v.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        if (dh.gettotalqty() > 0 || dh.gettotalprice() > 0)
        {
            //layoutInner.setVisibility(View.VISIBLE);
            cartbadge.setText(String.valueOf(dh.gettotalqty()));

        } else
            {
                cartbadge.setText(String.valueOf(dh.gettotalqty()));
            //layoutInner.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), CartActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        for(int i=0; i<strMenuTitle.size(); i++)
        {
            adapter.addFragment(new MainDishesFragment(strJson.get(i)),strMenuTitle.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    private class AsyncGetData extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(API_PATH+"restaurant/search-product/category");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("restId", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }

            try {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return (result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            pdLoading.dismiss();
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0)
                    {
                        Toast.makeText(getActivity(), "No Items found!!!", Toast.LENGTH_SHORT).show();
                    }

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jobject=jsonArray.getJSONObject(i);
                        strMenuTitle.add(jobject.getString("categoryName"));
                        JSONArray jsonArray1 = jobject.getJSONArray("menuItem");
                        ArrayList<String> s = new ArrayList<>();
                        for(int j=0; j<jsonArray1.length(); j++)
                        {
                            s.add(String.valueOf(jsonArray1.get(j)));
                        }
                        strJson.add(s);
                        setupViewPager(viewPager);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}