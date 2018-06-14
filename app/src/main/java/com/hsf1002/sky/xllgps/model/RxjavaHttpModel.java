package com.hsf1002.sky.xllgps.model;

import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.bean.ReceiveMsg;
import com.hsf1002.sky.xllgps.bean.SendMsg;
import com.hsf1002.sky.xllgps.http.ApiService;
import com.hsf1002.sky.xllgps.util.MD5Utils;
import com.hsf1002.sky.xllgps.util.SharedPreUtils;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hsf1002.sky.xllgps.util.Const.URL_APP_ID;
import static com.hsf1002.sky.xllgps.util.Const.URL_APP_ID_VALUE;
import static com.hsf1002.sky.xllgps.util.Const.URL_APP_SECRET;
import static com.hsf1002.sky.xllgps.util.Const.URL_APP_SECRET_VALUE;
import static com.hsf1002.sky.xllgps.util.Const.URL_ENCODE_TYPE;
import static com.hsf1002.sky.xllgps.util.Const.URL_LV;
import static com.hsf1002.sky.xllgps.util.Const.URL_LV_VALUE;
import static com.hsf1002.sky.xllgps.util.Const.URL_SIGN_VALUE;
import static com.hsf1002.sky.xllgps.util.Const.URL_TOKEN;
import static com.hsf1002.sky.xllgps.util.Const.URL_TPCODE;
import static com.hsf1002.sky.xllgps.util.Const.URL_TPCODE_VALUE;

/**
 * Created by hefeng on 18-6-8.
 */

public class RxjavaHttpModel implements BaseModel {
    private static final String TAG = "RxjavaHttpModel";

    @Override
    public void getGpsInfo()
    {
        RxHttpUtils.getSInstance()
                .createSApi(ApiService.class)
                .getGpsInfo()
                .compose(Transformer.<List<ReceiveMsg>>switchSchedulers())
                .subscribe(new CommonObserver<List<ReceiveMsg>>() {
                    @Override
                    protected void onError(String s) {
                        Log.d(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(List<ReceiveMsg> receiveMsgs) {
                        int size = receiveMsgs.size();
                        Log.d(TAG, "onSuccess: receive msg---------------------- receiveMsgs.size() = " + size);
                        for (int i=0; i<size; ++i)
                        {
                            Log.d(TAG, "onSuccess: receive[" + i + "]" + receiveMsgs.get(i));
                        }
                    }
                });
    }

    @Override
    public void pushGpsInfo()
    {
        String mobile = BaiduGpsApp.getInstance().getSendMsg().getMobile();
        String imei = BaiduGpsApp.getInstance().getSendMsg().getImei();
        String longitude = BaiduGpsApp.getInstance().getSendMsg().getLongitude();
        String latitude = BaiduGpsApp.getInstance().getSendMsg().getLantitude();
        String locTime = BaiduGpsApp.getInstance().getSendMsg().getLocTime();
        String locType = BaiduGpsApp.getInstance().getSendMsg().getLocType();
        String type = BaiduGpsApp.getInstance().getSendMsg().getType();
        String address = BaiduGpsApp.getInstance().getSendMsg().getAddress();
        String source_type = BaiduGpsApp.getInstance().getSendMsg().getSource_type();
        String coord_type = BaiduGpsApp.getInstance().getSendMsg().getCoord_type();
        int timeStamp = (int)System.currentTimeMillis();

        final String baseUrl = "http://api.cloud.site4test.com/thirdparty/index/location/receive";
        final String baseUrl1 = "http://api.cloud.site4test.com/thirdparty/index/location/receive?";
        String param = null;
        try
        {
            param = BaiduGpsApp.getInstance().getSendMsg().toEncodeString(URL_ENCODE_TYPE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        final String tailStr = getUrlTail();
        final String params = param;

        Log.d(TAG, "pushGpsInfo: tailStr = " + tailStr);
        Log.d(TAG, "pushGpsInfo: param = " + params);

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendPost(baseUrl, tailStr + params);
                //sendPost();
                //sendGet(baseUrl, tailStr + params);
                //getData("lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b&tpcode=xiaobawangmobile&timestamp=1528797474&sign=ee0906c15cb85385a63b349e6c27460a");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendPostMsg();
            }
        }).start();

        RxHttpUtils.getSInstance()
                .createSApi(ApiService.class)
                .pushGpsSimpleInfo(
                        coord_type,
                        imei,
                        latitude,
                        longitude,
                        locTime,
                        locType,
                        source_type,
                        timeStamp,
                        URL_LV_VALUE,
                        URL_APP_ID_VALUE,
                        URL_APP_SECRET_VALUE,
                        URL_TPCODE_VALUE,
                        URL_SIGN_VALUE
                       // "1528797474",
                        //"df7e14dbcc48be508d3e3a7da036c1b2"
                )
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    protected void onError(String s) {
                        // \u63a5\u53e3\u8bf7\u6c42\u8d85\u65f6  接口请求超时
                        Log.d(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(String s) {
                        Log.d(TAG, "onSuccess: s = " + s);
                    }
                });
/*
        RxHttpUtils.getSInstance()
                //.getSingleRetrofitBuilder()
                .createSApi(ApiService.class)
                .pushGpsInfo(
                        coord_type,
                        address,
                        imei,
                        latitude,
                        longitude,
                        locTime,
                        locType,
                        mobile,
                        source_type,
                        timeStamp,
                        type,
                        token,
                        getUrlTail())
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    protected void onError(String s) {
                        Log.d(TAG, "onError: s = " + s);
                    }

                    @Override
                    protected void onSuccess(String s) {
                        Log.d(TAG, "onSuccess: s = " + s);
                    }
                });*/
    }

    // http://api.cloud.site4test.com/thirdparty/index/location/receive/
    // lv=1&app_id=5cd12ff1222ef64d1a5227a181631a2195e1b456&app_secret=&0512a2c055a970f7e64381c7eee4f9da&tpcode=xiaobawangmobile&timestamp=1493857458&sign=fffxxxxx
    // coord_type=gcj02&gisInfo=%E4%B8%AD%E5%9B%BD%E6%B7%B1%E5%9C%B3%E5%B8%82%E5%8D%97%E5%B1%B1%E5%8C%BA%E7%A7%91%E6%8A%80%E5%8D%97%E5%8D%81%E4%BA%8C%E8%B7%AF&imei=864376039982516&lat=22.537686&lng=113.957182&locTime=1528782543103&locType=3&mobile=18818681242&source_type=1&timestamp=-225814259&type=1

    // POST /thirdparty/index/location/receive?lv=1&app_id=bc7007b968877d0d3eec4caa77127c99a96aeb2b&app_secret=9ff96155b3f42dc7a337aa59ca59578b&tpcode=xiaobawangmobile×tamp=1528797474&sign=ee0906c15cb85385a63b349e6c27460a HTTP/1.1
    private String getUrlTail()
    {
        String completStr = URL_LV + URL_APP_ID + URL_APP_SECRET + URL_TPCODE;// + URL_TIMESTAMP;
        String sendMsg = BaiduGpsApp.getInstance().getSendMsg().toString();
        String encodedSendMsg = getEncodePara(sendMsg + URL_TOKEN);

        completStr += "&sign=" + encodedSendMsg;
        Log.d(TAG, "getOriginalStr: sendMsg = " + sendMsg);
        Log.d(TAG, "getOriginalStr: encodedSendMsg = " + encodedSendMsg);
        Log.d(TAG, "getOriginalStr: completStr = " + completStr);
        return completStr;
    }

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

    public String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

            Log.d(TAG, "sendPost: url = " + url);
            Log.d(TAG, "sendPost: param = " + param);

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            //Log.d(TAG, "sendPost: conn = " + conn.getURL().toString());
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "sendPost: httpUrlConnection.getResponseCode() = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK ||
                    responseCode == HttpURLConnection.HTTP_CREATED ||
                    responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                Log.d(TAG, "sendPost: ok--------------------");
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream(), "UTF-8"));
                Log.d(TAG, "sendPost: error--------------------");
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.d(TAG, "sendPost: e = " + e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        // sendPost: result = {"status":0,"description":"\u7f3a\u5c11\u5fc5\u987b\u53c2\u6570[timestamp]"}  缺少必须参数
        Log.d(TAG, "sendPost: result = " + result);

        return result;
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
        SendMsg sendMsg = BaiduGpsApp.getInstance().getSendMsg();
        String encodedSendMsg = getEncodePara(sendMsg + URL_TOKEN);

        params.put("coord_type", sendMsg.getCoord_type());
        //params.put("gisInfo", sendMsg.getAddress());
        params.put("imei", sendMsg.getImei());
        params.put("lat", sendMsg.getLantitude());
        params.put("lng", sendMsg.getLongitude());
        params.put("locTime", sendMsg.getLocTime());
        params.put("locType", sendMsg.getLocType());
        //params.put("mobile", sendMsg.getMobile());
        params.put("source_type", sendMsg.getSource_type());
        params.put("timestamp", sendMsg.getTimestamp());
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
}
