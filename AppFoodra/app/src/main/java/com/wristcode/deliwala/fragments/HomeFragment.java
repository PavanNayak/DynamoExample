package com.wristcode.deliwala.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.LoginActivity;
import com.wristcode.deliwala.OTPActivity;
import com.wristcode.deliwala.Pojo.Category;
import com.wristcode.deliwala.Pojo.Offers;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.CategoryAdapter;
import com.wristcode.deliwala.adapter.OffersAdapter;
import com.wristcode.deliwala.extra.IConstants;

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

public class HomeFragment extends Fragment implements IConstants
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView text1,text2;
    RecyclerView menurecycler, offerrecycler;
    private List<Category> categoriesList;
    private List<Offers> categoriesList1;
    CategoryAdapter adapter;
    OffersAdapter adapter1;
    LinearLayoutManager HorizontalLayout;
    Context mContext;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();
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
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        text1 = v.findViewById(R.id.text1);
        text2 = v.findViewById(R.id.text2);
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
        new AsyncRestaurants.execute();
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

    private class AsyncRestaurants extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(mContext);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/all-restaurant");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return "exception";
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return "exception";
            }

            try
            {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return(result.toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            pdLoading.dismiss();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject json_data = jArray.getJSONObject(i);
                        Offers resData = new Offers();
                        resData.id = json_data.getString("id");
                        resData.name = json_data.getString("restaurantName");

                    }
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
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
