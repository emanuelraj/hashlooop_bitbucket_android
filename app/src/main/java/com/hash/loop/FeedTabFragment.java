package com.hash.loop;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hash.loop.adapter.FeedsAdapter;
import com.hash.loop.model.FeedLooopResponse;
import com.hash.loop.model.FetchLoopsRequest;
import com.hash.loop.utils.MySharedPreference;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by mathan on 24/9/15.
 */
public class FeedTabFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Bind(R.id.feed_list)
    RecyclerView mFeedList;
    private FeedsAdapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds_fragment, container, false);
        ButterKnife.bind(this, view);
        if(!mBus.isRegistered(this)) {
            mBus.register(this);
        }
        buildGoogleApiClient();
        mPrefs = new MySharedPreference(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false);
        mFeedList.setLayoutManager(manager);
        mAdapter = new FeedsAdapter(getActivity());
        mFeedList.setAdapter(mAdapter);

        return view;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void onEvent(final FeedLooopResponse feedLooopResponse) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateFeedsAdapter(feedLooopResponse.getLooops());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        mCurrentLocation = location;
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (mCurrentLocation != null) {
                getLooops();
                stopLocationUpdates();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCurrentLocation != null) {
            getLooops();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub
        Log.i("ERROR", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        //Toast.makeText(this, "Failed in service ", Toast.LENGTH_SHORT).show();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBus.unregister(this);
        stopLocationUpdates();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (mCurrentLocation != null) {
                getLooops();
                stopLocationUpdates();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            }
        }
        startLocationUpdates();
    }

    private void getLooops() {
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
