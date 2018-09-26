package com.hsf1002.sky.xllgps.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.hsf1002.sky.xllgps.app.GpsApplication;
import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;
import com.hsf1002.sky.xllgps.util.NetworkUtils;

import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_SERVICE_SCAN_INTERVAL;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_SOURCE_TYPE_ORDINARY;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_TYPE_DWSMS;

/**
 * Created by hefeng on 18-6-11.
 */

public class GpsService extends Service {
    private static final String TAG = "GpsService";
    private static int startServiceInterval = BAIDU_GPS_SERVICE_SCAN_INTERVAL;
    private static Context sContext = null;
    private static final String ACTION_TIMING_REPORT_GPS_LOCATION = "action.timing.report.gps.location";
    private static Intent sIntentReceiver = new Intent(ACTION_TIMING_REPORT_GPS_LOCATION);
    private static PendingIntent sPendingIntent = null;
    private static AlarmManager sManager = null;

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午5:36
    *  desc:       
    *  param:   
    *  return:  
    */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        sContext = GpsApplication.getAppContext();
        registerGpsReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午7:27
    *  desc:    开启或关闭服务
    *  param:
    *  return:
    */
    public static void setServiceAlarm(Context context, boolean isOn)
    {
        Intent intent = new Intent(context, GpsService.class);
        sContext = GpsApplication.getAppContext();
        sContext.startService(intent);

        sIntentReceiver = new Intent(ACTION_TIMING_REPORT_GPS_LOCATION);
        sPendingIntent = PendingIntent.getBroadcast(context, 0, sIntentReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        sManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Log.e(TAG, "setServiceAlarm: startServiceInterval = " + startServiceInterval + ", isOn = " + isOn);

        if (isOn)
        {
            // 刚开机, 刚开启定时定位服务的第一次, 不上报定位信息, 30分钟后再上报
            //sManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + startServiceInterval, startServiceInterval, pi);
            //sManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
            sManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + startServiceInterval, sPendingIntent);
        }
        else
        {
            sManager.cancel(sPendingIntent);
            sPendingIntent.cancel();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterGpsReceiver();
    }

    /**
     *  author:  hefeng
     *  created: 18-8-22 上午9:04
     *  desc:
     *  param:
     *  return:
     */
    private void registerGpsReceiver()
    {
        IntentFilter intentFilter = new IntentFilter(ACTION_TIMING_REPORT_GPS_LOCATION);
        sContext.registerReceiver(gpsReceiver, intentFilter);
    }

    /**
     *  author:  hefeng
     *  created: 18-8-22 上午9:04
     *  desc:
     *  param:
     *  return:
     */
    private void unregisterGpsReceiver()
    {
        sContext.unregisterReceiver(gpsReceiver);
    }

    /**
     *  author:  hefeng
     *  created: 18-8-22 上午9:00
     *  desc:    循环发送广播
     *  0. 不管是按SOS, 定时还是实时请求定位, 先
     *  1. 发送广播给sos 打开数据业务
     *  2. 打开WakeLock
     *  3. 收到数据业务打开的广播, 开启百度定位, 并上报位置信息
     *  4. 发送广播给sos 关闭数据业务
     *  5. 收到数据业务关闭的广播, 关闭WakeLock
     *  param:
     *  return:
     */
    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: gpsReceiver startServiceInterval = " + startServiceInterval);

            //BaiduGpsApp.getInstance().startBaiduGps();
            //RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_SOURCE_TYPE_ORDINARY);
            // 只是去打开数据业务
            NetworkUtils.sendBroadCastNetworkActivated();
            RxjavaHttpModel.getInstance().setGpsSourceType(LOCATION_SOURCE_TYPE_ORDINARY);

            // setExact 无法唤醒
            //sManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startServiceInterval, sPendingIntent);
            sManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + startServiceInterval, sPendingIntent);
        }
    };
}
