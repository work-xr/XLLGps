package com.hsf1002.sky.xllgps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_TYPE_DWSMS;
import static com.hsf1002.sky.xllgps.util.Constant.SMS_FROM_SERVER_CONTENT_PART1;
import static com.hsf1002.sky.xllgps.util.Constant.SMS_FROM_SERVER_CONTENT_PART2;
import static com.hsf1002.sky.xllgps.util.Constant.URL_ENCODE_TYPE;

/**
 * Created by hefeng on 18-7-25.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            Log.d(TAG, "onReceive: SMS_RECEIVED_ACTION .....................................");

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
                        RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_TYPE_DWSMS);
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
    *  desc:    将平台下发的短信删除
    *  param:
    *  return:
    */
    private void deleteServerSms()
    {

    }
}
