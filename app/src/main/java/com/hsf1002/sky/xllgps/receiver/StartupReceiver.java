package com.hsf1002.sky.xllgps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.hsf1002.sky.xllgps.util.Constant.ACTION_POWER_ON;

/**
 * Created by hefeng on 18-6-6.
 */

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: action = " + action);

        if (action.equals(ACTION_POWER_ON))
        {

        }
    }
}
