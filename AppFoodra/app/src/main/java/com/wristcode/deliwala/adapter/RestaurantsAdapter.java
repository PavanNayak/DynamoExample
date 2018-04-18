package com.wristcode.deliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wristcode.deliwala.HotelActivity;

import com.wristcode.deliwala.Pojo.Restaurants;
import com.wristcode.deliwala.R;
import com.wristcode.deliwala.extra.OnLoadMoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String mypreference = "mypref";
    public String cart_id = "-1";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public int flag = 0;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    private List<Restaurants> moviesList;
    private Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView offerrecycler;
    private LinearLayoutManager linearLayoutManager;
    SharedPreferences pref;
    Date newDate, newDate1;
    public List<String> resTagname = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, txtdesc, txtratings, txttime;
        RelativeLayout relativehotel;
        ImageView image;
        ImageView thumbnail;
        RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            txtname = view.findViewById(R.id.txtname);
            txtdesc = view.findViewById(R.id.txtdesc);
            txttime = view.findViewById(R.id.txttime);
            txtratings = view .findViewById(R.id.txtratings);
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
        linearLayoutManager = (LinearLayoutManager) offerrecycler.getLayoutManager();
        offerrecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
//                Toast.makeText(mContext, "Hi", Toast.LENGTH_SHORT).show();
//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold))
//                {
//                    if (mOnLoadMoreListener != null)
//                    {
//                        mOnLoadMoreListener.onLoadMore();
//                    }
//                    isLoading = true;
//                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                //.makeText(mContext, "Bye", Toast.LENGTH_SHORT).show();
            }
        });
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
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof MyViewHolder)
        {
            final Restaurants movie = moviesList.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.txtname.setText(movie.getResname());
            //myViewHolder.txtdesc.setText("Chinese, Arabian, Italian");
            //myViewHolder.txttime.setText("10AM - 10PM");
            myViewHolder.txtratings.setText(String.format("%.1f", Float.valueOf(movie.getRespop())));
            myViewHolder.ratingBar.setRating(Float.valueOf(movie.getRespop()));

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Bold.ttf");
            Typeface font1 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Medium.ttf");
            Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "GT-Walsheim-Regular.ttf");

            myViewHolder.txtname.setTypeface(font);
            myViewHolder.txtdesc.setTypeface(font2);
            myViewHolder.txttime.setTypeface(font2);

            Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/" + movie.getResimg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(myViewHolder.image);

            //Glide.with(mContext).load("http://appfoodra.com/uploads/restaurant/icons/"+movie.getResimg()).into(holder.image);

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

            myViewHolder.txttime.setText(date+" - "+date1);

            resTagname.clear();
            resTagname = movie.getRestags();

            for (int i=0; i<resTagname.size();i++)
            {
                myViewHolder.txtdesc.append(resTagname.get(i));
                myViewHolder.txtdesc.append(" ");
            }


            myViewHolder.relativehotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    SharedPreferences.Editor editor1 = pref.edit();
                    editor1.putString("id",movie.getResid());
                    editor1.putString("name",movie.getResname());
                    editor1.putString("img",movie.getResimg());
                    editor1.putString("isOpen",movie.getResisopen());
                    editor1.putString("pop",movie.getRespop());
                    editor1.putString("address",movie.getResadd());
                    editor1.putString("descp", movie.getResdescp());
                    editor1.apply();

                    Intent i = new Intent(mContext, HotelActivity.class);
//                    i.putExtra("id", movie.getResid());
//                    i.putExtra("address", movie.getResadd());
//                    i.putExtra("isOpen", movie.getResisopen());
//                    i.putExtra("descp", movie.getResdescp());
//                    i.putExtra("img", movie.getResimg());
//                    i.putExtra("pop", movie.getRespop());
//                    i.putExtra("name", movie.getResname());
                    mContext.startActivity(i);
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
        isLoading = false;
    }
}