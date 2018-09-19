package com.hsf1002.sky.xllgps.model;

import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.bean.ReceiveMsg;
import com.hsf1002.sky.xllgps.http.ApiService;
import com.hsf1002.sky.xllgps.params.BaiduGpsParam;
import com.hsf1002.sky.xllgps.result.ResultGpsMsg;
import com.hsf1002.sky.xllgps.util.MD5Utils;
import com.hsf1002.sky.xllgps.util.SprdCommonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hsf1002.sky.xllgps.util.Constant.RXJAVAHTTP_BASE_URL;
import static com.hsf1002.sky.xllgps.util.Constant.URL_APP_ID;
import static com.hsf1002.sky.xllgps.util.Constant.URL_APP_SECRET;
import static com.hsf1002.sky.xllgps.util.Constant.URL_ENCODE_TYPE;
import static com.hsf1002.sky.xllgps.util.Constant.URL_LV;
import static com.hsf1002.sky.xllgps.util.Constant.URL_TOKEN;
import static com.hsf1002.sky.xllgps.util.Constant.URL_TPCODE;

/**
 * Created by hefeng on 18-6-8.
 */

public class RxjavaHttpModel{
    private static final String TAG = "RxjavaHttpModel";

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午3:57
    *  desc:    根据 imei 查询所有上报的定位信息
    *  param:
    *  return:
    */
    public void getGpsInfo()
    {
        RxHttpUtils.getSInstance()
                .baseUrl(RXJAVAHTTP_BASE_URL)
                .createSApi(ApiService.class)
                .getGpsInfo("862909030733751")
                .compose(Transformer.<List<ReceiveMsg>>switchSchedulers())
                .subscribe(new CommonObserver<List<ReceiveMsg>>() {
                    @Override
                    protected void onError(String s) {
                        Log.d(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(List<ReceiveMsg> resultMsgs) {
                        int size = resultMsgs.size();
                        Log.d(TAG, "onSuccess: receive msg---------------------- receiveMsgs.size() = " + size);
                        for (int i=0; i<size; ++i)
                        {
                            Log.d(TAG, "onSuccess: receive[" + i + "] = " + resultMsgs.get(i));
                        }
                    }
                });


        /*new Thread(new Runnable() {

            @Override
            public void run() {
                //HttpUtil.getHttpRequest(RXJAVAHTTP_SERVER_TEST_URL);
                //HttpUtil.sendGetMsg(RXJAVAHTTP_SERVER_TEST_URL_PART1, RXJAVAHTTP_SERVER_TEST_URL_PART2);
            }
        }).start();*/
    }

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:25
    *  desc:    把定位信息以POST的方式上传到后台服务器
    *  param:
    *  return:
    */
    public void pushGpsInfo(String source_type)
    {
        //String mobile = BaiduGpsApp.getInstance().getSendMsg().getMobile();
        String imei = SprdCommonUtils.getInstance().getIMEI();
       /* String longitude = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLng();
        String latitude = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLat();
        String locTime = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLocTime();
        String locType = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLocType();
        String address = BaiduGpsApp.getInstance().getBaiduGpsStatus().getGisInfo();
        String coord_type = BaiduGpsApp.getInstance().getBaiduGpsStatus().getCoord_type();*/
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String mvversion = "V1.0000000";//SystemProperties.get("ro.product.externversion");

        timeStamp = timeStamp.substring(0, 10);
        String params = getParamStr(timeStamp, source_type);
        String data = params;
        String sign;

        //ReportParam reportParamBean = new ReportParam(coord_type, address, imei, latitude, longitude, locTime, locType, type, source_type, timeStamp);
        //String gsonString = ReportParam.getReportParamGson(reportParamBean);

        Log.e(TAG, "pushGpsInfo: imei = " + imei + ", params = " + params);
        //String sortedGsonString = getSortedParam(gsonString);

        try
        {
            data = URLEncoder.encode(params, URL_ENCODE_TYPE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        //sign = getSignedString();//MD5Utils.encrypt(gsonString/*data + RXJAVAHTTP_SECRET_CODE*/);
        sign = MD5Utils.encrypt(data);
        //Log.d(TAG, "pushGpsInfo: encodedData = " + data);

        String lastPart1 = getUrlTail();
        String lastPart2 = "&timestamp=" + timeStamp + "&mdversion=" + mvversion + "&sign=" + sign;

        Log.d(TAG, "pushGpsInfo: lastPart1 = " + lastPart1);
        Log.d(TAG, "pushGpsInfo: lastPart2 = " + lastPart2);
        String completeStr = params + lastPart1 + lastPart2;

        Log.e(TAG, "pushGpsInfo: completeParam = " + completeStr);
/*
        try
        {
            data = URLEncoder.encode(completStr, URL_ENCODE_TYPE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        Log.e(TAG, "pushGpsInfo: data = " + data);
*/
        String baseUrl = "http://api.cloud.site4test.com/thirdparty/index/location/receive";
        final String completeUrl = baseUrl + "?" + completeStr;
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.postHttpRequest(completeUrl);
            }
        }).start();
*/
        //sendPost(baseUrl, data);

        RxHttpUtils.getSInstance()
                .createSApi(ApiService.class)
                .postUrl(completeUrl)
                .compose(Transformer.<ResultGpsMsg>switchSchedulers())
                .subscribe(new CommonObserver<ResultGpsMsg>() {
                    @Override
                    protected void onError(String s) {
                        Log.i(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(ResultGpsMsg stringResultMsg) {
                        Log.i(TAG, "onSuccess: stringResultMsgGson = " + ResultGpsMsg.getResultGpsMsgGson(stringResultMsg));
                    }
                });

/*
        RxHttpUtils.getSInstance()
                .baseUrl(RXJAVAHTTP_BASE_URL)
                .createSApi(ApiService.class)
                .pushGpsSimpleInfo(
                        coord_type,
                        data,
                        URL_LV_VALUE,
                        URL_APP_ID_VALUE,
                        URL_APP_SECRET_VALUE,
                        URL_TPCODE_VALUE,
                        timeStamp,
                        version,
                        sign
                )
                .compose(Transformer.<ResultMsg>switchSchedulers())
                .subscribe(new CommonObserver<ResultMsg>() {
                    @Override
                    protected void onError(String s) {
                        Log.d(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(ResultMsg resultMsg) {
                        Log.d(TAG, "onSuccess: resultMsg = " + resultMsg);
                    }
                });
*/
    }


    /**
     *  author:  hefeng
     *  created: 18-9-18 下午4:24
     *  desc:    把各个参数拼接起来为一个字符串
     *  param:
     *  return:
     */
    private static String assembleParams(Map<String, Object> map)
    {
        if (map == null) {
            return null;
        }

        List<String> keyList = new ArrayList<>(map.keySet());
        Log.d(TAG, "getSign: before sort keyList = " + keyList);
        Collections.sort(keyList);
        Log.d(TAG, "getSign: after sort keyList = " + keyList);
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            Object value = map.get(key);
            sb.append(key + "=" + value);
            if (i != keyList.size() - 1) {
                sb.append("&");
            }
        }

        return sb.toString();
    }

    /**
     *  author:  hefeng
     *  created: 18-9-18 下午4:23
     *  desc:    把参数以map的方式保存
     *  param:
     *  return:
     */
    private static String getParamStr(String timeStamp, String sourceType)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        BaiduGpsParam baiduGpsParam = BaiduGpsApp.getInstance().getBaiduGpsStatus();

        params.put("locTime", baiduGpsParam.getLocTime());
        params.put("locType", baiduGpsParam.getLocType());
        //params.put("mobile", sendMsg.getMobile());
        params.put("source_type", sourceType);
        params.put("timestamp", timeStamp);
        params.put("token", URL_TOKEN);
        params.put("type", "1");

        params.put("coord_type", baiduGpsParam.getCoord_type());
        //params.put("gisInfo", sendMsg.getAddress());
        params.put("gisInfo", baiduGpsParam.getGisInfo());
        params.put("imei", SprdCommonUtils.getInstance().getIMEI());
        params.put("lat", baiduGpsParam.getLat());
        params.put("lng", baiduGpsParam.getLng());

        return assembleParams(params);
    }

    /**
     *  author:  hefeng
     *  created: 18-9-18 下午4:24
     *  desc:    取需要在后面追加的6个参数
     *  param:
     *  return:
     */
    private String getUrlTail()
    {
        String tailStr = URL_LV + URL_APP_ID + URL_APP_SECRET + URL_TPCODE;// + URL_TIMESTAMP;
        return tailStr;
    }

    // http://api.cloud.site4test.com/thirdparty/index/location/receive/
    // lv=1&app_id=5cd12ff1222ef64d1a5227a181631a2195e1b456&app_secret=&0512a2c055a970f7e64381c7eee4f9da&tpcode=xiaobawangmobile&timestamp=1493857458&sign=fffxxxxx
    // coord_type=gcj02&gisInfo=%E4%B8%AD%E5%9B%BD%E6%B7%B1%E5%9C%B3%E5%B8%82%E5%8D%97%E5%B1%B1%E5%8C%BA%E7%A7%91%E6%8A%80%E5%8D%97%E5%8D%81%E4%BA%8C%E8%B7%AF&imei=864376039982516&lat=22.537686&lng=113.957182&locTime=1528782543103&locType=3&mobile=18818681242&source_type=1&timestamp=-225814259&type=1

    // POST /thirdparty/index/location/receive?lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b&tpcode=xiaobawangmobile×tamp=1528797474&sign=ee0906c15cb85385a63b349e6c27460a HTTP/1.1


    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:25
    *  desc:    创建单例
    *  param:
    *  return:
    */
    public static RxjavaHttpModel getInstance()
    {
        return Holder.instance;
    }

    private static class Holder
    {
        private static RxjavaHttpModel instance = new RxjavaHttpModel();
    }
}
