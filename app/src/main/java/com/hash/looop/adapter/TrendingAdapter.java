package com.hash.looop.adapter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hash.looop.R;
import com.hash.looop.model.FollowRequest;
import com.hash.looop.model.Looop;
import com.hash.looop.model.LooopLikeRequest;
import com.hash.looop.utils.MySharedPreference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by mathan on 11/10/15.
 */
public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {
    private Context mContext;
    private List<Looop> mLooopList;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;
    private HashMap<String, Integer> mFollowMap = new HashMap<>();

    public TrendingAdapter(Context context) {
        mContext = context;
        mPrefs = new MySharedPreference(mContext);
    }

    public void updateLooops(List<Looop> looops) {
        mLooopList = looops;
    }

    public void updateFollowMap(HashMap<String, Integer> followMap) {
        mFollowMap = followMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trending_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Looop looop = mLooopList.get(position);
        holder.mLooopDetails.setText(looop.getName());
        holder.mLike.setTag(looop.getId());
        holder.mLooopContent.setText(looop.getStatus());
        if (mFollowMap.get("" + looop.getUserId()) == 0) {
            if (!looop.getUserId().toString().equalsIgnoreCase(mPrefs.getUserId())) {
                holder.mFollow.setVisibility(View.VISIBLE);
                holder.mFollow.setTag(position);
            } else {
                holder.mFollow.setVisibility(View.GONE);
            }
        } else {
            holder.mFollow.setVisibility(View.GONE);
        }
        Picasso.with(mContext).load("https://scontent.xx.fbcdn.net/hphotos-xtp1/v/t1.0-9/11168058_858144984222404_7536273263674152164_n.jpg?oh=9667cba30ed5fb925626e6f353b742ca&oe=5688C8A7")
                .into(holder.mImage);
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
        @Bind(R.id.image)
        ImageView mImage;
        @Bind(R.id.looop_details)
        TextView mLooopDetails;
        @Bind(R.id.distance)
        TextView mDistance;
        @Bind(R.id.looop_content)
        TextView mLooopContent;
        @Bind(R.id.like)
        ImageView mLike;
        @Bind(R.id.like_layout)
        LinearLayout mLikeLayout;
        @Bind(R.id.follow)
        ImageView mFollow;
        @Bind(R.id.card_view)
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ImageView likeView = (ImageView) itemView.findViewById(R.id.like);
            likeView.setOnClickListener(this);
            ImageView followView = (ImageView) itemView.findViewById(R.id.follow);
            followView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.like:
                    mLike.setSelected(!mLike.isSelected());
                    LooopLikeRequest likeRequest = new LooopLikeRequest();
                    likeRequest.setUserId(mPrefs.getUserId());
                    likeRequest.setLooopId("" + (int) mLike.getTag());
                    mBus.post(likeRequest);
                    break;
                case R.id.follow:
                    FollowRequest followRequest = new FollowRequest();
                    int position = (int) mFollow.getTag();
                    followRequest.setUserId("" + mLooopList.get(position).getUserId());
                    followRequest.setFollowingId(mPrefs.getUserId());
                    mBus.post(followRequest);
                    mFollow.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
