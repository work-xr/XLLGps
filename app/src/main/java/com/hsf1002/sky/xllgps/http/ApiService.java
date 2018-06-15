package com.hsf1002.sky.xllgps.http;


import com.hsf1002.sky.xllgps.bean.ReceiveMsg;
import com.hsf1002.sky.xllgps.bean.TrackMsg;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hefeng on 18-6-8.
 */

public interface ApiService {
    // http://api.cloud.site4test.com/thirdparty/mobile/location/list?imei=864648030095476&start=2017-10-25 17:00:00&lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b
    @GET("http://api.cloud.site4test.com/thirdparty/mobile/location/list?imei=864376039982516&start=2017-10-25 17:00:00&lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b")
    Observable<List<ReceiveMsg>> getGpsInfo();

   /* {
            "mobile": "13333333333",
      		“imei”:”864648030040316”,
            "lng": "123.123",
            "lat": "123.123",
            "locTime": "2016-04-26 13:22:22",
            "locType": "1",
            "type": "1",
            "gisInfo": "南京市XXXX",
            “source_type”:1,
            “coord_type”:”GCJ02”,
            “timestamp”:1493857458
    }*/
    // http://api.cloud.site4test.com/thirdparty/index/location/receive
    //lv=1&app_id=5cd12ff1222ef64d1a5227a181631a2195e1b456&app_secret=&0512a2c055a970f7e64381c7eee4f9da&tpcode=xiaobawangmobile&timestamp=1493857458&sign=fffxxxxx
    /*Observable<String> pushGpsInfo(@Field("mobile") String mobile,
                                   @Field("imei") String imei,
                                   @Field("lng") String longitude,
                                   @Field("lat") String latitude,
                                   @Field("locTime") String locTime,
                                   @Field("locType") String locType,
                                   @Field("type") String type,
                                   @Field("gisInfo") String address,
                                   @Field("source_type") int source_type,
                                   @Field("coord_type") String coord_type,
                                   @Field("timestamp") int timeStamp);*/
    @POST("http://api.cloud.site4test.com/thirdparty/index/location/receive/")
    @FormUrlEncoded
    Observable<String> pushGpsInfo(
            @Field("coord_type") String coord_type,
            @Field("gisInfo") String address,
            @Field("imei") String imei,
            @Field("lat") String latitude,
            @Field("lng") String longitude,
            @Field("locTime") String locTime,
            @Field("locType") String locType,
            @Field("mobile") String mobile,
            @Field("source_type") int source_type,
            @Field("timestamp") int timeStamp,
            //@Field("token") String token,
            @Field("type") String type/*,
            @Path("url") String url*/);
   //@PUT("http://api.cloud.site4test.com/thirdparty/index/location/receive/{url}")
   //Observable<String> pushGpsInfo(@Path("url") String url);

    @POST("http://api.cloud.site4test.com/thirdparty/index/location/receive")
    @FormUrlEncoded
    Observable<String> pushGpsSimpleInfo(
            @Field("coord_type") String coord_type,
            //@Field("gisInfo") String address,
            @Field("imei") String imei,
            @Field("lat") String latitude,
            @Field("lng") String longitude,
            @Field("locTime") String locTime,
            @Field("locType") String locType,
            //@Field("mobile") String mobile,
            @Field("source_type") String source_type,
            @Field("timestamp") int timeStamp,
            //@Field("token") String token,
            //@Field("type") String type/*,

            @Field("lv") String lv,
            @Field("app_id") String app_id,
            @Field("app_secret") String app_secret,
            @Field("tpcode") String tpcode,
            //@Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    // http://auth.hoinnet.com:8001/checkin
    @POST("http://auth.hoinnet.com:8001/checkin")
    @FormUrlEncoded
    Observable<TrackMsg>  getTrackLoginInfo(
            @Field("debug") boolean debug,
            @Field("meid") String meid,
            @Field("type") String type,
            @Field("version") String version
    );

}
