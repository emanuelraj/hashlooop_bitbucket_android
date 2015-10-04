package com.hash.loop;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hash.loop.model.PostLooopRequest;
import com.hash.loop.model.PostLooopResponse;
import com.hash.loop.utils.MySharedPreference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ComposeLooopActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Bind(R.id.status_field)
    EditText mStatusField;
    @Bind(R.id.post)
    TextView mPost;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_status);
        ButterKnife.bind(this);
        mBus.register(this);
        mPrefs = new MySharedPreference(getApplicationContext());
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void onEvent(PostLooopResponse postLooopResponse) {
        if(postLooopResponse.getStatus() == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Looop posted succesfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @OnClick(R.id.post)
    public void postNewLooop() {
        if(mCurrentLocation == null) {
            Toast.makeText(getApplicationContext(),"Please enable location service", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        PostLooopRequest postLooopRequest = new PostLooopRequest();
        postLooopRequest.setUserId(mPrefs.getUserId());
        postLooopRequest.setLat(mCurrentLocation.getLatitude());
        postLooopRequest.setLong(mCurrentLocation.getLongitude());
        postLooopRequest.setStatus(mStatusField.getText().toString());
        mBus.post(postLooopRequest);
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
                            status.startResolutionForResult(ComposeLooopActivity.this, 1000);
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

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        mCurrentLocation = location;
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (mCurrentLocation != null) {
                stopLocationUpdates();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub
        Log.i("ERROR", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        //Toast.makeText(this, "Failed in service ", Toast.LENGTH_SHORT).show();
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
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
                stopLocationUpdates();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            }
        }
        startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        mBus.unregister(this);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
