package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.RelayLoop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/15/2017.
 */

public class RelayLoopManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static RelayLoopManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized RelayLoopManager getInstance(Context context) {
        if(instance == null){
            instance = new RelayLoopManager(context);
        }
        return instance;
    }
    private RelayLoopManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String moduleUuid,String uuid,String adapterUuid,String name,int loop,int delayTime,int enabled){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_MODULEUUID, moduleUuid)
                .put(ConfigConstant.COLUMN_ADAPTERNAME, adapterUuid)
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_LOOP, loop)
                .put(ConfigConstant.COLUMN_DELAYTIME, delayTime)
                .put(ConfigConstant.COLUMN_ENABLED,enabled).getValues();
        long rowId = DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_RELAYLOOP,values);
        return rowId;
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_RELAYLOOP,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_RELAYLOOP,primaryId);
    }

    private ContentValues putUpDateValues(RelayLoop device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mName)){
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if(device.mDelayTime > 0){
            values.put(ConfigConstant.COLUMN_DELAYTIME, device.mDelayTime);
        }
        if(!TextUtils.isEmpty(device.mAdapterUuid)) {
            values.put(ConfigConstant.COLUMN_ADAPTERNAME, device.mAdapterUuid);
        }

        values.put(ConfigConstant.COLUMN_ENABLED, device.mEnabled);
        return values;
    }
    public synchronized int updateByPrimaryId(long primaryId, RelayLoop device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_RELAYLOOP,putUpDateValues(device),primaryId);
    }
    public synchronized int updateByUuid(String  uuid, RelayLoop device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_RELAYLOOP,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized RelayLoop getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_RELAYLOOP,primaryId);
        RelayLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized RelayLoop getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_RELAYLOOP,ConfigConstant.COLUMN_UUID,uuid);
        RelayLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<RelayLoop> getRelayLoopAllList() {
        List<RelayLoop> RelayLoops = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_RELAYLOOP);
        while(cursor.moveToNext()){
            if(null == RelayLoops){
                RelayLoops = new ArrayList<RelayLoop>();
            }
            RelayLoop device = fillDefault(cursor);
            RelayLoops.add(device);
        }
        cursor.close();
        return RelayLoops;
    }

    private RelayLoop fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        RelayLoop device = new RelayLoop();
        device.mModuleUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_MODULEUUID));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mLoop = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_LOOP));
        device.mDelayTime = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_DELAYTIME));
        device.mEnabled = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_ENABLED));
        device.mAdapterUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ADAPTERNAME));
        device.mUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_RELAYLOOP_INT,device.mId);
        return device;
    }

    public synchronized List<RelayLoop>getByModuleUuid(String moduleUuid){
        if(TextUtils.isEmpty(moduleUuid)){
            return null;
        }
        List<RelayLoop>relayLoops = null;
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_RELAYLOOP,ConfigConstant.COLUMN_MODULEUUID,moduleUuid);
        while(cursor.moveToNext()){
            if(null == relayLoops){
                relayLoops = new ArrayList<RelayLoop>();
            }
            RelayLoop device = fillDefault(cursor);
            relayLoops.add(device);
        }
        cursor.close();
        return  relayLoops;
    }
    public void extensionRelayGet(JSONObject jsonObject)throws JSONException {
        List<RelayLoop>lists = getByModuleUuid(jsonObject.getString(CommonJson.JSON_UUID_KEY));
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            RelayLoop loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }
    private void loopToJson(JSONObject loopMapObject, RelayLoop loop) throws  JSONException{
        loopMapObject.put(CommonJson.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonJson.JSON_ALIASNAME_KEY,loop.mName);
        loopMapObject.put(CommonData.JSON_KEY_LOOP,loop.mLoop);
        loopMapObject.put(CommonData.JSON_KEY_DELAYTIME,loop.mDelayTime+"");
        loopMapObject.put(CommonData.JSON_KEY_ENABLE,loop.mEnabled+"");
        loopMapObject.put(CommonData.JSON_KEY_ADAPTERNAME, loop.mAdapterUuid);
        loopMapObject.put(CommonData.JSON_KEY_MODULEUUID, loop.mModuleUuid);
    }

    public void relayUpdate(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            RelayLoop loop = getByUuid(uuid);
            if(null == loop){
                continue;
            }
            loopForUpdate(loop,loopMapObject);
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            if(num > 0 && loopMapObject.has(CommonJson.JSON_ALIASNAME_KEY)){
                DbCommonUtil.updateCommonName(mContext,uuid,loopMapObject.getString(CommonJson.JSON_ALIASNAME_KEY), loop.mEnabled);
            }
        }
    }

    private void loopForUpdate(RelayLoop loop, JSONObject loopMapObject) {
        if(loopMapObject.has(CommonJson.JSON_ALIASNAME_KEY)) {
            loop.mName = loopMapObject.optString(CommonJson.JSON_ALIASNAME_KEY);
        }
        if(loopMapObject.has(CommonData.JSON_KEY_DELAYTIME)){
            loop.mDelayTime = Integer.valueOf(loopMapObject.optString(CommonData.JSON_KEY_DELAYTIME));
        }
        if(loopMapObject.has(CommonData.JSON_KEY_ENABLE)){
            loop.mEnabled = Integer.valueOf(loopMapObject.optString(CommonData.JSON_KEY_ENABLE));
        }
        if(loopMapObject.has(CommonData.JSON_KEY_ADAPTERNAME)){
            loop.mAdapterUuid = loopMapObject.optString(CommonData.JSON_KEY_ADAPTERNAME);
        }
    }

    public void relayDelete(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            if(num > 0){
                CommonDeviceManager.getInstance(mContext).deleteByUuid(uuid);
            }
        }
    }

    public void getDeviceLoopDetailsInfo(JSONObject jsonObject) {
        JSONArray loopMapArray = jsonObject.optJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        Cursor cursor = DbCommonUtil.getDeviceLoopDetails(dbHelper, ConfigConstant.TABLE_RELAYLOOP);
        // Use the original jsonArray if JSONObject contains one, construct new JSONArray otherwise
        if (loopMapArray == null) {
            loopMapArray = new JSONArray();
        }
        try{
            // query loop from database and fill json object
            while(cursor.moveToNext()){
                RelayLoop loop = fillDefault(cursor);
                if(null == loop){
                    continue;
                }
                JSONObject loopMapObject = new JSONObject();
                String devLoopType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TYPE));
                loopMapObject.put(CommonData.JSON_TYPE_KEY, devLoopType);
                loopToJson(loopMapObject, loop);
                loopMapArray.put(loopMapObject);
            }
            // updatet jsonObject passed in
            jsonObject.put(CommonJson.JSON_LOOPMAP_KEY, loopMapArray);
            jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(null != cursor){
                cursor.close();
            }
        }
    }
}
