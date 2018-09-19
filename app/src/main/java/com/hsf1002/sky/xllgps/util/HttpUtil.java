package com.hsf1002.sky.xllgps.util;

import android.text.TextUtils;
import android.util.Log;

import com.hsf1002.sky.xllgps.baidu.BaiduGpsApp;
import com.hsf1002.sky.xllgps.params.BaiduGpsParam;

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

import static com.hsf1002.sky.xllgps.util.Constant.URL_TOKEN;

/**
 * Created by hefeng on 18-7-27.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:27
    *  desc:    Get方式请求网络 1, 验证ok
    *  param:
    *  return:
    */
    public static String getHttpRequest(String address){
        HttpURLConnection connection=null;
        try{
            URL url=new URL(address);
            Log.d(TAG, "getHttpRequest: request="+address);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            Log.d(TAG, "getHttpRequest: getting the inputStream...");
            InputStream in=connection.getInputStream();
            Log.d(TAG, "getHttpRequest: got the inputStream...");
            //下面对获取到的流进行读取
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder response=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                response.append(line);
            }
            Log.d(TAG, "getHttpRequest: Got the response... = " + response.toString());
            return response.toString();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, "getHttpRequest: Some thing wrong....");
            return e.getMessage();
        }
        finally{
            if(connection!=null){
                connection.disconnect();
            }
        }
    }

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:28
    *  desc:    Post方式请求网络 1, 验证ok
    *  param:
    *  return:
    */
    public static String postHttpRequest(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            Log.d(TAG, "postHttpRequest: url = " + url);

            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            //Log.d(TAG, "sendPost: conn = " + conn.getURL().toString());
            //out = new PrintWriter(conn.getOutputStream());
            //out.print(param);
            //out.flush();
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "postHttpRequest: httpUrlConnection.getResponseCode() = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK ||
                    responseCode == HttpURLConnection.HTTP_CREATED ||
                    responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                Log.d(TAG, "postHttpRequest: ok--------------------");
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream(), "UTF-8"));
                Log.d(TAG, "postHttpRequest: error--------------------");
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.d(TAG, "postHttpRequest: e = " + e);
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
        Log.d(TAG, "postHttpRequest: result = " + result);

        return result;
    }


    /**
     *  author:  hefeng
     *  created: 18-9-18 下午4:28
     *  desc:    Get方式请求网络 2, 验证ok
     *  param:
     *  return:
     */
    public static String sendGetMsg(String url, String param) {
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
            //Map<String, List<String>> map = connection.getHeaderFields();

            Log.d(TAG, "sendGetMsg: url = " + url);
            Log.d(TAG, "sendGetMsg: param = " + param);

            // 遍历所有的响应头字段
            //for (String key : map.keySet()) {
                //System.out.println(key + "--->" + map.get(key));
                //Log.d(TAG, "sendGetMsg: getHeaderFields key =" + key + " + map.get(key) = " + map.get(key));
            //}
            // 定义 BufferedReader输入流来读取URL的响应
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "sendGetMsg: httpUrlConnection.getResponseCode() = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK ||
                    responseCode == HttpURLConnection.HTTP_CREATED ||
                    responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                Log.d(TAG, "sendGetMsg: ok--------------------");
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
                Log.d(TAG, "sendGetMsg: error--------------------");
            }

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.d(TAG, "sendGetMsg: e = " + e);
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
        Log.d(TAG, "sendGetMsg: result = " + result);
        return result;
    }

    private String getEncodePara(String original)
    {
        return MD5Utils.encrypt(original);
    }

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:28
    *  desc:    Post方式请求网络 2
    *  param:
    *  return:
    */
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
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:41
    *  desc:    对参数进行排序
    *  param:
    *  return:
    */
    @Deprecated
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

    /**
    *  author:  hefeng
    *  created: 18-9-18 下午4:40
    *  desc:    按照某种方式取 一个 key-value 的参数
    *  param:
    *  return:
    */
    @Deprecated
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
