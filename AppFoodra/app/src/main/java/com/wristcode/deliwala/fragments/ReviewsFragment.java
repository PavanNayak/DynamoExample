package com.wristcode.deliwala.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wristcode.deliwala.CartActivity;
import com.wristcode.deliwala.HotelActivity;
import com.wristcode.deliwala.NavDrawer;
import com.wristcode.deliwala.OrderHistoryActivity;
import com.wristcode.deliwala.Pojo.OrderHistory;
import com.wristcode.deliwala.Pojo.Reviews;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.OrderHistoryAdapter;
import com.wristcode.deliwala.adapter.ReviewsAdapter;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ajay Jagadish on 28-Feb-18.
 */

public class ReviewsFragment extends Fragment implements IConstants
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerReview;
    private List<Reviews> reviewsList;
    ReviewsAdapter adapter;
    SharedPreferences pref;
    LinearLayout linear;

    public ReviewsFragment() {}

    public static ReviewsFragment newInstance() {
        ReviewsFragment fragment = new ReviewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);
        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        linear = v.findViewById(R.id.linear);
        recyclerReview = v.findViewById(R.id.recyclerReview);
        reviewsList = new ArrayList<>();
//        prepareAlbums();
        new AsyncListRatings().execute(pref.getString("id","").toString());
        return v;
    }

//    private void prepareAlbums() {
//        int[] covers = new int[]{
//                R.drawable.userimg,
//                R.drawable.userimg,
//                R.drawable.userimg,
//                R.drawable.userimg
//        };
//
//        Reviews a = new Reviews("", "Pavan Nayak", "Loved the restaurant!!!", covers[0]);
//        reviewsList.add(a);
//        a = new Reviews("", "Srikanth", "Tasty Food, Would love to visit again!!", covers[1]);
//        reviewsList.add(a);
//        a = new Reviews("", "Shailesh", "Good..", covers[2]);
//        reviewsList.add(a);
//        a = new Reviews("", "Prashanth", "Wonderful...", covers[3]);
//        reviewsList.add(a);
//
//        adapter = new ReviewsAdapter(getActivity(), reviewsList);
//        recyclerReview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerReview.setNestedScrollingEnabled(false);
//        recyclerReview.setFocusable(false);
//        recyclerReview.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

    private class AsyncListRatings extends AsyncTask<String, String, String> {
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
                url = new URL("http://www.appfoodra.com/api/app-manager/get-functionality/restaurant/ratings-list");
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

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("restId", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
            List<Reviews> data = new ArrayList<>();
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true"))
                {
                    JSONArray jArray = jsonObject.getJSONArray("data");
                    if (jArray.length() == 0)
                    {
                        linear.setVisibility(View.VISIBLE);
                        recyclerReview.setVisibility(View.GONE);
                    }
                    else
                    {
                        linear.setVisibility(View.GONE);
                        recyclerReview.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject json_data = jArray.getJSONObject(i);
                            Reviews fishData = new Reviews();
                            fishData.id = json_data.getString("id");
                            fishData.ratings = json_data.getString("ratings");
                            fishData.reviews = json_data.getString("reviews");

                            JSONObject jObject = json_data.getJSONObject("customer");
                            fishData.name = jObject.getString("username");
                            data.add(fishData);
                        }
                    }
                }

                adapter = new ReviewsAdapter(getActivity(), data);
                recyclerReview.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerReview.setNestedScrollingEnabled(false);
                recyclerReview.setFocusable(false);
                recyclerReview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
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
