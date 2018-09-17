package com.hsf1002.sky.xllgps.model;

import android.text.TextUtils;
import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.bean.ReceiveMsg;
import com.hsf1002.sky.xllgps.bean.TrackMsg;
import com.hsf1002.sky.xllgps.http.ApiService;
import com.hsf1002.sky.xllgps.http.HttpUtil;
import com.hsf1002.sky.xllgps.params.BaiduGpsParam;

import com.hsf1002.sky.xllgps.result.ResultMsg;
import com.hsf1002.sky.xllgps.util.MD5Utils;
import com.hsf1002.sky.xllgps.util.SharedPreUtils;
import com.hsf1002.sky.xllgps.util.SprdCommonUtils;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public void getGpsInfo()
    {
        RxHttpUtils.getSInstance()
                .baseUrl(RXJAVAHTTP_BASE_URL)
                .createSApi(ApiService.class)
                .getGpsInfo("862909030733751")
                .compose(Transformer.<List<ResultMsg<ReceiveMsg>>>switchSchedulers())
                .subscribe(new CommonObserver<List<ResultMsg<ReceiveMsg>>>() {
                    @Override
                    protected void onError(String s) {
                        Log.d(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(List<ResultMsg<ReceiveMsg>> resultMsgs) {
                        int size = resultMsgs.size();
                        Log.d(TAG, "onSuccess: receive msg---------------------- receiveMsgs.size() = " + size);
                        for (int i=0; i<size; ++i)
                        {
                            Log.d(TAG, "onSuccess: receive[" + i + "]" + resultMsgs.get(i));
                        }
                    }
                });
    }

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

    private static String getParamStr(String timeStamp)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        BaiduGpsParam baiduGpsParam = BaiduGpsApp.getInstance().getBaiduGpsStatus();

        params.put("locTime", baiduGpsParam.getLocTime());
        params.put("locType", baiduGpsParam.getLocType());
        //params.put("mobile", sendMsg.getMobile());
        params.put("source_type", "1");
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

    private String getUrlTail()
    {
        String tailStr = URL_LV + URL_APP_ID + URL_APP_SECRET + URL_TPCODE;// + URL_TIMESTAMP;
        return tailStr;
    }

    public void pushGpsInfo(String type, String source_type)
    {
        //String mobile = BaiduGpsApp.getInstance().getSendMsg().getMobile();
        String imei = SprdCommonUtils.getInstance().getIMEI();
        String longitude = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLng();
        String latitude = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLat();
        String locTime = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLocTime();
        String locType = BaiduGpsApp.getInstance().getBaiduGpsStatus().getLocType();
        String address = BaiduGpsApp.getInstance().getBaiduGpsStatus().getGisInfo();
        String coord_type = BaiduGpsApp.getInstance().getBaiduGpsStatus().getCoord_type();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String mvversion = "V1.0000000";//SystemProperties.get("ro.product.externversion");

        timeStamp = timeStamp.substring(0, 10);
        String params = getParamStr(timeStamp);
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
                .compose(Transformer.<ResultMsg<TrackMsg>>switchSchedulers())
                .subscribe(new CommonObserver<ResultMsg<TrackMsg>>() {
                    @Override
                    protected void onError(String s) {
                        Log.i(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(ResultMsg<TrackMsg> trackMsgResultMsg) {

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

    // http://api.cloud.site4test.com/thirdparty/index/location/receive/
    // lv=1&app_id=5cd12ff1222ef64d1a5227a181631a2195e1b456&app_secret=&0512a2c055a970f7e64381c7eee4f9da&tpcode=xiaobawangmobile&timestamp=1493857458&sign=fffxxxxx
    // coord_type=gcj02&gisInfo=%E4%B8%AD%E5%9B%BD%E6%B7%B1%E5%9C%B3%E5%B8%82%E5%8D%97%E5%B1%B1%E5%8C%BA%E7%A7%91%E6%8A%80%E5%8D%97%E5%8D%81%E4%BA%8C%E8%B7%AF&imei=864376039982516&lat=22.537686&lng=113.957182&locTime=1528782543103&locType=3&mobile=18818681242&source_type=1&timestamp=-225814259&type=1

    // POST /thirdparty/index/location/receive?lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b&tpcode=xiaobawangmobile×tamp=1528797474&sign=ee0906c15cb85385a63b349e6c27460a HTTP/1.1

    private String getEncodePara(String original)
    {
        return MD5Utils.encrypt(original);
    }

    public static RxjavaHttpModel getInstance()
    {
        return Holder.instance;
    }

    private static class Holder
    {
        private static RxjavaHttpModel instance = new RxjavaHttpModel();
    }



    public static String sendPostMessage(URL url, Map<String, String> params, String encode) {
        // 作为StringBuffer初始化的字符串
        StringBuffer buffer = new StringBuffer();
        String result = null;
        try {
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    // 完成转码操作
                    buffer.append(entry.getKey()).append("=").append(
                            URLEncoder.encode(entry.getValue(), encode))
                            .append("&");
                }
                buffer.deleteCharAt(buffer.length() - 1);
            }
            // System.out.println(buffer.toString());
            // 删除掉最有一个&

            Log.d(TAG, "sendPostMessage: buffer = " + buffer);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);// 表示从服务器获取数据
            urlConnection.setDoOutput(true);// 表示向服务器写数据
            // 获得上传信息的字节大小以及长度
            byte[] mydata = buffer.toString().getBytes();
            // 表示设置请求体的类型是文本类型
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(mydata.length));
            // 获得输出流,向服务器输出数据
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(mydata,0,mydata.length);
            outputStream.close();
            // 获得服务器响应的结果和状态码
            int responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "sendPostMessage: responseCode--------------------" + responseCode);
            if (responseCode == 200) {
                result = changeInputStream(urlConnection.getInputStream(), encode);
            }
            else
            {
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d(TAG, "sendPostMessage: result = " + result);
        
        return result;
    }

    private static String changeInputStream(InputStream inputStream,
                                            String encode) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray(), encode);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public void sendPostMsg() {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<String, String>();
        BaiduGpsParam sendMsg = BaiduGpsApp.getInstance().getBaiduGpsStatus();
        String encodedSendMsg = getEncodePara(sendMsg + URL_TOKEN);

        params.put("coord_type", sendMsg.getCoord_type());
        //params.put("gisInfo", sendMsg.getAddress());
       // params.put("imei", sendMsg.getImei());
       // params.put("lat", sendMsg.getLantitude());
       // params.put("lng", sendMsg.getLongitude());
        params.put("locTime", sendMsg.getLocTime());
        params.put("locType", sendMsg.getLocType());
        //params.put("mobile", sendMsg.getMobile());
        params.put("source_type", "1");
       // params.put("timestamp", sendMsg.getTimestamp());
        //params.put("type", sendMsg.getType());

        params.put("lv", "1");
        params.put("app_id", "bc7007b968877d0d3eec4caa77127c99a96aeb2b");
        params.put("app_secret", "9ff96155b3f42dc7a337aa59ca59578b");
        params.put("tpcode", "xiaobawangmobile");
        params.put("sign", encodedSendMsg);

        URL url = null;

        try
        {
            url = new URL("http://api.cloud.site4test.com/thirdparty/index/location/receive");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        sendPostMessage(url, params, "utf-8");
    }

    public String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            Log.d(TAG, "sendPost: url = " + url);
            Log.d(TAG, "sendPost: param = " + param);

            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
                Log.d(TAG, "sendGet: getHeaderFields key =" + key + " + map.get(key) = " + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "sendGet: httpUrlConnection.getResponseCode() = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK ||
                    responseCode == HttpURLConnection.HTTP_CREATED ||
                    responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                Log.d(TAG, "sendGet: ok--------------------");
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
                Log.d(TAG, "sendGet: error--------------------");
            }

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.d(TAG, "sendGet: e = " + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        Log.d(TAG, "sendGet: result = " + result);
        return result;
    }

    private Map tokenMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("access-token", SharedPreUtils.getInstance().getString("token", "weyue"));
        map.put("app-type", "Android");
        return map;
    }

    private String getSortedParam(String params)
    {
        StringBuilder sortedParam = new StringBuilder();
        List<String> listString = new ArrayList<>();
        int length = 0;
        int paramLen = 0;

        if (TextUtils.isEmpty(params) || params.length() == 0)
        {
            return null;
        }

        params = params.substring(1);  // delete {
        String[] strSplits = params.split(":");
        length = strSplits.length - 1;
        Log.e(TAG, "getSortedParam: before sorted paramNumber = " + length + ", params = " + params);

        for (int i=0; i<length; ++i)
        {
            String param = getOneParam(params, paramLen + i);
            paramLen += param.length();
            //Log.d(TAG, "getSortedParam: param = " + param + ", param.length() = " + param.length());
            listString.add(param);
        }

        Collections.sort(listString, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String str1=(String) o1;
                String str2=(String) o2;
                if (str1.compareToIgnoreCase(str2)<0){
                    return -1;
                }
                return 1;
            }
        });

        sortedParam.append("{");

        for (int i=0; i<length; ++i)
        {
            //Log.d(TAG, "geSortedParam: listString[" + i + "]" + listString.get(i).toString());
            sortedParam.append(listString.get(i).toString());

            if (i != length - 1)
            {
                sortedParam.append(",");
            }
        }
        sortedParam.append("}");

        Log.e(TAG, "getSortedParam: after sorted sortedParam = " + sortedParam);

        return sortedParam.toString();
    }

    private String getOneParam(String params, int position)
    {
        String param = null;
        int startPos = 0;
        int endPos = 0;

        startPos = params.indexOf("\"", position);
        endPos = params.indexOf("\"", startPos + 1);
        endPos = params.indexOf("\"", endPos + 1 );
        endPos = params.indexOf("\"", endPos + 1);

        Log.e(TAG, "getOneParam: startPos = " + startPos + ", endPos = " + endPos);

        try {
            param = params.substring(startPos, endPos + 1);
        }
        catch (StringIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }

        int keyStartPos = params.indexOf("\"", position);
        int keyEndPos = params.indexOf("\"", keyStartPos + 1);
        int valueStartPos = params.indexOf("\"", keyEndPos + 1);
        int valueEndPos = params.indexOf("\"", valueStartPos + 1);
        String key = null;
        String value = null;

        key = params.substring(keyStartPos + 1, keyEndPos);
        value = params.substring(valueStartPos + 1, valueEndPos);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(key, value);

        Log.d(TAG, "getOneParam: key = " + key + ", value = " + value);
        Log.d(TAG, "getOneParam: key = " + hashMap.keySet() + ", value = " + hashMap.get(key));
        Log.d(TAG, "getOneParam: hashMap = " + hashMap);

        return param;
    }
}
