package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.CommonDevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/14/2017.
 */

public class CommonlDeviceManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static CommonlDeviceManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized CommonlDeviceManager getInstance(Context context) {
        if(instance == null){
            instance = new CommonlDeviceManager(context);
        }
        return instance;
    }
    private CommonlDeviceManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String name,String type){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_TYPE,type).getValues();
        return  DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_COMMONDEVICE,values);
    }

    public synchronized int deleteByUuid(String uuid) {
       return  DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_COMMONDEVICE,ConfigConstant.COLUMN_UUID , uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return  DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_COMMONDEVICE,primaryId);
    }

    private ContentValues putUpDateValues(CommonDevice device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mName)){
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if(!TextUtils.isEmpty(device.mType)){
            values.put(ConfigConstant.COLUMN_TYPE, device.mType);
        }
        return values;
    }
    public synchronized int updateByPrimaryId(long primaryId, CommonDevice device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return  DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_COMMONDEVICE,putUpDateValues(device),primaryId);
    }
    public synchronized int updateByUuid(String  uuid, CommonDevice device) {
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_COMMONDEVICE,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized CommonDevice getByPrimaryId(long primaryId) {
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper, ConfigConstant.TABLE_COMMONDEVICE,primaryId);
        CommonDevice device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }
    public synchronized List<CommonDevice>  getByDeviceType(String deviceType) {
        List<CommonDevice> commonDevices = null;
        if(TextUtils.isEmpty(deviceType)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_COMMONDEVICE,ConfigConstant.COLUMN_TYPE,deviceType);
        while(cursor.moveToNext()){
            if(null == commonDevices){
                commonDevices = new ArrayList<CommonDevice>();
            }
            CommonDevice device = fillDefault(cursor);
            commonDevices.add(device);
        }
        cursor.close();
        return commonDevices;
    }

    public synchronized CommonDevice getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_COMMONDEVICE,ConfigConstant.COLUMN_UUID,uuid);
        CommonDevice device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<CommonDevice> getCommonDeviceList() {
        List<CommonDevice> commonDevices = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_COMMONDEVICE);
        while(cursor.moveToNext()){
            if(null == commonDevices){
                commonDevices = new ArrayList<CommonDevice>();
            }
            CommonDevice device = fillDefault(cursor);
            commonDevices.add(device);
        }
        cursor.close();
        return commonDevices;
    }

    private CommonDevice fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        CommonDevice device = new CommonDevice();
        device.mType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TYPE));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        return device;
    }

    public void getScenarioList(JSONObject jsonObject) throws JSONException {
        List<CommonDevice> lists = getByDeviceType(CommonData.COMMONDEVICE_TYPE_SCENARIO);
        if (null == lists) {
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            CommonDevice loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopMapObject.put(CommonData.JSON_UUID_KEY, loop.mUuid);
            loopMapObject.put(CommonData.JSON_KEY_NAME, loop.mName);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY, loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY, CommonData.JSON_ERRORCODE_VALUE_OK);
    }
}
