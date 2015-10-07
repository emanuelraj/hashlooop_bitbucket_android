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
import com.hash.loop.model.LooopLikeRequest;
import com.hash.loop.utils.MySharedPreference;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by mathan on 26/9/15.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    private Context mContext;
    private List<Looop> mLooopList;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    public FeedsAdapter(Context context) {
        mContext = context;
        mPrefs = new MySharedPreference(mContext);
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
        holder.mLooopDetail.setText(looop.getName());
        holder.mLike.setTag(looop.getId());
        holder.mLooopContent.setText(looop.getStatus());
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.looop_details)
        TextView mLooopDetail;
        @Bind(R.id.looop_content)
        TextView mLooopContent;
        @Bind(R.id.card_view)
        CardView mCardView;
        @Bind(R.id.like)
        TextView mLike;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            TextView likeView = (TextView) itemView.findViewById(R.id.like);
            likeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.like:
                    mLike.setSelected(!mLike.isSelected());
                    LooopLikeRequest likeRequest = new LooopLikeRequest();
                    likeRequest.setUserId(mPrefs.getUserId());
                    likeRequest.setLooopId(""+(int) v.getTag());
                    mBus.post(likeRequest);
                    break;
            }
        }
    }

}
