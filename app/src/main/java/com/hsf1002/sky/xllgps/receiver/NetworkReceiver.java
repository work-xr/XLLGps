package com.hsf1002.sky.xllgps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;
import com.hsf1002.sky.xllgps.util.WakeLockUtil;


/**
 * Created by hefeng on 18-8-25.
 */

public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkReceiver";
    private Context appContext = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        appContext = context.getApplicationContext();

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d(TAG, "onReceive: CONNECTIVITY_ACTION .....................................");
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (info != null) {
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.d(TAG, "onReceive: NetworkInfo.State.CONNECTED info.type = TYPE_MOBILE");
                    }

                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        Log.d(TAG, "onReceive: NetworkInfo.State.CONNECTED info.type = TYPE_WIFI");
                    }

                    if (RxjavaHttpModel.getInstance().isReady())
                    {
                        WakeLockUtil.getInstance().acquireWakeLock("NetworkReceiver-CONNECTED");

                        RxjavaHttpModel.getInstance().pushGpsInfo();
                    }

                    return;
                }

                if (NetworkInfo.State.DISCONNECTED == info.getState())
                {
                    Log.d(TAG, "onReceive: NetworkInfo.State.DISCONNECTED ***************************");
                    RxjavaHttpModel.getInstance().resetGpsType();
                    WakeLockUtil.getInstance().releaseWakeLock("NetworkReceiver-DISCONNECTED");
                    return;
                }
            }
        }
    }
}
