package com.hsf1002.sky.xllgps.http;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hefeng on 18-7-27.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static String getHttpRequest(Context context, String address){
        HttpURLConnection connection=null;
        try{
            URL url=new URL(address);
            Log.d(TAG, "HttpUtil: request="+address);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            Log.d(TAG, "HttpUtil: getting the inputStream...");
            InputStream in=connection.getInputStream();
            Log.d(TAG, "HttpUtil: got the inputStream...");
            //下面对获取到的流进行读取
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder response=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                response.append(line);
            }
            Log.d(TAG, "HttpUtil: Got the response...");
            return response.toString();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, "HttpUtil: Some thing wrong....");
            return e.getMessage();
        }
        finally{
            if(connection!=null){
                connection.disconnect();
            }
        }
    }

    public static String postHttpRequest(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            Log.d(TAG, "sendPost: url = " + url);

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



}
