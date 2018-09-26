package com.hsf1002.sky.xllgps.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hsf1002.sky.xllgps.app.GpsApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.hsf1002.sky.xllgps.util.Constant.ACTION_ACTIVATED_CONNECTIVITY;
import static com.hsf1002.sky.xllgps.util.Constant.ACTION_INACTIVATED_CONNECTIVITY;

/**
 * Created by hefeng on 18-8-27.
 */

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static boolean isNetworkAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) GpsApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        Boolean isMobileConn = networkInfo.isConnected();

        Log.i(TAG, "isNetworkAvailable: " + isMobileConn);

        return isMobileConn;
    }


    /**
    *  author:  hefeng
    *  created: 18-9-11 下午8:11
    *  desc:    网络能否ping通
    *  param:
    *  return:
    */
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    /**
     *  author:  hefeng
     *  created: 18-9-11 下午2:38
     *  desc:
     *  param:
     *  return:
     */
    public static void sendBroadCastNetworkActivated()
    {
        Intent intent = new Intent();
        intent.setAction(ACTION_ACTIVATED_CONNECTIVITY);
        GpsApplication.getAppContext().sendBroadcast(intent);
        Log.i(TAG, "sendBroadCastNetworkActivated: ");
    }

    /**
     *  author:  hefeng
     *  created: 18-9-11 下午2:39
     *  desc:
     *  param:
     *  return:
     */
    public static void sendBroadCastNetworkInactivated()
    {
        Intent intent = new Intent();
        intent.setAction(ACTION_INACTIVATED_CONNECTIVITY);
        GpsApplication.getAppContext().sendBroadcast(intent);
        Log.i(TAG, "sendBroadCastNetworkInactivated: ");
    }
}
