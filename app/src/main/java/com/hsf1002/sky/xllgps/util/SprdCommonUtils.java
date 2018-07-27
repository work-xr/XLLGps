package com.hsf1002.sky.xllgps.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hsf1002.sky.xllgps.app.XLLGpsApplication;

/**
 * Created by hefeng on 18-6-11.
 */

public class SprdCommonUtils {
    private static final String TAG = "SprdCommonUtils";

    public String getIMEI()
    {
        TelephonyManager telephonyManager = (TelephonyManager) XLLGpsApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = null;
        //int phoneCount = telephonyManager.getPhoneCount();
        //Log.d(TAG, "getIMEI: " + phoneCount);

        for (int slot = 0; slot < 2/*phoneCount*/; slot++)
        {
            try {
                deviceId = telephonyManager.getDeviceId();
            }catch (SecurityException e)
            {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(deviceId))
            {
                Log.d(TAG, "getIMEI: " + deviceId + ", i = " + slot);
                break;
            }
        }

        return deviceId;
    }

    public static SprdCommonUtils getInstance()
    {
        return Holder.instance;
    }

    private static class Holder
    {
        private static final SprdCommonUtils instance = new SprdCommonUtils();
    }
}
