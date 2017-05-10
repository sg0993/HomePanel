package com.honeywell.homepanel.ui.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.databases.manager.PreferenceManager;

/**
 * Created by H135901 on 1/24/2017.
 */

public class TopStaus {
    private static final String TAG = "TopStaus";
    private static TopStaus instance = null;
    public Context mContext = null;
    private  String mWeather = CommonData.WEATHER_SUNNY;
    private  int mTemperature = 15;
    private  String healthy = CommonData.UNHEALTHY;
    private  String mArmStatus = CommonData.ARMSTATUS_DISARM;
    private  int mWifiStatus = CommonData.DISCONNECT;
    private  int mServerStatus = CommonData.DISCONNECT;
    private  int mIPDCStatus = CommonData.DISCONNECT;

    public int getmServerStatus() {
        return mServerStatus;
    }
    private SharedPreferences mPrefence = null;
    private static  final  String SCENARIO_NAME = PreferenceManager.HOMEPANEL_CONFIG_NAME;
    private static  final  String SCENARIOKEY_NAME = CommonData.KEY_CURSCENARIO;

    public void setmServerStatus(int mServerStatus) {
        Log.d(TAG, "setmServerStatus: mServerStatus=" + mServerStatus);
        this.mServerStatus = mServerStatus;
    }

    public int getmIPDCStatus() {
        return mIPDCStatus;
    }

    public void setmIPDCStatus(int mIPDCStatus) {
        this.mIPDCStatus = mIPDCStatus;
    }

    //public int mCurScenario = CommonData.SCENARIO_AWAY;//CommonData.SCENARIO_HOME;


    public static synchronized TopStaus getInstance(Context context) {
        if (instance == null){
            instance = new TopStaus(context);
        }
        return instance;
    }
    public int getCurScenario(){
        return mPrefence.getInt(SCENARIOKEY_NAME,CommonData.SCENARIO_AWAY);
    }
    public void setCurScenario(int scenario){
        mPrefence.edit().putInt(SCENARIOKEY_NAME,scenario).apply();
    }
    private TopStaus(Context context) {
        mContext = context;
        mPrefence = context.getSharedPreferences(SCENARIO_NAME,Context.MODE_PRIVATE);
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
