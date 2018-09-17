package com.hsf1002.sky.xllgps.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hsf1002.sky.xllgps.app.GpsApplication;
import com.hsf1002.sky.xllgps.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.location.Criteria.ACCURACY_FINE;
import static android.location.Criteria.POWER_LOW;

/**
 * Created by hefeng on 18-7-26.
 */

public class NetworkGpsApp {
    private static final String TAG = "NetworkGpsApp";
    private String provider;
    public static final int SHOW_LOCATION=0;//更新文字式的位置信息
    public static final int SHOW_LATLNG=1; //更新经纬坐标式的位置信息

    private static class Holder
    {
        private static NetworkGpsApp sInstance = new NetworkGpsApp();
    }

    public static NetworkGpsApp getInstance()
    {
        return Holder.sInstance;
    }

    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message message){
            switch(message.what){
                case SHOW_LOCATION:
                    Log.d(TAG, "showing the positio>>>>>");
                    String currentPosition=(String)message.obj;

                    Log.d(TAG, "Has show the position...>>>>...." + currentPosition);
                    break;
                case SHOW_LATLNG:
                    String latlng=(String)message.obj;
                    Log.d(TAG, "Has show the latlng...>>>>...." + latlng);
                default:
                    break;
            }
        }
    };

    public void getLocationNetworkGps() throws SecurityException
    {
        LocationManager locationManager = (LocationManager) GpsApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        //LocationProvider gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);           //1.通过GPS定位，较精确。也比較耗电
        //LocationProvider netProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);       //2.通过网络定位。对定位精度度不高或省点情况可考虑使用

        List<String>providerList = locationManager.getProviders(true);
        Log.d(TAG, "getLocationNetworkGps: providerList = " + providerList);

        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }
        else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        }
        else if(providerList.contains(LocationManager.PASSIVE_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }
        else{
            return;
        }

        Criteria crite = new Criteria();
        crite.setAccuracy(ACCURACY_FINE);
        crite.setPowerRequirement(POWER_LOW);
        provider = locationManager.getBestProvider(crite, true);

        Log.d(TAG, "getLocationNetworkGps: getBestProvider = " + provider);

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null)
        {
            showLocation(location);
        }
        else
        {
            Log.d(TAG, "getLocationNetworkGps: location = null");
        }

        /*
        * 进行定位  * provider:用于定位的locationProvider字符串:LocationManager.NETWORK_PROVIDER/LocationManager.GPS_PROVIDER
        * minTime:时间更新间隔。单位：ms     * minDistance:位置刷新距离，单位：m
        * listener:用于定位更新的监听者locationListener
        */
        locationManager.requestLocationUpdates(provider, 1000, 1, listener);

        /*{
            //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
            //Toast.makeText(this, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            GpsApplication.getAppContext().startActivity(i);

            Log.d(TAG, "init: startActivity");
        }*/
    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Log.d(TAG, "onLocationChanged: latitude = " + latitude + ", longitude = " + longitude);
            //geocoder(latitude, longitude);
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(final Location location){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    String request = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
                    request += location.getLatitude()+","+location.getLongitude()+"&sensor=false";
                    String response = HttpUtil.getHttpRequest(GpsApplication.getAppContext(),request);
                    parseJSONResponse(response);

                }
                catch(Exception e){
                    Log.d(TAG, "showLocation: the inptuStream is wrong!");
                    e.printStackTrace();
                }
            }

        }).start();
        //显示经纬度坐标
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String position="";
                position="Latitude="+location.getLatitude()+"\n"
                        +"Longitude="+location.getLongitude();
                Message msg=new Message();
                msg.what=SHOW_LATLNG;
                msg.obj=position;
                handler.sendMessage(msg);
            }

        }).start();
    }

    //解析JSON数据
    private void parseJSONResponse(String response){
        try{
            Log.d(TAG, "parseJSONResponse: getting the jsonObject...");
            JSONObject jsonObject=new JSONObject(response);
            //获取results节点下的位置
            Log.d(TAG, "parseJSONResponse: Getting the jsongArray...");
            JSONArray resultArray=jsonObject.getJSONArray("results");
            Log.d(TAG, "parseJSONResponse: Got the JSONArray...");
            if(resultArray.length()>0){
                JSONObject subObject=resultArray.getJSONObject(0);
                //取出格式化后的位置信息
                String address=subObject.getString("formatted_address");
                Message message=new Message();
                message.what = SHOW_LOCATION;
                message.obj="您的位置:"+address;
                Log.d(TAG, "showLocation:Sending the inputStream...");
                handler.sendMessage(message);
            }
        }
        catch(Exception e){
            Log.d(TAG, "parseJSONResponse: something wrong");
            e.printStackTrace();
        }


    }

    private static void geocoder(double latitude, double longitude)
    {
        Geocoder gc = new Geocoder(GpsApplication.getAppContext(), Locale.getDefault());
        List<Address> locationList = null;

        try {
            locationList = gc.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = locationList.get(0);
        String countryName = address.getCountryName();
        String locality = address.getLocality();
        Log.i(TAG, "countryName = " + countryName + "locality = " + locality);

        for (int i = 0; address.getAddressLine(i) != null; i++) {
            String addressLine = address.getAddressLine(i);//得到周边信息。包含街道等。i=0，得到街道名称
            Log.i(TAG, "addressLine = " + addressLine);
        }
    }
}
