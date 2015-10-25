package com.hash.looop;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.looop.adapter.TrendingAdapter;
import com.hash.looop.model.FollowResponse;
import com.hash.looop.model.Looop;
import com.hash.looop.model.TrendingLooopsRequest;
import com.hash.looop.model.TrendingLooopsResponse;
import com.hash.looop.utils.MySharedPreference;
import com.hash.looop.utils.SpacesItemDecoration;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by mathan on 26/9/15.
 */
public class TrendingTabFragment extends Fragment {

    @Bind(R.id.feed_list)
    RecyclerView mFeedList;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;
    private Location mCurrentLocation;
    private TrendingAdapter mAdapter;
    private TrendingLooopsResponse mTrendingLooopsResponse;
    private HashMap<String,Integer> mFollowMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_layout, container, false);
        ButterKnife.bind(this, view);
        if(!mBus.isRegistered(this)) {
            mBus.register(this);
        }
        mPrefs = new MySharedPreference(getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFeedList.setLayoutManager(linearLayoutManager);
        mAdapter = new TrendingAdapter(getActivity());
        mFeedList.addItemDecoration(new SpacesItemDecoration(dpToPx(5),dpToPx(5)));
        mFeedList.setAdapter(mAdapter);

        return view;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    private void getTrendingLooops() {
        TrendingLooopsRequest trendingLooopsRequest = new TrendingLooopsRequest();
        trendingLooopsRequest.setUserId(mPrefs.getUserId());
        trendingLooopsRequest.setLatitude("" + mCurrentLocation.getLatitude());
        trendingLooopsRequest.setLongitude("" + mCurrentLocation.getLongitude());
        mBus.post(trendingLooopsRequest);
    }

    public void onEventMainThread(FollowResponse followResponse) {
        if (followResponse.getStatus() == 1) {
            mFollowMap.put(followResponse.getFollowedId(),1);
            mAdapter.updateFollowMap(mFollowMap);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEvent(Location currentLocation) {
        mCurrentLocation = currentLocation;
        getTrendingLooops();
    }

    public void onEventMainThread(TrendingLooopsResponse trendingLooopsResponse) {
        mTrendingLooopsResponse = trendingLooopsResponse;
        if(mTrendingLooopsResponse != null && mTrendingLooopsResponse.getLooops() != null &&
                mTrendingLooopsResponse.getLooops().size() > 0) {
            List<Looop> looopList = mTrendingLooopsResponse.getLooops();
            for(int i = 0 ; i < looopList.size(); i++) {
                mFollowMap.put(""+looopList.get(i).getUserId(),looopList.get(i).getRelationship());
            }
            mAdapter.updateLooops(looopList);
            mAdapter.updateFollowMap(mFollowMap);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
