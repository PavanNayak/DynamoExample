package com.wristcode.deliwala.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wristcode.deliwala.R;
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

import static com.wristcode.deliwala.SelectLocationActivity.READ_TIMEOUT;
import static com.wristcode.deliwala.adapter.RestaurantsAdapter.CONNECTION_TIMEOUT;

public class MenuFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<String> strMenuTitle, strJson;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView cart_badge;
    ExampleDBHelper dh;

    public MenuFragment() {
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        cart_badge = v.findViewById(R.id.cart_badge);
        strMenuTitle = new ArrayList<>();
        strJson = new ArrayList<>();

        new AsyncGetData().execute("2");
        viewPager = v.findViewById(R.id.simpleViewPager);

        tabLayout = v.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        for(int i=0; i<strMenuTitle.size(); i++)
        adapter.addFragment(new MainDishesFragment(strJson.get(i)), strMenuTitle.get(i));
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

    public void setCart(int item)
    {
        //cart_badge.setText(item);
    }

    public void setPrice(int total)
    {
        //txtitemtotal.setText("₹" + String.valueOf(total));
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/restaurant/search-product");
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
        protected void onPostExecute(String result) {
            pdLoading.dismiss();

            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");



                    for(int i=0;i<jsonArray.length();i++){


                      //  JSONObject json=new JSONObject(String.valueOf(jsonArray));
                       // strJson.add(String.valueOf(json));
                        JSONObject jobject=jsonArray.getJSONObject(i);

                        JSONObject jCategory=jobject.getJSONObject("category");

                        if(!(strMenuTitle.contains(jCategory.getString("categoryName")))){
                            strMenuTitle.add(jCategory.getString("categoryName"));
                            strJson.add(String.valueOf(jsonArray.get(i)));
                      //      Toast.makeText(getActivity(),jCategory.getString("categoryName"), Toast.LENGTH_SHORT).show();
                        }
//                        else {
//                            strMenuTitle.add("");
//                            strJson.add(String.valueOf(jsonArray.get(i)));
//                        }
                     //   for(int j=0;j<strMenuTitle.size();j++)
                     //   if(!(strMenuTitle.get(i).equals(jCategory.getString("categoryName")))){

                      //  }
                      //  else
                       //     Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();

                        setupViewPager(viewPager);


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}