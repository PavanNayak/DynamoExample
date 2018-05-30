package com.wristcode.deliwala.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wristcode.deliwala.R;
import com.wristcode.deliwala.Pojo.SingleItemModel;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private String typeList;
    private Context mContext;
    private int lastSelectedPosition = -1;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList, String typeList) {
        this.itemsList = itemsList;
        this.typeList = typeList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler3_dialog, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i)
    {
        SingleItemModel singleItem = itemsList.get(i);
        holder.tvTitle.setText(singleItem.getName());
        holder.tvPrice.setText(singleItem.getPrice());
        holder.rbtnselect.setChecked(lastSelectedPosition == i);
        if(typeList.equals("single"))
        {
            holder.cbselect.setVisibility(View.GONE);
            holder.rbtnselect.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.cbselect.setVisibility(View.VISIBLE);
            holder.rbtnselect.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder
    {

        public TextView tvTitle, tvPrice;
        public CheckBox cbselect;
        public RadioButton rbtnselect;

        public SingleItemRowHolder(View view)
        {
            super(view);
            tvTitle = view.findViewById(R.id.lblListItem);
            tvPrice = view.findViewById(R.id.lblListItemPrice);
            cbselect = view.findViewById(R.id.cbselect);
            rbtnselect = view.findViewById(R.id.rbtnselect);

            rbtnselect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Toast.makeText(mContext, tvTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            cbselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        Toast.makeText(mContext, tvTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}