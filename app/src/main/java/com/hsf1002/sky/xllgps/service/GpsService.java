package com.hsf1002.sky.xllgps.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.location.NetworkApp;
import com.hsf1002.sky.xllgps.location.NetworkGpsApp;
import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;

import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_SCAN_SPAN_TIME_INTERVAL;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_SOURCE_TYPE_ORDINARY;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_TYPE_PLATFORM;

/**
 * Created by hefeng on 18-6-11.
 */

public class GpsService extends Service {
    private static final String TAG = "GpsService";
    private static int startServiceInterval = BAIDU_GPS_SCAN_SPAN_TIME_INTERVAL;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        BaiduGpsApp.getInstance().startBaiduGps();

        //NetworkGpsApp.getInstance().getLocationNetworkGps();
        //NetworkApp.getInstance().getLocationNetwork();

        new Thread(new Runnable() {
            @Override
            public void run() {
                RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_TYPE_PLATFORM, LOCATION_SOURCE_TYPE_ORDINARY);
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setServiceAlarm(Context context, boolean isOn)
    {
        Intent intent = new Intent(context, GpsService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if  (isOn)
        {
            manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), startServiceInterval, pi);
        }
        else
        {
            manager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context)
    {
        Intent intent = new Intent(context, GpsService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

    public static void setStartServiceInterval(int interval)
    {
        startServiceInterval = interval;
    }
}
