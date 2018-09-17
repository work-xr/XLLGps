package com.hsf1002.sky.xllgps.params;

/**
 * Created by hefeng on 18-6-15.
 */

public class BaiduGpsParam {
    protected String coord_type;  // 坐标系 WGS84-大地坐标系  GCJ02-国家测绘局坐标系统  BD09-百度坐标系
    protected String gisInfo;     // not necessary  位置信息
    protected String lat;         // 纬度
    protected String lng;         // 经度
    protected String locTime;     // 定位时间: yyyy-MM-dd HH:mm:ss
    protected String locType;     // 定位类型: 1-GPS  2-GPS_One  3-激战定位
    //protected String source_type; // 来源类型 1-普通定位 2-SOS定位

    public BaiduGpsParam() {
    }

    public BaiduGpsParam(String coord_type, String gisInfo, String lat, String lng, String locTime, String locType, String source_type) {
        this.coord_type = coord_type;
        this.gisInfo = gisInfo;
        this.lat = lat;
        this.lng = lng;
        this.locTime = locTime;
        this.locType = locType;
    }

    public String getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(String coord_type) {
        this.coord_type = coord_type;
    }

    public String getGisInfo() {
        return gisInfo;
    }

    public void setGisInfo(String gisInfo) {
        this.gisInfo = gisInfo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocTime() {
        return locTime;
    }

    public void setLocTime(String locTime) {
        this.locTime = locTime;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    @Override
    public String toString() {
        return "BaiduGpsParam{" +
                "coord_type='" + coord_type + '\'' +
                ", gisInfo='" + gisInfo + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", locTime='" + locTime + '\'' +
                ", locType='" + locType + '\'' +
                '}';
    }
}
