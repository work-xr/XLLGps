package com.hsf1002.sky.xllgps.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.service.GpsService;

import static com.hsf1002.sky.xllgps.util.Constant.RXJAVAHTTP_BASE_URL;
import static com.hsf1002.sky.xllgps.util.Constant.RXJAVAHTTP_CONNCET_TIMEOUT;
import static com.hsf1002.sky.xllgps.util.Constant.RXJAVAHTTP_READ_TIMEOUT;
import static com.hsf1002.sky.xllgps.util.Constant.RXJAVAHTTP_WRITE_TIMEOUT;

/**
 * Created by hefeng on 18-6-6.
 */

public class GpsApplication extends Application {
    private static final String TAG = "GpsApplication";
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");
        sContext = getApplicationContext();

        BaiduGpsApp.getInstance().initBaiduSDK(sContext);
        GpsService.setServiceAlarm(sContext, true);

        rxjavaHttpInit();
    }

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午7:20
    *  desc:    初始化RxJava配置
    *  param:
    *  return:
    */
    private void rxjavaHttpInit()
    {
        RxHttpUtils.init(this);
        RxHttpUtils.getInstance()
                .config()
                .setBaseUrl(RXJAVAHTTP_BASE_URL)
                .setCookie(false)
                .setSslSocketFactory()
                .setReadTimeout(RXJAVAHTTP_READ_TIMEOUT)
                .setWriteTimeout(RXJAVAHTTP_WRITE_TIMEOUT)
                .setConnectTimeout(RXJAVAHTTP_CONNCET_TIMEOUT)
                .setLog(true);
    }

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午7:20
    *  desc:    获取应用的context
    *  param:
    *  return:
    */
    public static Context getAppContext()
    {
        return sContext;
    }
}
