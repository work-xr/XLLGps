package com.hsf1002.sky.xllgps.params;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by hefeng on 18-6-15.
 */

public class ReportParam extends BaiduGpsParam{
    // private String mobile;   // not necessary
    private String imei;
/*
    private String coord_type;  // 坐标系 WGS84-大地坐标系  GCJ02-国家测绘局坐标系统  BD09-百度坐标系
    private String gisInfo;     // not necessary  位置信息
    private String lat;         // 纬度
    private String lng;         // 经度
    private String locTime;     // 定位时间: yyyy-MM-dd HH:mm:ss
    private String locType;     // 定位类型: 1-GPS  2-GPS_One  3-激战定位
    private String source_type; // 来源类型 1-普通定位 2-SOS定位*/

    private String timestamp;   // 调用接口的时间,是linux的时间戳,与url保持一致
    private String type;        // not necessary    1-平台定位  2-短信定位

    public ReportParam(String coord_type, String gisInfo, String imei, String lat, String lng, String locTime, String locType, String type, String source_type, String timestamp) {
        super(coord_type, gisInfo, lat, lng, locTime, locType, source_type);
        this.imei = imei;
/*
        this.coord_type = coord_type;
        this.gisInfo = gisInfo;
        this.lat = lat;
        this.lng = lng;
        this.locTime = locTime;
        this.locType = locType;
        this.source_type = source_type;*/
        this.timestamp = timestamp;
        this.type = type;
    }

    public static String getReportParamGson(ReportParam param)
    {
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.serializeNulls().create();

        return gson.toJson(param, ReportParam.class);
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
