package com.hsf1002.sky.xllgps.util;

/**
 * Created by hefeng on 18-6-8.
 */

public class Const {

    public static final String URL_LV = "lv=1";
    public static final String URL_APP_ID = "&app_id=5cd12ff1222ef64d1a5227a181631a2195e1b456";
    public static final String URL_APP_SECRET = "&app_secret=0512a2c055a970f7e64381c7eee4f9da";
    public static final String URL_TOKEN = "65e5bc4c79b3430ad11e793ae9dde2b53";
    public static final String URL_TPCODE = "&tpcode=xiaobawangmobile";
    public static final String URL_TIMESTAMP = "&timestamp=1493857458";
    public static final String URL_PHONE_NUMBER = "18818681242";
    public static final String URL_ENCODE_TYPE = "UTF-8";
    public static final String URL_LV_VALUE = "1";
    public static final String URL_APP_ID_VALUE = "5cd12ff1222ef64d1a5227a181631a2195e1b456";
    public static final String URL_APP_SECRET_VALUE = "0512a2c055a970f7e64381c7eee4f9da";
    public static final String URL_SIGN_VALUE = "ee0906c15cb85385a63b349e6c27460a";
    public static final String URL_TPCODE_VALUE = "&tpcode=xiaobawangmobile";
    public static final int TIMESTAMP_START_INDEX = 0;
    public static final int TIMESTAMP_END_INDEX = 10;

    public static final String SHARED_PREFERENCE_NAME = "lxjgps_sp";

    public static final String RXJAVAHTTP_BASE_URL = "https://api.douban.com/";
    public static final int RXJAVAHTTP_READ_TIMEOUT = 100;
    public static final int RXJAVAHTTP_WRITE_TIMEOUT = 100;
    public static final int RXJAVAHTTP_CONNCET_TIMEOUT = 100;

    // 每隔多久启动一次IntentService服务来开始定位,可由服务器端通过调用setStartServiceInterval进行设置更改
    public static final int BAIDU_GPS_SCAN_SPAN_TIME_INTERVAL = 1 * 60 * 1000;
    // 每次启动百度地图服务XLJGpsService.setServiceAlarm(getApplicationContext(), true);, 如果3分钟内没有定位成功, 自动停止地图服务stopBaiduGps
    public static final int BAIDU_GPS_FIRST_SCAN_TIME_MAX = 3 * 60 * 1000;
}
