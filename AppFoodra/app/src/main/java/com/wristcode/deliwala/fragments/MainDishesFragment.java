package com.wristcode.deliwala.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.Pojo.Items;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.adapter.ItemsAdapter;
import com.wristcode.deliwala.sqlite.ExampleDBHelper;

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
    TextView cart_badge;
    ExampleDBHelper dh;
    ArrayList<String> menuItem;
    LinearLayout layoutveg;
    Switch vegonly;

    public MainDishesFragment() {}

    @SuppressLint("ValidFragment")
    public MainDishesFragment(ArrayList<String> a) {

        this.menuItem = a;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        layoutveg = v.findViewById(R.id.layoutveg);
        vegonly = v.findViewById(R.id.simpleSwitch);
        vegonly.setChecked(false);
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

                JSONObject jobject1 = jobject.getJSONObject("restaurant");
                data.resid = jobject1.getString("id");
                data.resname = jobject1.getString("restaurantName");

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

            vegonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (vegonly.isChecked())
                    {
                        adapter = new ItemsAdapter(getActivity(), vegList, frag);
                        recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerMenu.setNestedScrollingEnabled(false);
                        recyclerMenu.setFocusable(false);
                        recyclerMenu.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
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
//            JSONArray jsonArray = jobject.getJSONArray("priceVariation");
//            if(jsonArray.length() == 0)
//            {
//                //Do nothing
//            }
//            for(int i=0;i<jsonArray.length();i++)
//            {
//                JSONObject jobject2 = jsonArray.getJSONObject(i);
//                data.vid = jobject2.getString("id");
//                data.vname = jobject2.getString("variationName");
//                data.vprice = jobject2.getString("price");
//            }
        return v;
    }
}
