package com.hsf1002.sky.xllgps.baidu;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hsf1002.sky.xllgps.bean.SendMsg;
import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;
import com.hsf1002.sky.xllgps.util.SprdCommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.hsf1002.sky.xllgps.util.Const.BAIDU_GPS_FIRST_SCAN_TIME_MAX;
import static com.hsf1002.sky.xllgps.util.Const.TIMESTAMP_END_INDEX;
import static com.hsf1002.sky.xllgps.util.Const.TIMESTAMP_START_INDEX;
import static com.hsf1002.sky.xllgps.util.Const.URL_PHONE_NUMBER;
import static com.hsf1002.sky.xllgps.util.Const.URL_TOKEN;


/**
 * Created by hefeng on 18-6-6.
 */

public class BaiduGpsApp {
    private static final String TAG = "BaiduGpsApp";
    private LocationClient client;
    private MyLocationLister myLocationLister;
    private LocationClientOption option;
    private static long startTime = 0;
    private static long currentTime = 0;
    private SendMsg sendMsg;

    BaiduGpsApp()
    {
        sendMsg = new SendMsg();
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
        startTime = System.currentTimeMillis();
        initLocation();
    }

    private void initLocation()
    {
        Log.d(TAG, "initLocation: ");
        option = new LocationClientOption();
        // LocationMode.Hight_Accuracy：高精度； LocationMode. Battery_Saving：低功耗； LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置返回经纬度坐标类型，默认gcj02, gcj02：国测局坐标； d09ll：百度经纬度坐标； bd09：百度墨卡托坐标； 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        //option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        //可选，设置发起定位请求的间隔，int类型，单位ms, 如果设置为0，则代表单次定位，即仅定位一次，默认为0, 如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(5000);
        //可选，设置是否使用gps，默认false, 使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
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

    public class MyLocationLister extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder curPosition = new StringBuilder();
            curPosition.append("Lantitude: ").append(bdLocation.getLatitude()).append(", ");
            curPosition.append("Longtitude:").append(bdLocation.getLongitude()).append(", ");
            curPosition.append("Country: ").append(bdLocation.getCountry()).append(", ");
            curPosition.append("City:").append(bdLocation.getCity()).append(", ");
            curPosition.append("District: ").append(bdLocation.getDistrict()).append(", ");
            curPosition.append("Street:").append(bdLocation.getStreet()).append(", ");
            curPosition.append("location way: ");

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation)
            {
                curPosition.append("GPS");
            }
            else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) 
            {
                curPosition.append("NETWORK");
            }
            else
            {
                curPosition.append("null");         
            }
            
            currentTime = System.currentTimeMillis();
            //Log.d(TAG, "onReceiveLocation: startTimie = " + startTime + ", currentTime = " + currentTime);
            if (currentTime - startTime >= BAIDU_GPS_FIRST_SCAN_TIME_MAX)
            {
                //Log.d(TAG, "onReceiveLocation: cost too long(larger than) " + BAIDU_GPS_FIRST_SCAN_TIME_MAX/1000 + " seconds to locate, timeout, stop gps..........");
                stopBaiduGps();
                return;
            }
/*
* http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/addition-func/error-code
*若返回值是162~167，请将错误码、IMEI、定位唯一标识（自v7.2版本起，通过BDLocation.getLocationID方法获取）和定位时间反馈至邮箱loc-bugs@baidu.com
*           61: GPS success
*           62: 无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位
*           63: 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位
*           161: Network Success
*           162: 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件
*           167: 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位
*           505: AK不存在或者非法，请按照说明文档重新申请AK(1. 将申请的AK写入AndroidMenifest.xml 2. AS->Build中Generated Signed APK再进行安装)
*  在应用内部, 断网, 还是会取到上次联网时的定位, 退出应用再进入则是断网 63 状态, 此时联网则可正常定位
* */
            //Log.d(TAG, "onReceiveLocation: getLocType = " + bdLocation.getLocType() + ", curPosition = " + curPosition);

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)
            {
                Log.d(TAG, "onReceiveLocation: 0   get location success, stop gps service");
                setSendMsg(String.valueOf(bdLocation.getLongitude()),
                        String.valueOf(bdLocation.getLatitude()),
                        String.valueOf(System.nanoTime()),
                        getLocType(bdLocation.getLocType()),
                        "1",
                        bdLocation.getCountry() + bdLocation.getCity() + bdLocation.getDistrict() + bdLocation.getStreet(),
                        1,
                        "gcj02"
                        );
                RxjavaHttpModel.getInstance().pushGpsInfo();
                stopBaiduGps();
            }
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
    private void setSendMsg(String longitude,
                            String latitude,
                            String locTime,
                            String locType,
                            String type,
                            String address,
                            int source_type,
                            String coord_type)
    {
        Date date= new Date();
        SimpleDateFormat smft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 2016-04-26 13:22:22
        String nowString=smft.format(date.getTime());

        sendMsg.setLongitude(longitude);
        sendMsg.setLantitude(latitude);
        sendMsg.setLocTime(nowString);
        sendMsg.setLocType(locType);
        sendMsg.setType(type);
        sendMsg.setAddress(address);
        sendMsg.setSource_type(String.valueOf(source_type));
        sendMsg.setCoord_type(coord_type);
        sendMsg.setMobile(URL_PHONE_NUMBER);
        sendMsg.setImei(SprdCommonUtils.getInstance().getIMEI());
        sendMsg.setTimestamp(String.valueOf(System.currentTimeMillis()).substring(TIMESTAMP_START_INDEX, TIMESTAMP_END_INDEX));
        sendMsg.setToken(URL_TOKEN);
    }

    private String getLocType(int type)
    {
        String locType = "0";

        switch (type)
        {
            case BDLocation.TypeGpsLocation:
                locType = "1";
                break;
            case BDLocation.TypeNetWorkLocation:
                locType = "3";
                break;

            default:
                break;
        }

        return locType;
    }

    public SendMsg getSendMsg()
    {
        return sendMsg;
    }
}
