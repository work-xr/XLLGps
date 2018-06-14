package com.hsf1002.sky.xllgps.model;



/**
 * Created by hefeng on 18-6-8.
 */

public interface BaseModel {
    /*void downloadInfo(RxjavaHttpPresenter.OnDownloadListener listener);
    void uploadInfo(RxjavaHttpPresenter.OnUploadListener listener);
    void reportInfo(RxjavaHttpPresenter.OnReportListener listener);*/
    void getGpsInfo();
    void pushGpsInfo();
}
