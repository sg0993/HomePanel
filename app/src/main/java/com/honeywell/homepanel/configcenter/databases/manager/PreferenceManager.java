package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigDispatchCenter;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;

/**
 * Created by H135901 on 3/14/2017.
 */

public class PreferenceManager {
    public static final String HOMEPANEL_CONFIG_NAME = "homepanelconfig";

    public static synchronized void putBooleanConfig(Context context, String key, boolean value) {
        if (context == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();

        onPrivateConfigurationChanged(key);
    }

    public static synchronized boolean getBooleanConfig(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return false;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static synchronized  void putIntConfig(Context context, String key, int value){
        if(null == context || TextUtils.isEmpty(key)){
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME,Context.MODE_PRIVATE);
        preferences.edit().putInt(key,value).apply();

        onPrivateConfigurationChanged(key);
    }

    public static synchronized void putStringConfig(Context context, String key, String value){
        if(null == context || TextUtils.isEmpty(key)|| TextUtils.isEmpty(value)){
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME,Context.MODE_PRIVATE);
        preferences.edit().putString(key,value).apply();

        onPrivateConfigurationChanged(key);
    }

    public static synchronized int getIntConfig(Context context,String key){
        int value = getDefaultValueInt(key);
        if(null == context || TextUtils.isEmpty(key)){
            return value;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME, Context.MODE_PRIVATE);
        value = preferences.getInt(key,value);
        return value;
    }

    private static int getDefaultValueInt(String key) {
        int value = 0;
        if(!TextUtils.isEmpty(key) && key.contains(CommonData.KEY_VOLUME_PREFIX)){
            value = CommonData.VOLUME_VALUE_DEFAULT;
        }
        return  value;
    }

    public static synchronized String getStringConfig(Context context,String key){
        String value = "unknown";
        if(null == context || TextUtils.isEmpty(key)){
            return value;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME, Context.MODE_PRIVATE);
        value = preferences.getString(key,value);
        return value;
    }

    public static synchronized void updateVersionId(Context context){
        if(null == context){
            return;
        }
        int versionId = getIntConfig(context, ConfigConstant.KEY_DBVERSIONID);
        versionId += 1;
        putIntConfig(context,ConfigConstant.KEY_DBVERSIONID,versionId);
    }

    public static void onPrivateConfigurationChanged(String contentTitle){
        if (!TextUtils.isEmpty(contentTitle)) {
            ConfigDispatchCenter.getInstance().broadcastConfigurationUpdated(CommonData.JSON_CONFIGDATA_CATEGORY_PRIVATE, contentTitle);
        }
    }
}
