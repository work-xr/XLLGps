package com.hsf1002.sky.xllgps.location;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.hsf1002.sky.xllgps.app.XLLGpsApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by hefeng on 18-7-26.
 */

public class NetworkApp {
    public static final String TAG = "NetworkApp";

    private static final class Holder
    {
        private static final NetworkApp sInstance = new NetworkApp();
    }

    public static NetworkApp getInstance()
    {
        return Holder.sInstance;
    }

    /* 基站信息结构体 */
    public class SCell{
        public int MCC;
        public int MNC;
        public int LAC;
        public int CID;
    }

    /* 经纬度信息结构体 */
    public class SItude{
        public String latitude;
        public String longitude;
    }

    /* 获取具体位置信息 */
    public void getLocationNetwork() {

        try {
            /** 获取基站数据 */
            SCell cell = getCellInfo();

            /** 根据基站数据获取经纬度 */
            SItude itude = getItude(cell);

            /** 获取地理位置 */
            String location = getLocation(itude);

            Log.d(TAG, "getLocationNetwork: cell = " + cell + ", location = " + location);

        } catch (Exception e) {

            Log.d(TAG, "getLocationNetwork: error.msg = " + e.getMessage());
        }
    }

    /**
     * 获取基站信息
     *
     * @throws Exception
     */
    private SCell getCellInfo() throws Exception {
        SCell cell = new SCell();

        TelephonyManager mTelNet = (TelephonyManager) XLLGpsApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
        if (location == null) {
            throw new Exception("获取基站信息失败");
        }
        String operator = mTelNet.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int cid = location.getCid();
        int lac = location.getLac();

        cell.MCC = mcc;
        cell.MNC = mnc;
        cell.LAC = lac;
        cell.CID = cid;

        return cell;
    }

    /**
     * 获取经纬度
     *
     * @throws Exception
     */
    private SItude getItude(SCell cell) throws Exception {
        SItude itude = new SItude();

        /** 采用Android默认的HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用POST方法 */
        HttpPost post = new HttpPost("http://www.google.com/loc/json");
        try {
            /** 构造POST的JSON数据 */
            JSONObject holder = new JSONObject();
            holder.put("version", "1.1.0");
            holder.put("host", "maps.google.com");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);
            holder.put("radio_type", "gsm");
            holder.put("carrier", "HTC");

            JSONObject tower = new JSONObject();
            tower.put("mobile_country_code", cell.MCC);
            tower.put("mobile_network_code", cell.MNC);
            tower.put("cell_id", cell.CID);
            tower.put("location_area_code", cell.LAC);

            JSONArray towerarray = new JSONArray();
            towerarray.put(tower);
            holder.put("cell_towers", towerarray);

            StringEntity query = new StringEntity(holder.toString());
            post.setEntity(query);

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }

            JSONObject json = new JSONObject(strBuff.toString());
            JSONObject subjosn = new JSONObject(json.getString("location"));

            itude.latitude = subjosn.getString("latitude");
            itude.longitude = subjosn.getString("longitude");

            Log.d(TAG, "getItude: itude.latitude = " + itude.latitude + ", itude.longitude = " + itude.longitude);

        } catch (Exception e) {
            Log.e(e.getMessage(), e.toString());
            throw new Exception("获取经纬度出现错误:" + e.getMessage());
        } finally{
            post.abort();
            client = null;
        }

        return itude;
    }

    /**
     * 获取地理位置
     *
     * @throws Exception
     */
    private String getLocation(SItude itude) throws Exception {
        String resultString = "";

        /** 这里采用get方法，直接将参数加到URL上 */
        String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.latitude, itude.longitude);
        Log.i("URL", urlString);

        /** 新建HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用GET方法 */
        HttpGet get = new HttpGet(urlString);
        try {
            /** 发起GET请求并获得返回数据 */
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }
            resultString = strBuff.toString();

            /** 解析JSON数据，获得物理地址 */
            if (resultString != null && resultString.length() > 0) {
                JSONObject jsonobject = new JSONObject(resultString);
                JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                resultString = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    resultString = jsonArray.getJSONObject(i).getString("address");
                }
            }
        } catch (Exception e) {
            throw new Exception("获取物理位置出现错误:" + e.getMessage());
        } finally {
            get.abort();
            client = null;
        }

        return resultString;
    }
}
