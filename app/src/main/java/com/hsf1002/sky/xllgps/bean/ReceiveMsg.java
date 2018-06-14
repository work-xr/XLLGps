package com.hsf1002.sky.xllgps.bean;

/**
 * Created by hefeng on 18-6-12.
 */

public class ReceiveMsg {
    private ID _Id;
    private String imei;
    private String lat;
    private String lng;
    private String locTime;
    private String locType;
    private String timestamp;
    private String tp_code;
    private String source_type;
    private String coord_type;

    private static class ID
    {
        private String $id;

        public String get$id() {
            return $id;
        }

        public void set$id(String $id) {
            this.$id = $id;
        }

        @Override
        public String toString() {
            return "$id = " + $id;
        }
    }

    public ID get_Id() {
        return _Id;
    }

    public String getCoord_type() {
        return coord_type;
    }

    public String getImei() {
        return imei;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getLocTime() {
        return locTime;
    }

    public String getLocType() {
        return locType;
    }

    public String getSource_type() {
        return source_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTp_code() {
        return tp_code;
    }

    @Override
    public String toString() {
        return "_Id = " + _Id + ", imei = " + imei + ", lat = " + lat + ", lng = " + lng + ", locTime = " + locTime + ", locType = " + locType + ", timestamp = " + timestamp + ", tp_code = " + tp_code +", source_type = " + source_type + ", coord_type = " + coord_type;
    }
}
