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

import com.hash.looop.adapter.FeedsAdapter;
import com.hash.looop.model.FeedLooopResponse;
import com.hash.looop.model.FetchLoopsRequest;
import com.hash.looop.model.FollowResponse;
import com.hash.looop.model.Looop;
import com.hash.looop.model.RefreshModel;
import com.hash.looop.utils.MySharedPreference;
import com.hash.looop.utils.SpacesItemDecoration;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by mathan on 24/9/15.
 */
public class FeedTabFragment extends Fragment {

    @Bind(R.id.feed_list)
    RecyclerView mFeedList;
    private FeedsAdapter mAdapter;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;
    private Location mCurrentLocation;
    private HashMap<String, Integer> mFollowMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds_fragment, container, false);
        ButterKnife.bind(this, view);
        if (!mBus.isRegistered(this)) {
            mBus.register(this);
        }
        mPrefs = new MySharedPreference(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false);
        mFeedList.setLayoutManager(manager);
        mFeedList.addItemDecoration(new SpacesItemDecoration(dpToPx(5),dpToPx(5)));
        mAdapter = new FeedsAdapter(getActivity());
        mFeedList.setAdapter(mAdapter);
        /*mFeedList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                })
        );*/

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
        if (refreshModel.getType() == 12) {
            getLooops();
        }
    }

    public void onEventMainThread(final FeedLooopResponse feedLooopResponse) {
        List<Looop> looopList = feedLooopResponse.getLooops();
        for (int i = 0; i < looopList.size(); i++) {
            mFollowMap.put("" + looopList.get(i).getUserId(), looopList.get(i).getRelationship());
        }
        mAdapter.updateFeedsAdapter(feedLooopResponse.getLooops());
        mAdapter.updateFollowMap(mFollowMap);
        mAdapter.notifyDataSetChanged();
    }

    public void onEvent(Location currentLocation) {
        mCurrentLocation = currentLocation;
        getLooops();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    private void getLooops() {
        if (mCurrentLocation == null) {
            return;
        }
        FetchLoopsRequest fetchLoopsRequest = new FetchLoopsRequest();
        fetchLoopsRequest.setUserId(mPrefs.getUserId());
        fetchLoopsRequest.setLat(mCurrentLocation.getLatitude());
        fetchLoopsRequest.setLong(mCurrentLocation.getLongitude());
        mBus.post(fetchLoopsRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
