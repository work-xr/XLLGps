package com.hsf1002.sky.xllgps.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hefeng on 18-7-19.
 */

public class DateTimeUtils {
    private static final String TAG = "DateTimeUtils";

    public static String getFormatCurrentTime()
    {
        Date date= new Date();
        SimpleDateFormat smft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 2016-04-26 13:22:22
        String nowTimeString = smft.format(date.getTime());

        return nowTimeString;
    }
}
