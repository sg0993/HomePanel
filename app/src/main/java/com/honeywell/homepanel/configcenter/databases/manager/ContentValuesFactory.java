package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;

/**
 * Created by H135901 on 3/14/2017.
 */

public class ContentValuesFactory {
    private ContentValues mValues = new ContentValues();
    private static final int MAX_VALUE_LENGTH = 1024;
    public ContentValuesFactory put(String key, String value) {
        if(null == value){
            value = "";
        }
        if(value.length() > MAX_VALUE_LENGTH){
            value = value.substring(0, MAX_VALUE_LENGTH-1);
        }
        if(null != key && null != value){
            mValues.put(key, value);
        }
        return this;
    }

    public ContentValuesFactory putAll(ContentValues other) {
        mValues.putAll(other);
        return this;
    }

    public ContentValuesFactory put(String key, Byte value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key,Short value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key, Integer value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key,Long value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key,Float value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key, Double value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key,Boolean value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory put(String key, byte[] value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesFactory putNull(String key) {
        mValues.putNull(key);
        return this;
    }

    public ContentValues getValues() {
        return mValues;
    }
}
