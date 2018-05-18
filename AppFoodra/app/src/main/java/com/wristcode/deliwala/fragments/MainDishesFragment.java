package com.wristcode.deliwala.fragments;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.ItemsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Jagadish on 28-Feb-18.
 */

public class MainDishesFragment extends Fragment
{
    RecyclerView recyclerMenu;
    List<Items> categoriesList;
    List<Items> vegList;
    ItemsAdapter adapter;
    MenuFragment frag = new MenuFragment();
    ArrayList<String> menuItem;
    LinearLayout layoutveg;
    Switch vegonly;
    ImageView imgveg;
    TextView txtveg;
    SharedPreferences pref;
    SearchView searchitem;

    public MainDishesFragment() {}

    @SuppressLint("ValidFragment")
    public MainDishesFragment(ArrayList<String> a)
    {
        this.menuItem = a;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchitem = (SearchView) v.findViewById(R.id.searchitem);
        searchitem.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchitem.setMaxWidth(Integer.MAX_VALUE);
        searchitem.setQueryHint("Search Items");
        searchitem.setIconified(false);
        searchitem.clearFocus();
        layoutveg = v.findViewById(R.id.layoutveg);
        imgveg = v.findViewById(R.id.imgveg);
        txtveg = v.findViewById(R.id.txtveg);
        vegonly = v.findViewById(R.id.simpleSwitch);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GT-Walsheim-Medium.ttf");
        txtveg.setTypeface(font);
        vegonly.setChecked(false);

        searchitem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

        try
        {
            categoriesList = new ArrayList<>();
            vegList = new ArrayList<>();
            for (int i = 0; i < menuItem.size(); i++)
            {
                JSONObject jobject = new JSONObject(menuItem.get(i));
                Items data = new Items();
                data.id = jobject.getString("id");
                data.name = jobject.getString("itemName");
                data.type = jobject.getString("vegType");
                if (jobject.has("itemShortDescription"))
                {
                    data.descp = jobject.getString("itemShortDescription");
                }
                else
                {
                    data.descp = "No Description";
                }
                data.price = jobject.getString("regularPrice");

                data.resid = pref.getString("id","").toString();
                data.resname = pref.getString("name","").toString();

                JSONArray jsonArray = jobject.getJSONArray("priceVariation");
                if(!(jsonArray.length() == 0))
                {
                    for(int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject jobject2 = jsonArray.getJSONObject(j);
                        data.vid.add(jobject2.getString("id"));
                        data.vname.add(jobject2.getString("variationName"));
                        data.vprice.add(jobject2.getString("price"));

                    //Add Priority here after for loop sort the arrayList
                    }
                }

                categoriesList.add(data);

                if (jobject.getString("vegType").toString().equals("veg"))
                {
                    vegList.add(data);
                }
            }
            recyclerMenu = v.findViewById(R.id.recyclerMenu);
            adapter = new ItemsAdapter(getActivity(), categoriesList, frag);
            recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerMenu.setNestedScrollingEnabled(false);
            recyclerMenu.setFocusable(false);
            recyclerMenu.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            vegonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (vegonly.isChecked())
                    {
                        if(vegList.isEmpty())
                        {
                            recyclerMenu.setVisibility(View.GONE);
                            imgveg.setVisibility(View.VISIBLE);
                            txtveg.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            adapter = new ItemsAdapter(getActivity(), vegList, frag);
                            recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerMenu.setNestedScrollingEnabled(false);
                            recyclerMenu.setFocusable(false);
                            recyclerMenu.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                    else
                    {
                        imgveg.setVisibility(View.GONE);
                        txtveg.setVisibility(View.GONE);
                        recyclerMenu.setVisibility(View.VISIBLE);
                        adapter = new ItemsAdapter(getActivity(), categoriesList, frag);
                        recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerMenu.setNestedScrollingEnabled(false);
                        recyclerMenu.setFocusable(false);
                        recyclerMenu.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return v;
    }
}
