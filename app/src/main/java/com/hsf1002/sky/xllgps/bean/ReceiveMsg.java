package com.hsf1002.sky.xllgps.bean;

/**
 * Created by hefeng on 18-6-12.
 */

public class ReceiveMsg {
    /*
    * {"_id":{"$id":"5b4050ffbf4d8298308b46da"},
    * "source_type":"1",
    * "coord_type":"BD09",
    * "imei":"862909030733751",
    * "locType":"1",
    * "timestamp":"1530941694",
    * "locTime":"2018-07-07 13:34:54",
    * "lat":"26.099835",
    * "lng":"119.313902",
    * "tp_code":"xiaobawangmobile",
    * "mdversion":"P18A_WD_37_SUBOR_S7_20180705_V1.0.2"}
    * */
    private ID _id;
    private String imei;
    private String lat;
    private String lng;
    private String locTime;
    private String locType;
    private String timestamp;
    private String tp_code;
    private String source_type;
    private String coord_type;
    private String mdversion;

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

    @Override
    public String toString() {
        return "_Id = " + _id + ", imei = " + imei + ", lat = " + lat + ", lng = " + lng + ", locTime = " + locTime + ", locType = " + locType + ", timestamp = " + timestamp + ", tp_code = " + tp_code +", source_type = " + source_type + ", coord_type = " + coord_type;
    }
}
