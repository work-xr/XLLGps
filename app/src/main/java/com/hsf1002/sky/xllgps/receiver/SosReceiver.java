package com.hsf1002.sky.xllgps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;
import com.hsf1002.sky.xllgps.util.NetworkUtils;

import static com.hsf1002.sky.xllgps.util.Constant.ACTION_SOS_REPORT_POSITION;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_SOURCE_TYPE_SOS;


/**
 * Created by hefeng on 18-7-30.
 * desc: 用户按了SOS键之后, 会发送此广播, 本应用收到广播后上报SOS信息
 */

public class SosReceiver extends BroadcastReceiver {
    private static final String TAG = "SosReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.d(TAG, "onReceive: action = " + action);

        if (action.equals(ACTION_SOS_REPORT_POSITION))
        {
            //RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_SOURCE_TYPE_SOS);
            NetworkUtils.sendBroadCastNetworkActivated();
            RxjavaHttpModel.getInstance().setGpsSourceType(LOCATION_SOURCE_TYPE_SOS);
        }
    }
}
