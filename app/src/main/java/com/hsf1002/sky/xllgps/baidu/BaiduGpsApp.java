package com.hsf1002.sky.xllgps.baidu;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hsf1002.sky.xllgps.app.GpsApplication;
import com.hsf1002.sky.xllgps.params.BaiduGpsParam;

import static android.content.Context.WIFI_SERVICE;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_DEFAULT_ADDRESS;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_DEFAULT_COORDINATE_TYPE;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_DEFAULT_LANTITUDE;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_DEFAULT_LOCTIME;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_DEFAULT_LOCTYPE;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_DEFAULT_LONGITUDE;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_SCAN_TIMEOUT;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_TYPE_GPS;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_TYPE_LBS;
import static com.hsf1002.sky.xllgps.util.Constant.BAIDU_GPS_LOCATION_TYPE_WIFI;


/**
 * Created by hefeng on 18-6-6.
 */

public class BaiduGpsApp {
    private static final String TAG = "BaiduGpsApp";
    private LocationClient client;
    private MyLocationLister myLocationLister;
    private LocationClientOption option;
    private static BaiduGpsParam sBaiduGpsMsgBean;

    /**
    *  author:  hefeng
    *  created: 18-8-13 下午6:33
    *  desc:    初始化定位, 开机就要上传定位信息
    *  param:
    *  return:
    */
    static
    {
        sBaiduGpsMsgBean = new BaiduGpsParam();
        setBaiduGpsStatus(null, null, null, null, null, null);
    }

    public static BaiduGpsApp getInstance()
    {
        return Holder.sInstance;
    }

    private static class Holder
    {
        private static final BaiduGpsApp sInstance = new BaiduGpsApp();
    }

    public void initBaiduSDK(Context context)
    {
        Log.d(TAG, "initBaiduSDK: ");
        myLocationLister = new MyLocationLister();
        client = new LocationClient(context);
        initLocation();
    }

    private void initLocation()
    {
        Log.d(TAG, "initLocation: ");
        option = new LocationClientOption();
        // LocationMode.Hight_Accuracy：高精度； LocationMode. Battery_Saving：低功耗； LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //可选，设置返回经纬度坐标类型，默认gcj02, gcj02：国测局坐标； d09ll：百度经纬度坐标； bd09：百度墨卡托坐标； 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        //option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        //可选，设置发起定位请求的间隔，int类型，单位ms, 如果设置为0，则代表单次定位，即仅定位一次，默认为0, 如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(BAIDU_GPS_LOCATION_SCAN_TIMEOUT);
        //可选，设置是否使用gps，默认false, 使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(true);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        //option.setEnableSimulateGps(false);
        //mLocationClient为第二步初始化过的LocationClient对象, 需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        client.setLocOption(option);
    }

    public void startBaiduGps()
    {
        Log.d(TAG, "startBaiduGps: isStarted = " + client.isStarted());
        if (!client.isStarted())
        {
            client.registerLocationListener(myLocationLister);
            client.start();
        }
    }

    public void stopBaiduGps() {
        Log.d(TAG, "stopBaiduGps: isStarted = " + client.isStarted());
        if (client.isStarted()) 
        {
            client.unRegisterLocationListener(myLocationLister);
            client.stop();
        }
    }

    public void restartBaiduGps()
    {
        Log.d(TAG, "restartBaiduGps: ");
        client.registerLocationListener(myLocationLister);
        client.restart();
    }

    public void setBaiduGpsScanSpan(int span)
    {
        Log.d(TAG, "setBaiduGpsScanSpan: ");
        option.setScanSpan(span);
    }

    /**
    *  author:  hefeng
    *  created: 18-8-6 上午9:58
    *  desc:
    * http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/addition-func/error-code
    * bdLocation.getLocType()
    * 若返回值是162~167，请将错误码、IMEI、定位唯一标识（自v7.2版本起，通过BDLocation.getLocationID方法获取）和定位时间反馈至邮箱loc-bugs@baidu.com
    *   61: GPS success
    *   62: 无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位
    *   63: 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位
    *   161: Network Success
    *   162: 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件
    *   167: 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位
    *   505: AK不存在或者非法，请按照说明文档重新申请AK(1. 将申请的AK写入AndroidMenifest.xml 2. AS->Build中Generated Signed APK再进行安装)
    *  在应用内部, 断网, 还是会取到上次联网时的定位, 退出应用再进入则是断网 63 状态, 此时联网则可正常定位
    */
    public class MyLocationLister extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String latitude = String.valueOf(bdLocation.getLatitude());
            String longitude = String.valueOf(bdLocation.getLongitude());
            String locationType = BAIDU_GPS_LOCATION_TYPE_LBS;
            int locType = bdLocation.getLocType();
            String locTypeStr = "1";        // 1->"Baidu" 2->"GaoDe";
            String locTime = bdLocation.getTime();
            StringBuilder address = new StringBuilder();

            address.append(bdLocation.getCountry()).append(bdLocation.getCity()).append(bdLocation.getDistrict()).append(bdLocation.getStreet());

            if (locType == BDLocation.TypeGpsLocation)
            {
                locationType = BAIDU_GPS_LOCATION_TYPE_GPS;
            }
            else if (locType == BDLocation.TypeNetWorkLocation)
            {
                WifiManager wifiManager = (WifiManager) GpsApplication.getAppContext().getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED && wifiInfo != null)
                {
                    String ssid = wifiInfo.getSSID();
                    Log.i(TAG, "onReceiveLocation: ssid = " + ssid);

                    if (!TextUtils.isEmpty(ssid))
                    {
                        locationType = BAIDU_GPS_LOCATION_TYPE_WIFI;
                    }
                }
                else
                {
                    locationType = BAIDU_GPS_LOCATION_TYPE_LBS;
                }
            }
            else
            {
                locationType = "null";
            }
            Log.i(TAG, "onReceiveLocation: locType = " + locType + ", latitude = " + latitude + ", longitude = " + longitude + ", address = " + address.toString() + ", locationType = " + locationType + ", getLocType = " + locType + ", locTime = " + locTime);

            if (locType == BDLocation.TypeGpsLocation || locType == BDLocation.TypeNetWorkLocation)
            {
                Log.i(TAG, "onReceiveLocation:  get location success, baidu service continue");
                // 保存定位信息
                setBaiduGpsStatus(address.toString(), latitude, longitude, locTypeStr, locTime, locationType);
            }
            else
            {
                // 如果定位失败, 隔一段时间会重新发起定位请求, 目前默认设置为1分钟 BAIDU_GPS_SCAN_SPAN_TIME_INTERVAL
                Log.i(TAG, "onReceiveLocation:  get location failed, baidu service continue");
            }

            stopBaiduGps();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }

        @Override
        public void onLocDiagnosticMessage(int i, int i1, String s) {
            super.onLocDiagnosticMessage(i, i1, s);
        }
    }

    /**
    *  author:  hefeng
    *  created: 18-8-6 上午11:23
    *  desc:    需要保证上传的参数的值不为空, 否则上传失败, 如果定位失败, 给一个默认值
    *  param:
    *  return:
    */
    private static void setBaiduGpsStatus(String address,
                                          String latitude,
                                          String longitude,
                                          String locType,
                                          String locTime,
                                          String coordinate_type)
    {
        if (TextUtils.isEmpty(address))
        {
            sBaiduGpsMsgBean.setGisInfo(BAIDU_GPS_LOCATION_DEFAULT_ADDRESS);
        }
        else
        {
            sBaiduGpsMsgBean.setGisInfo(address);
        }

        if (TextUtils.isEmpty(latitude))
        {
            sBaiduGpsMsgBean.setLat(BAIDU_GPS_LOCATION_DEFAULT_LANTITUDE);
        }
        else
        {
            sBaiduGpsMsgBean.setLat(latitude);
        }

        if (TextUtils.isEmpty(longitude))
        {
            sBaiduGpsMsgBean.setLng(BAIDU_GPS_LOCATION_DEFAULT_LONGITUDE);
        }
        else
        {
            sBaiduGpsMsgBean.setLng(longitude);
        }

        if (TextUtils.isEmpty(locType))
        {
            sBaiduGpsMsgBean.setLocType(BAIDU_GPS_LOCATION_DEFAULT_LOCTYPE);
        }
        else
        {
            sBaiduGpsMsgBean.setLocType(locType);
        }

        if (TextUtils.isEmpty(locTime))
        {
            sBaiduGpsMsgBean.setLocTime(BAIDU_GPS_LOCATION_DEFAULT_LOCTIME);
        }
        else
        {
            sBaiduGpsMsgBean.setLocTime(locTime);
        }

        if (TextUtils.isEmpty(coordinate_type))
        {
            sBaiduGpsMsgBean.setCoord_type(BAIDU_GPS_LOCATION_DEFAULT_COORDINATE_TYPE);
        }
        else
        {
            sBaiduGpsMsgBean.setCoord_type(coordinate_type);
        }
    }

    /**
    *  author:  hefeng
    *  created: 18-8-7 上午9:32
    *  desc:    取得当前GPS定位信息
    *  param:
    *  return:
    */
    public BaiduGpsParam getBaiduGpsStatus()
    {
        return sBaiduGpsMsgBean;
    }
}
