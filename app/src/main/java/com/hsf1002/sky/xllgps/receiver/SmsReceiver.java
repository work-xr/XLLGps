package com.hsf1002.sky.xllgps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;

import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_SOURCE_TYPE_ORDINARY;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_SOURCE_TYPE_SOS;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_TYPE_DWSMS;
import static com.hsf1002.sky.xllgps.util.Constant.LOCATION_TYPE_PLATFORM;
import static com.hsf1002.sky.xllgps.util.Constant.SOS_RECEIVED_ACTION;

/**
 * Created by hefeng on 18-7-25.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(SOS_RECEIVED_ACTION))
        {
            Log.d(TAG, "onReceive: SOS_RECEIVED_ACTION .....................................");
            RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_TYPE_PLATFORM, LOCATION_SOURCE_TYPE_SOS);
            return;
        }

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

                    if (true)
                    {
                        Log.d(TAG, "onReceive: get the msg success................................");
                        RxjavaHttpModel.getInstance().pushGpsInfo(LOCATION_TYPE_DWSMS, LOCATION_SOURCE_TYPE_ORDINARY);
                        break;
                    }
                }
            }
            return;
        }
    }
}
