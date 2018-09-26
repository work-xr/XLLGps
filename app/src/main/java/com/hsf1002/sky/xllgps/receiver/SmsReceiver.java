package com.hsf1002.sky.xllgps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;
import com.hsf1002.sky.xllgps.util.NetworkUtils;

import static com.hsf1002.sky.xllgps.util.Constant.ACTION_SMS_FROM_SERVER;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_SOURCE_TYPE_SOS;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_TYPE_DWSMS;
import static com.hsf1002.sky.xllgps.util.Constant.SMS_FROM_SERVER_CONTENT_PART1;
import static com.hsf1002.sky.xllgps.util.Constant.SMS_FROM_SERVER_CONTENT_PART2;

/**
 * Created by hefeng on 18-7-25.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d(TAG, "onReceive: ...........................................action = " + action);

        // 判断是否从孝老平台后台服务器发送的短信, 如果是, 将其截断后发送此广播
        if (action.equals(ACTION_SMS_FROM_SERVER))
        {
            Log.d(TAG, "onReceive: msg from server, just report position");
            //RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_TYPE_DWSMS);
            NetworkUtils.sendBroadCastNetworkActivated();
            RxjavaHttpModel.getInstance().setGpsType(LOCATION_TYPE_DWSMS);
        }

        // 不要在此处接收再删除, 客户已经有感知
        if (action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;

            if (null != bundle)
            {
                Object[] smsObj = (Object[]) bundle.get("pdus");

                for (Object object : smsObj) {
                    msg = SmsMessage.createFromPdu((byte[]) object);
                    String address = msg.getOriginatingAddress();
                    String body = msg.getMessageBody();
                    String center = msg.getServiceCenterAddress();

                    Log.d(TAG, "onReceive: address = " +  address);
                    Log.d(TAG, "onReceive: body = " + body);
                    Log.d(TAG, "onReceive: center = " + center);

                    if (isFromServerSms(body))
                    {
                        Log.d(TAG, "onReceive: get the msg success................................");
                        //RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_TYPE_DWSMS);
                        break;
                    }
                }
            }
            return;
        }
    }

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午4:31
    *  desc:    判断是否来自服务器的短信
    *  param:   [福建青鸟三盛]老人机即时定位API
    *  return:
    */
    private boolean isFromServerSms(final String content) {
        boolean result = false;
        String presetPart1 = SMS_FROM_SERVER_CONTENT_PART1;
        String presetPart2 = SMS_FROM_SERVER_CONTENT_PART2;

        Log.i(TAG, "isFromServerSms: presetPart1 = " + presetPart1);
        Log.i(TAG, "isFromServerSms: presetPart2 = " + presetPart2);

        if (content.contains(presetPart1) && content.contains(presetPart2))
        {
            result = true;
            deleteServerSms();
        }

        return result;
    }

    /**
    *  author:  hefeng
    *  created: 18-9-17 下午5:11
    *  desc:    将平台下发的短信删除, 可能拿不到权限, 需要发送到系统应用去删除
    *  param:
    *  return:
    */
    private void deleteServerSms()
    {
        // sendBroadcast
    }
}
