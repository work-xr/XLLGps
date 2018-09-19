package com.hsf1002.sky.xllgps.util;

/**
 * Created by hefeng on 18-6-8.
 */

public class Constant {
    public static final String URL_LV = "&lv=1";
    public static final String URL_APP_ID = "&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b";
    public static final String URL_APP_SECRET = "&app_secret=9ff96155b3f42dc7a337aa59ca59578b";
    public static final String URL_TPCODE = "&tpcode=xiaobawangmobile";

    public static final String URL_TOKEN = "65e5bc4c79b3430ad11e793ae9dde2b53";
    public static final String URL_PHONE_NUMBER = "18818681242";
    public static final String URL_ENCODE_TYPE = "UTF-8";
    public static final String URL_LV_VALUE = "1";
    public static final String URL_APP_ID_VALUE = "5cd12ff1222ef64d1a5227a181631a2195e1b456";
    public static final String URL_APP_SECRET_VALUE = "0512a2c055a970f7e64381c7eee4f9da";
    public static final String URL_SIGN_VALUE = "ee0906c15cb85385a63b349e6c27460a";
    public static final String URL_TPCODE_VALUE = "xiaobawangmobile";
    public static final int TIMESTAMP_START_INDEX = 0;
    public static final int TIMESTAMP_END_INDEX = 10;

    public static final String SHARED_PREFERENCE_NAME = "lxjgps_sp";

    public static final String RXJAVAHTTP_BASE_URL = "http://api.cloud.site4test.com";//https://api.douban.com/";
    public static final int RXJAVAHTTP_READ_TIMEOUT = 100;
    public static final int RXJAVAHTTP_WRITE_TIMEOUT = 100;
    public static final int RXJAVAHTTP_CONNCET_TIMEOUT = 100;

    public static final String LOCATION_TYPE_PLATFORM = "1";         // 平台定位
    public static final String LOCATION_TYPE_DWSMS = "2";            // DW短信定位
    public static final String LOCATION_SOURCE_TYPE_ORDINARY = "1";  // 普通定位
    public static final String LOCATION_SOURCE_TYPE_SOS = "2";       // SOS定位

	// 定位服务上报的时间间隔
    public static final int BAIDU_GPS_SERVICE_SCAN_INTERVAL = 30 * 60 * 1000;
    // 每隔多久发起一次请求
    public static final int BAIDU_GPS_SCAN_SPAN_TIME_INTERVAL = 1 * 60 * 1000;
    public static final String BAIDU_GPS_LOCATION_TYPE_GPS = "gps";
    public static final String BAIDU_GPS_LOCATION_TYPE_LBS = "lbs";
    public static final String BAIDU_GPS_LOCATION_TYPE_WIFI = "wifi";

    // 由于上传给服务器的数据不能为空, 定位数据的默认值
    public static final String BAIDU_GPS_LOCATION_DEFAULT_ADDRESS = "sz";
    public static final String BAIDU_GPS_LOCATION_DEFAULT_LONGITUDE = "4.9E-324";   // 114.044044
    public static final String BAIDU_GPS_LOCATION_DEFAULT_LANTITUDE = "4.9E-324";   // 22.522522
    public static final String BAIDU_GPS_LOCATION_DEFAULT_LOCTYPE = "3";            // 1: gps  2: gps one 3: 基站定位
    public static final String BAIDU_GPS_LOCATION_DEFAULT_LOCTIME = "2018-09-26 13:13:13";
	// 按键SOS后发此广播, 收到广播后,上传信息到平台
    public static final String ACTION_SOS_REPORT_POSITION = "action.sos.report.position";
	public static final String ACTION_POWER_ON = "android.intent.action.BOOT_COMPLETED";
	// 开启数据业务
    public static final String ACTION_ACTIVATED_CONNECTIVITY = "intent.action.ACTIVATED_CONNECTIVITY";
    // 关闭数据业务
    public static final String ACTION_INACTIVATED_CONNECTIVITY = "intent.action.INACTIVATED_CONNECTIVITY";
    public static final String BAIDU_GPS_LOCATION_DEFAULT_COORDINATE_TYPE = "GCJ02";  // WGS84：大地坐标系     GCJ02：国家测绘局坐标系统     BD09：百度坐标系

    public static final String SMS_FROM_SERVER_CONTENT_PART1 = "福建青鸟三盛";
    public static final String SMS_FROM_SERVER_CONTENT_PART2 = "老人机即时定位API";
    public static final String ACTION_SMS_FROM_SERVER = "intent.action.SEND_SERVER_MSG";

    public static final String RXJAVAHTTP_SERVER_TEST_URL = "http://api.cloud.site4test.com/thirdparty/mobile/location/list?imei=864648030095476&start=2017-10-25%2017:00:00&lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b";
    public static final String RXJAVAHTTP_SERVER_TEST_URL_PART1 = "http://api.cloud.site4test.com/thirdparty/mobile/location/list";
    public static final String RXJAVAHTTP_SERVER_TEST_URL_PART2 = "imei=864648030095476&start=2017-10-25%2017:00:00&lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b";
}
