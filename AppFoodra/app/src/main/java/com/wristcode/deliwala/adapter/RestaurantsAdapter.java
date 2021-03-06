package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wristcode.deliwala.HotelActivity;

import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.extra.OnLoadMoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public int flag = 0;
    private List<Restaurants> moviesList;
    private Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView offerrecycler;
    private LinearLayoutManager linearLayoutManager;
    SharedPreferences pref;
    Date newDate, newDate1;
    public List<String> resTagname = new ArrayList<>();
    String iTag;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, txtdesc, txtratings, txttime, txtdistance;
        RelativeLayout relativehotel;
        ImageView image;
        ImageView thumbnail;
        RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            txtname = view.findViewById(R.id.txtname);
            txtdesc = view.findViewById(R.id.txtdesc);
            txttime = view.findViewById(R.id.txttime);
            txtratings = view.findViewById(R.id.txtratings);
            txtdistance = view.findViewById(R.id.txtdistance);
            ratingBar = view.findViewById(R.id.ratingBar);
            image = view.findViewById(R.id.image);
            relativehotel = view.findViewById(R.id.relativehotel);
            pref = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder
    {
        ProgressBar progressBar;

        public LoadingViewHolder(View view)
        {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }

    public RestaurantsAdapter(final Context mContext, List<Restaurants> moviesList, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.offerrecycler = recyclerView;
//    }
//
//    public RestaurantsAdapter()
//    {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return moviesList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row, parent, false);
//        return new MyViewHolder(itemView);

        if (viewType == VIEW_TYPE_ITEM)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row, parent, false);
            return new MyViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof MyViewHolder)
        {
            final Restaurants movie = moviesList.get(position);
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.txtname.setText(movie.getResname());
            myViewHolder.txtratings.setText(String.format("%.1f", Float.valueOf(movie.getRespop())));
            myViewHolder.ratingBar.setRating(Float.valueOf(movie.getRespop()));
            myViewHolder.txtdistance.setText(String.format("%.2f", Float.valueOf(movie.getResdist()))+" km");

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
            Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
            Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

            myViewHolder.txtname.setTypeface(font);
            myViewHolder.txtdesc.setTypeface(font2);
            myViewHolder.txttime.setTypeface(font2);
            myViewHolder.txtdistance.setTypeface(font2);

            Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/" + movie.getResimg())
                    .placeholder(R.drawable.logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo)
                    .into(myViewHolder.image);


            String strCurrentDate = movie.getResopentime();
            String strCurrentDate1 = movie.getResclosetime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            try {
                newDate = format.parse(strCurrentDate);
                newDate1 = format.parse(strCurrentDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String date = dateFormat.format(newDate);
            String date1 = dateFormat.format(newDate1);

            SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
            Date slot1 = null;
            Date slot2 = null;

            try
            {
                slot1 = time.parse(date);
                slot2 = time.parse(date1);

                Date CurrentTime = time.parse(time.format(new Date()));
                if ((CurrentTime.before(slot1) || CurrentTime.after(slot2)))
                {
                    myViewHolder.txttime.setText("CLOSED");
                    myViewHolder.txttime.setTextColor(Color.parseColor("#DC143C"));
                }
                else
                {
                    myViewHolder.txttime.setText(date+" - "+date1);
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            resTagname.clear();
            resTagname = movie.getRestags();

            for (int i=0; i<resTagname.size();i++)
            {
                iTag = resTagname.get(i);
                myViewHolder.txtdesc.setText(myViewHolder.txtdesc.getText()+iTag+ " ");
            }


            myViewHolder.relativehotel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(myViewHolder.txttime.getText().toString().equals("CLOSED"))
                    {
                        myViewHolder.relativehotel.setEnabled(false);
                    }
                    else
                    {
                        myViewHolder.relativehotel.setEnabled(true);
                        SharedPreferences.Editor editor1 = pref.edit();
                        editor1.putString("id",movie.getResid());
                        editor1.putString("name",movie.getResname());
                        editor1.putString("img",movie.getResimg());
                        editor1.putString("isOpen",movie.getResisopen());
                        editor1.putString("pop",movie.getRespop());
                        editor1.putString("address",movie.getResadd());
                        editor1.putString("descp", movie.getResdescp());
                        editor1.putString("distance", movie.getResdist());
                        editor1.apply();

                        Intent i = new Intent(mContext, HotelActivity.class);
                        mContext.startActivity(i);
                    }

                }
            });
        }
        else if (holder instanceof LoadingViewHolder)
        {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
    public void updateList(List<Restaurants> list){
        moviesList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount()
    {
        //return moviesList.size();
        return moviesList == null ? 0 : moviesList.size();
    }

    public void setLoaded() {
        loading = false;
    }
}