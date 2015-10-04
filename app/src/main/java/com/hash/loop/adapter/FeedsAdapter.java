package com.hash.loop.adapter;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hash.loop.R;
import com.hash.loop.model.Looop;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathan on 26/9/15.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    private Context mContext;
    private List<Looop> mLooopList;

    public FeedsAdapter(Context context) {
        mContext = context;
    }

    public void updateFeedsAdapter(List<Looop> looops) {
        mLooopList = looops;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Looop looop = mLooopList.get(position);
        holder.mListItem.setText(looop.getStatus() + "  -  " + looop.getUserId());
        //enterReveal(holder.mListItem);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void enterReveal(View view) {
        // previously invisible view
        View myView = view;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;

        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    @Override
    public int getItemCount() {
        return mLooopList == null ? 0 : mLooopList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_item)
        TextView mListItem;
        @Bind(R.id.card_view)
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
