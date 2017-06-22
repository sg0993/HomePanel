package com.honeywell.homepanel.ui.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.databases.manager.PreferenceManager;
import com.honeywell.homepanel.ui.activities.MainActivity;

/**
 * Created by H135901 on 1/24/2017.
 */

public class TopStaus {
    private static final String TAG = "TopStaus";
    private static TopStaus instance = null;
    public Context mContext = null;
    private  String mWeather = null;//CommonData.WEATHER_SUNNY;
    private  String healthy = null;
    private  String mArmStatus = CommonData.ARMSTATUS_DISARM;
    private  int mTemperature = 0xFFFF;
    private  int mWifiStatus = CommonData.DISCONNECT;
    private  int mServerStatus = CommonData.DISCONNECT;
    private  int mFrontIPDCStatus = CommonData.DISCONNECT;
    private  int mBackIPDCStatus = CommonData.DISCONNECT;
    private  boolean CloudBoundStatus = false;

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

    public int getmFrontIPDCStatus() {
        return mFrontIPDCStatus;
    }

    public void setmFrontIPDCStatus(int mFrontIPDCStatus) {
        Log.d(TAG, "setmFrontIPDCStatus: mFrontIPDCStatus:"+ mFrontIPDCStatus +",,,1111111111");
        this.mFrontIPDCStatus = mFrontIPDCStatus;
    }

    public int getmBackIPDCStatus() {
        return mBackIPDCStatus;
    }


    public void setmBackIPDCStatus(int mBackIPDCStatus) {
        Log.d(TAG, "setmBackIPDCStatus: mBackIPDCStatus:"+ mBackIPDCStatus +",,,1111111111");
        this.mBackIPDCStatus = mBackIPDCStatus;
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

    public boolean isCloudBoundStatus() {
        return CloudBoundStatus;
    }

    public void setCloudBoundStatus(boolean cloudBoundStatus) {
        if (MainActivity.mHomePanelType == CommonData.HOMEPANEL_TYPE_MAIN) {//only mainphone need show bound status
            CloudBoundStatus = cloudBoundStatus;
        }
    }
}
