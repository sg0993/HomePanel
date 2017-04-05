package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;

/**
 * Created by H135901 on 3/14/2017.
 */

public class PreferenceManager {
    public static final String HOMEPANEL_CONFIG_NAME = "homepanelconfig";

    public static synchronized  void putIntConfig(Context context, String key, int value){
        if(null == context || TextUtils.isEmpty(key)){
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME,Context.MODE_PRIVATE);
        preferences.edit().putInt(key,value).apply();
    }

    public static synchronized void putStringConfig(Context context, String key, String value){
        if(null == context || TextUtils.isEmpty(key)|| TextUtils.isEmpty(value)){
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME,Context.MODE_PRIVATE);
        preferences.edit().putString(key,value).apply();
    }

    public static synchronized int getIntConfig(Context context,String key){
        int value = 0;
        if(null == context || TextUtils.isEmpty(key)){
            return value;
        }
        SharedPreferences preferences = context.getSharedPreferences(HOMEPANEL_CONFIG_NAME, Context.MODE_PRIVATE);
        value = preferences.getInt(key,value);
        return value;
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
}