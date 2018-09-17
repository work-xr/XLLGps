package com.hsf1002.sky.xllgps.result;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by hefeng on 18-9-17.
 */

public class ResultGpsMsg {
    protected int status;
    protected String result;
    protected String description;

    public int getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    public static String getResultGpsMsgGson(ResultGpsMsg param)
    {
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.serializeNulls().create();

        return gson.toJson(param, ResultGpsMsg.class);
    }
}
