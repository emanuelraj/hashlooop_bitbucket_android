package com.hash.looop;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.looop.adapter.FeedsAdapter;
import com.hash.looop.model.FeedLooopResponse;
import com.hash.looop.model.FetchLoopsRequest;
import com.hash.looop.model.FollowResponse;
import com.hash.looop.model.Looop;
import com.hash.looop.model.RefreshModel;
import com.hash.looop.utils.Constants;
import com.hash.looop.utils.MySharedPreference;
import com.hash.looop.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by mathan on 24/9/15.
 */
public class FeedTabFragment extends Fragment implements OnRefreshListener{

    @Bind(R.id.feed_list)
    RecyclerView mFeedList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private FeedsAdapter mAdapter;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;
    private Location mCurrentLocation;
    private HashMap<String, Integer> mFollowMap = new HashMap<>();
    private boolean isLoading = false;
    int mPastVisiblesItems;
    int mVisibleItemCount;
    int mTotalItemCount;
    LinearLayoutManager mLayoutManager;
    private List<Looop> mFeedListLooops;
    private int mMaxId = 0;
    private int mMinId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds_fragment, container, false);
        ButterKnife.bind(this, view);
        if (!mBus.isRegistered(this)) {
            mBus.register(this);
        }
        mPrefs = new MySharedPreference(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false);
        mFeedList.setLayoutManager(mLayoutManager);
        mFeedList.addItemDecoration(new SpacesItemDecoration(dpToPx(5), dpToPx(5)));
        mAdapter = new FeedsAdapter(getActivity());
        mFeedList.setAdapter(mAdapter);
        mFeedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mVisibleItemCount = mLayoutManager.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                mPastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((mVisibleItemCount + mPastVisiblesItems) >= mTotalItemCount) {
                        getLooops(Constants.FETCH_BOTTOM);
                    }
                }
            }
        });
        mSwipeRefresh.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);
        mSwipeRefresh.setOnRefreshListener(this);

        return view;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void onEventMainThread(FollowResponse followResponse) {
        if (followResponse.getStatus() == 1) {
            mFollowMap.put(followResponse.getFollowedId(), 1);
            mAdapter.updateFollowMap(mFollowMap);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEvent(RefreshModel refreshModel) {
        /*if (refreshModel.getType() == 12) {
            getLooops();
        }*/
    }

    public void onEventMainThread(final FeedLooopResponse feedLooopResponse) {
        isLoading = false;
        List<Looop> looopList = feedLooopResponse.getLooops();
        for (int i = 0; i < looopList.size(); i++) {
            mFollowMap.put("" + looopList.get(i).getUserId(), looopList.get(i).getRelationship());
        }

        if(feedLooopResponse.getStatus() == 2) {
            return;
        }

        mMaxId = feedLooopResponse.getMaxId();
        mMinId = feedLooopResponse.getMinId();

        List<Looop> tempList = new ArrayList<Looop>(feedLooopResponse.getLooops());
        switch (feedLooopResponse.getResponseType()) {
            case Constants.FETCH_TOP:
                mSwipeRefresh.setRefreshing(false);
                mFeedListLooops.addAll(0,tempList);
                break;
            case Constants.FETCH_BOTTOM:
                mFeedListLooops.addAll(mFeedListLooops.size(),tempList);
                break;
            case Constants.FETCH_NORMAL:
                mFeedListLooops = tempList;
                break;
        }

        updateAdapter();
    }

    private void updateAdapter() {
        mAdapter.updateFeedsAdapter(mFeedListLooops);
        mAdapter.updateFollowMap(mFollowMap);
        mAdapter.notifyDataSetChanged();
    }

    public void onEvent(Location currentLocation) {
        mCurrentLocation = currentLocation;
        getLooops(Constants.FETCH_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    private void getLooops(int requestType) {
        if (mCurrentLocation == null) {
            return;
        }
        isLoading = true;
        FetchLoopsRequest fetchLoopsRequest = new FetchLoopsRequest();
        fetchLoopsRequest.setUserId(mPrefs.getUserId());
        fetchLoopsRequest.setLat(mCurrentLocation.getLatitude());
        fetchLoopsRequest.setLong(mCurrentLocation.getLongitude());
        fetchLoopsRequest.setMaxId(mMaxId);
        fetchLoopsRequest.setMinId(mMinId);
        fetchLoopsRequest.setFetchRequest(requestType);
        mBus.post(fetchLoopsRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        getLooops(Constants.FETCH_TOP);
    }
}
