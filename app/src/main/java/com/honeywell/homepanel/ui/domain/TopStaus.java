package com.honeywell.homepanel.ui.domain;

import android.content.Context;

import com.honeywell.homepanel.common.CommonData;

/**
 * Created by H135901 on 1/24/2017.
 */

public class TopStaus {

    private static TopStaus instance = null;
    public Context mContext = null;
    private  String mWeather = CommonData.WEATHER_SUNNY;
    private  int mTemperature = 25;
    private  String healthy = CommonData.UNHEALTHY;
    private  String mArmStatus = CommonData.ARMSTATUS_DISARM;
    private  int mWifiStatus = CommonData.WIFI_CONNECTED;


    public static synchronized TopStaus getInstance(Context context) {
        if (instance == null){
            instance = new TopStaus(context);
        }
        return instance;
    }

    private TopStaus(Context context) {
        mContext = context;
    }

    public String getWeather() {
        return mWeather;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public String getHealthy() {
        return healthy;
    }

    public String getArmStatus() {
        return mArmStatus;
    }

    public int getWifiStatus() {
        return mWifiStatus;
    }

    public void setWeather(String mWeather) {
        this.mWeather = mWeather;
    }

    public void setTemperature(int mTemperature) {
        this.mTemperature = mTemperature;
    }

    public void setHealthy(String healthy) {
        this.healthy = healthy;
    }

    public void setArmStatus(String mArmStatus) {
        this.mArmStatus = mArmStatus;
    }

    public void setWifiStatus(int mWifiStatus) {
        this.mWifiStatus = mWifiStatus;
    }
}
