package com.hsf1002.sky.xllgps.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hsf1002.sky.xllgps.app.XLLGpsApplication;

import static com.hsf1002.sky.xllgps.util.Constant.SHARED_PREFERENCE_NAME;

/**
 * Created by hefeng on 18-6-12.
 */

public class SharedPreUtils {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SharedPreUtils() {
        sharedPreferences = XLLGpsApplication.getAppContext().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();
    }

    public static SharedPreUtils getInstance()
    {
        return  Holder.sInstance;
    }

    private static class Holder
    {
        private static final SharedPreUtils sInstance = new SharedPreUtils();
    }

    public void sharedPreRemove(String key) {
        editor.remove(key).apply();
    }

    public int getInt(String key, int value)
    {
        return sharedPreferences.getInt(key, value);
    }

    public void putInt(String key, int value)
    {
        editor.putInt(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key, Boolean value)
    {
        return sharedPreferences.getBoolean(key, value);
    }

    public void putBoolean(String key, Boolean value)
    {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getString(String key, String value)
    {
        return sharedPreferences.getString(key, value);
    }

    public void putString(String key, String value)
    {
        editor.putString(key, value);
        editor.apply();
    }
}
