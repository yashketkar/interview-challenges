package com.yashketkar.telenotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yashketkar on 4/22/2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static ArrayList<Restaurant> mDataset;
    private static Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public LinearLayout mLinearLayout;
        public TextView mName;
        public TextView mAddress;
        //public TextView mRating;
        public TextView mType;
        public RatingBar mRatingBar;

        public ViewHolder(LinearLayout v) {
            super(v);
            v.setOnClickListener(this);
            mName = (TextView) v.findViewById(R.id.name);
            mAddress = (TextView) v.findViewById(R.id.address);
            mType = (TextView) v.findViewById(R.id.type);
            mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.row_layout);

        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            restaurantClicked(itemPosition);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Restaurant> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        ViewHolder vh = new ViewHolder((LinearLayout)v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mName.setText(mDataset.get(position).getmName());
        holder.mAddress.setText(mDataset.get(position).getmAddress());
        holder.mType.setText(mDataset.get(position).getmType());
        holder.mRatingBar.setRating(Float.parseFloat(mDataset.get(position).getmRating()));
    }

//    @Override
    public static void restaurantClicked(int itemPosition) {
        Intent myIntent = new Intent(mContext, MapsActivity.class);
        myIntent.putExtra("restaurant", mDataset.get(itemPosition));
        mContext.startActivity(myIntent);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
