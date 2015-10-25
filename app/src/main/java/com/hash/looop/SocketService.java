package com.hash.looop;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hash.looop.model.FeedLooopResponse;
import com.hash.looop.model.FetchLoopsRequest;
import com.hash.looop.model.FollowRequest;
import com.hash.looop.model.FollowResponse;
import com.hash.looop.model.LoginRequestModel;
import com.hash.looop.model.LoginResponseModel;
import com.hash.looop.model.LooopLikeRequest;
import com.hash.looop.model.PostLooopImage;
import com.hash.looop.model.PostLooopRequest;
import com.hash.looop.model.PostLooopResponse;
import com.hash.looop.model.RegistrationRequestModel;
import com.hash.looop.model.RegistrationResponseModel;
import com.hash.looop.model.TrendingLooopsRequest;
import com.hash.looop.model.TrendingLooopsResponse;
import com.hash.looop.utils.Constants;
import com.hash.looop.utils.MySharedPreference;

import java.net.URISyntaxException;

import de.greenrobot.event.EventBus;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

/**
 * Created by mathan on 26/8/15.
 */
public class SocketService extends Service {

    private static final String TAG = "!Looop";
    private Socket mSocket;
    private EventBus mEventBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "STATUS");
            Timber.e("Error");
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        }
    };

    private Emitter.Listener onTimeoutError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "STATUS");
            Timber.e("Timeout");
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onTimeoutError);
        }
    };

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "STATUS");
            Timber.e("Connected");
            if (!TextUtils.isEmpty(mPrefs.getUserId())) {
                updateSocketId();
            }
        }
    };

    private Emitter.Listener onRegistrationResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            RegistrationResponseModel responseModel = gson.fromJson(args[0].toString(),
                    RegistrationResponseModel.class);
            mEventBus.post(responseModel);
        }

    };

    private Emitter.Listener onLoginSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            LoginResponseModel loginResponseModel = gson.fromJson(args[0].toString(), LoginResponseModel.class);
            mEventBus.post(loginResponseModel);
        }
    };

    private Emitter.Listener onSocketUpdateSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
        }
    };

    private Emitter.Listener onLikeResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
        }
    };

    private Emitter.Listener onPostLooopResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            PostLooopResponse postLooopResponse = gson.fromJson(args[0].toString(),
                    PostLooopResponse.class);
            mEventBus.post(postLooopResponse);
        }
    };

    private Emitter.Listener onLooopsFeedResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            FeedLooopResponse feedLooopResponse = gson.fromJson(args[0].toString(),
                    FeedLooopResponse.class);
            mEventBus.post(feedLooopResponse);
        }
    };

    private Emitter.Listener onTrendingLooopResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED" + "Trending");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            TrendingLooopsResponse trendingLooopsResponse = gson.fromJson(args[0].toString(),
                    TrendingLooopsResponse.class);
            mEventBus.post(trendingLooopsResponse);
        }
    };

    private Emitter.Listener onFollowResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            FollowResponse followResponse = gson.fromJson(args[0].toString(),
                    FollowResponse.class);
            mEventBus.post(followResponse);
        }
    };

    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onTimeoutError);
        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        setSocketListeners();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
        return START_STICKY;
    }

    public void onEvent(RegistrationRequestModel registrationRequestModel) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(gson.toJson(registrationRequestModel));
        mSocket.emit(Constants.REGISTRATION_REQUEST, gson.toJson(registrationRequestModel));
    }

    private void setSocketListeners() {
        mSocket.on(Constants.REGISTRATION_RESPONSE, onRegistrationResponse);
        mSocket.on(Constants.LOGIN_RESPONSE, onLoginSuccess);
        mSocket.on(Constants.LOOOP_POST_RESPONSE, onPostLooopResponse);
        mSocket.on(Constants.FETCH_LOOOPS_RESPONSE, onLooopsFeedResponse);
        mSocket.on(Constants.RECONNECT_RESPONSE, onSocketUpdateSuccess);
        mSocket.on(Constants.LIKE_RESPONSE, onLikeResponse);
        mSocket.on(Constants.TRENDING_LOOOPS_RESPONSE, onTrendingLooopResponse);
        mSocket.on(Constants.FOLLOW_RESPONSE, onFollowResponse);
    }

    private void updateSocketId() {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", mPrefs.getUserId());
        Timber.d(Constants.RECONNECT + " - " + gson.toJson(jsonObject));
        mSocket.emit(Constants.RECONNECT, jsonObject);
    }

    public void onEvent(LoginRequestModel requestModel) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.LOGIN_REQUEST + " - " + gson.toJson(requestModel));
        mSocket.emit(Constants.LOGIN_REQUEST, gson.toJson(requestModel));
    }

    public void onEvent(PostLooopRequest postLooopRequest) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.NEW_LOOOP + " - " + gson.toJson(postLooopRequest));
        mSocket.emit(Constants.NEW_LOOOP, gson.toJson(postLooopRequest));
    }

    public void onEvent(FetchLoopsRequest fetchLoopsRequest) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.FETCH_LOOOPS_REQUEST + " - " + gson.toJson(fetchLoopsRequest));
        mSocket.emit(Constants.FETCH_LOOOPS_REQUEST, gson.toJson(fetchLoopsRequest));
    }

    public void onEvent(LooopLikeRequest loopLikeRequest) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.LIKE_REQUEST + " - " + gson.toJson(loopLikeRequest));
        mSocket.emit(Constants.LIKE_REQUEST, gson.toJson(loopLikeRequest));
    }

    public void onEvent(TrendingLooopsRequest trendingLooopsRequest) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.TRENDING_LOOOPS_REQUEST + " - " + gson.toJson(trendingLooopsRequest));
        mSocket.emit(Constants.TRENDING_LOOOPS_REQUEST, gson.toJson(trendingLooopsRequest));
    }

    public void onEvent(FollowRequest followRequest) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.NEW_FOLLOW_REQUEST + " - " + gson.toJson(followRequest));
        mSocket.emit(Constants.NEW_FOLLOW_REQUEST, gson.toJson(followRequest));
    }

    public void onEvent(PostLooopImage postLooopImage) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(Constants.NEW_IMAGE_LOOOP+ " - " + gson.toJson(postLooopImage));
        mSocket.emit(Constants.NEW_IMAGE_LOOOP, gson.toJson(postLooopImage));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onTimeoutError);
        mEventBus.unregister(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus.register(this);
        mPrefs = new MySharedPreference(getApplicationContext());
    }


}
