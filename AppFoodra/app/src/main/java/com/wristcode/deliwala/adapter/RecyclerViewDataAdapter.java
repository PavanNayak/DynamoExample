package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.R;
import com.wristcode.deliwala.Pojo.SectionDataModel;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;

    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler2_dialog, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i)
    {
        final String sectionName = dataList.get(i).getHeaderTitle();
        final String sectionType = dataList.get(i).getHeaderType();
        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();
        itemRowHolder.tvheader.setText(sectionName);
        itemRowHolder.tvtype.setText(sectionType);
        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems, sectionType);
        itemRowHolder.rvItems.setHasFixedSize(true);
        itemRowHolder.rvItems.setLayoutManager(new LinearLayoutManager(mContext));
        itemRowHolder.rvItems.setAdapter(itemListDataAdapter);
        itemRowHolder.rvItems.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount()
    {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvheader, tvtype;
        protected RecyclerView rvItems;

        public ItemRowHolder(View view)
        {
            super(view);
            tvheader = (TextView) view.findViewById(R.id.tvheader);
            tvtype = (TextView) view.findViewById(R.id.tvtype);
            rvItems = (RecyclerView) view.findViewById(R.id.rvItems);
        }
    }
}