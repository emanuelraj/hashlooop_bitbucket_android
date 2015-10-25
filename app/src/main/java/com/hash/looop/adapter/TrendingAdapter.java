package com.hash.looop.adapter;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hash.looop.R;
import com.hash.looop.model.FollowRequest;
import com.hash.looop.model.Looop;
import com.hash.looop.model.LooopLikeRequest;
import com.hash.looop.utils.MySharedPreference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Looop looop = mLooopList.get(position);
        holder.mLooopDetail.setText(looop.getName());
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato_Regular.ttf");
        holder.mLooopDetail.setTypeface(typeface, Typeface.BOLD);
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

        if (looop.getStatusType() != null && looop.getStatusType() == 1) {
            holder.mLooopImage.setVisibility(View.GONE);
            holder.mLooopContent.setEllipsize(null);
            holder.mLooopContent.setMaxLines(10);
        } else if (looop.getStatusType() != null && looop.getStatusType() == 2) {
            holder.mLooopImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(looop.getImageUrl()).into(holder.mLooopImage);
            holder.mLooopContent.setEllipsize(TextUtils.TruncateAt.END);
            holder.mLooopContent.setMaxLines(1);
        }

        if (looop.getIsLiked() != null && looop.getIsLiked() == 0) {
            holder.mLike.setSelected(false);
        } else if (looop.getIsLiked() != null && looop.getIsLiked() == 1) {
            holder.mLike.setSelected(true);
        }

        Double distance = looop.getDistance();
        if (distance != null) {
            distance = Math.round(distance * 10) / 10.0;
            if (distance > 0.0) {
                holder.mDistance.setText("" + distance + " kms Near you");
            } else {
                holder.mDistance.setText("Near you");
            }
        } else {
            holder.mDistance.setText("");
        }
        /*Picasso.with(mContext).load("https://scontent.xx.fbcdn.net/hphotos-xtp1/v/t1" +
                ".0-9/11168058_858144984222404_7536273263674152164_n.jpg?oh=9667cba30ed5fb925626e6f353b742ca&oe=5688C8A7")
                .into(holder.mProfile);*/

        if (!TextUtils.isEmpty(looop.getName())) {
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getColor(looop.getName().charAt(0));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRect(looop.getName().charAt(0) + "", color);
            holder.mProfile.setImageDrawable(drawable);
        }

    }

    private String getFormatedTime(String time) {
        String formattedTime = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM dd");
        try {
            Date date = formatter.parse(time);
            formattedTime = outputFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d("!Adapter", formattedTime);
        return formattedTime;
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
        ImageView mLike;
        @Bind(R.id.follow)
        ImageView mFollow;
        @Bind(R.id.image)
        ImageView mProfile;
        @Bind(R.id.looop_image)
        ImageView mLooopImage;
        @Bind(R.id.distance)
        TextView mDistance;

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
