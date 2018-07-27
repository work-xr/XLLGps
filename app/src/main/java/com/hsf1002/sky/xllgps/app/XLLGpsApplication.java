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

public class XLLGpsApplication extends Application {
    private static final String TAG = "XLLGpsApplication";
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");
        sContext = getApplicationContext();
        BaiduGpsApp.getInstance().initBaiduSDK(sContext);
        //startService(new Intent(this, GpsService.class));
        GpsService.setServiceAlarm(getApplicationContext(), true);

        rxjavaHttpInit();
    }

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

    public static Context getAppContext()
    {
        return sContext;
    }
}
