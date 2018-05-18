package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.Pojo.Tags;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.TagRestaurantsActivity;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.MyViewHolder>
{
    private List<Tags> moviesList;
    private Context mContext;
    private int lastSelectedPosition = -1;
    RestaurantsAdapter adapter1;
    RecyclerView offerrecycler;
    SharedPreferences pref;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catid, catname;
        RelativeLayout relative;
        ImageView catimg;
        RadioButton radiobutton;

        public MyViewHolder(View view) {
            super(view);
            catid = view.findViewById(R.id.catid);
            catname = view.findViewById(R.id.catname);
            relative = view.findViewById(R.id.relative);
            catimg = view.findViewById(R.id.catimg);
            radiobutton = view.findViewById(R.id.radiobutton);
            pref = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

            radiobutton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = getAdapterPosition();
                    catimg.setColorFilter(Color.parseColor("#DC143C"));
                    catname.setTextColor(Color.parseColor("#DC143C"));
                    notifyDataSetChanged();

                    SharedPreferences.Editor editor1 = pref.edit();
                    editor1.putString("Tagpos", String.valueOf(lastSelectedPosition));
                    editor1.apply();

                    Intent i = new Intent(mContext, TagRestaurantsActivity.class);
                    i.putExtra("catid", catid.getText().toString());
                    mContext.startActivity(i);
                }
            });
        }
    }

    public TagsAdapter(Context mContext, List<Tags> moviesList, RestaurantsAdapter adapter1, RecyclerView offerrecycler) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.adapter1 = adapter1;
        this.offerrecycler = offerrecycler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Tags movie = moviesList.get(position);
        holder.catid.setText(movie.getId());
        holder.catname.setText(movie.getName());

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

        holder.catname.setTypeface(font2);

        Glide.with(mContext).load("http://appfoodra.com/uploads/restauranttype/icons/"+movie.getImg())
                .placeholder(R.drawable.all)
                .error(R.drawable.all)
                .into(holder.catimg);

        holder.radiobutton.setChecked(lastSelectedPosition == position);

        int colorImg = (position == lastSelectedPosition)? Color.parseColor("#DC143C") : Color.parseColor("#C0C0C0");
        holder.catimg.setColorFilter(colorImg);
        int colorTitle = (position == lastSelectedPosition)? Color.parseColor("#DC143C") : Color.parseColor("#C0C0C0");
        holder.catname.setTextColor(colorTitle);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}