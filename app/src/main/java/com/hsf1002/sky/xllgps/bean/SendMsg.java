package com.hsf1002.sky.xllgps.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by hefeng on 18-6-12.
 */

public class SendMsg {
    String longitude;
    String lantitude;
    String locTime;
    String locType;
    String type;
    String address;
    String imei;
    String mobile;
    String source_type;
    String coord_type;
    String timestamp;
    String token;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLantitude() {
        return lantitude;
    }

    public void setLantitude(String latitude) {
        this.lantitude = latitude;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(String coord_type) {
        this.coord_type = coord_type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        //return "longtitude = " + longitude + ", lantitude = " + lantitude + ", locTime = " + locTime + ", locType = " + locType + ", type = " + type + ", address = " + address + ", source_type = " + source_type + ", coord_type = " + coord_type;
        return "&coord_type=" + coord_type +
                "&gisInfo=" + address +
                "&imei=" + imei +
                "&lat=" + lantitude +
                "&lng=" + longitude +
                "&locTime=" + locTime +
                "&locType=" + locType +
                "&mobile=" + mobile +
                "&source_type=" + source_type +
                "&timestamp=" + timestamp +
                //"&token=" + token +
                "&type=" + type;
    }

    public String toEncodeString(String encode) throws UnsupportedEncodingException {
        //return "longtitude = " + longitude + ", lantitude = " + lantitude + ", locTime = " + locTime + ", locType = " + locType + ", type = " + type + ", address = " + address + ", source_type = " + source_type + ", coord_type = " + coord_type;
        return "&coord_type=" + URLEncoder.encode(coord_type, encode) +
                //"&gisInfo=" + URLEncoder.encode(address, encode) +
                "&imei=" + URLEncoder.encode(imei, encode) +
                "&lat=" + URLEncoder.encode(lantitude, encode) +
                "&lng=" + URLEncoder.encode(longitude, encode) +
                "&locTime=" + URLEncoder.encode(locTime, encode) +
                "&locType=" + URLEncoder.encode(locType, encode) +
                //"&mobile=" + URLEncoder.encode(mobile, encode) +
                "&source_type=" + URLEncoder.encode(source_type, encode) +
                "&timestamp=" + URLEncoder.encode(timestamp, encode);
                //"&token=" + token +
                //"&type=" + URLEncoder.encode(type, encode);
    }
}
