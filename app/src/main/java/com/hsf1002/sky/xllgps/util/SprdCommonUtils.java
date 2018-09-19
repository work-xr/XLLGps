package com.hsf1002.sky.xllgps.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hsf1002.sky.xllgps.app.GpsApplication;

/**
 * Created by hefeng on 18-6-11.
 */

public class SprdCommonUtils {
    private static final String TAG = "SprdCommonUtils";

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午7:25
    *  desc:    获取手机IMEI号码
    *  param:
    *  return:
    */
    public String getIMEI()
    {
        TelephonyManager telephonyManager = (TelephonyManager) sAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = null;
        //int phoneCount = telephonyManager.getPhoneCount();    // Android4.4 不支持此方法

        //Log.i(TAG, "getIMEI: start " );

        //for (int slot = 0; slot < phoneCount; slot++)
        {
            try {
                deviceId = telephonyManager.getDeviceId();
            }catch (SecurityException e)
            {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(deviceId))
            {
               // Log.i(TAG, "getIMEI: imei = " + deviceId );
               // break;
            }
        }

        return deviceId;
    }

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午7:25
    *  desc:    创建单例
    *  param:
    *  return:
    */
    public static SprdCommonUtils getInstance()
    {
        return Holder.instance;
    }

    private static class Holder
    {
        private static final SprdCommonUtils instance = new SprdCommonUtils();
    }
}
