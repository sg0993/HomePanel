package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.ZoneLoop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/15/2017.
 */

public class ZoneLoopManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static ZoneLoopManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized ZoneLoopManager getInstance(Context context) {
        if(instance == null){
            instance = new ZoneLoopManager(context);
        }
        return instance;
    }
    private ZoneLoopManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String moduleUuid,String uuid,String name,int loop,int delayTime,int enabled,
                                 String zoneType,String alarmType){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_MODULEUUID, moduleUuid)
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_LOOP, loop)
                .put(ConfigConstant.COLUMN_DELAYTIME, delayTime)
                .put(ConfigConstant.COLUMN_ENABLED,enabled)
                .put(ConfigConstant.COLUMN_ZONETYPE,zoneType)
                .put(ConfigConstant.COLUMN_ALARMTYPE,alarmType).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_ZONELOOP,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_ZONELOOP,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_ZONELOOP,primaryId);
    }

    private ContentValues putUpDateValues(ZoneLoop device){
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
        values.put(ConfigConstant.COLUMN_ENABLED, device.mEnabled);

        if(!TextUtils.isEmpty(device.mZoneType)){
            values.put(ConfigConstant.COLUMN_ZONETYPE, device.mZoneType);
        }
        if(!TextUtils.isEmpty(device.mAlarmType)){
            values.put(ConfigConstant.COLUMN_ALARMTYPE, device.mAlarmType);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, ZoneLoop device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_ZONELOOP,putUpDateValues(device),primaryId);
    }

    public synchronized int updateByUuid(String  uuid, ZoneLoop device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_ZONELOOP,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized ZoneLoop getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_ZONELOOP,primaryId);
        ZoneLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized ZoneLoop getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_ZONELOOP,ConfigConstant.COLUMN_UUID,uuid);
        ZoneLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }
    public synchronized List<ZoneLoop>getByModuleUuid(String moduleUuid){
        if(TextUtils.isEmpty(moduleUuid)){
            return null;
        }
        List<ZoneLoop>ZoneLoops = null;
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_ZONELOOP,ConfigConstant.COLUMN_MODULEUUID,moduleUuid);
        while(cursor.moveToNext()){
            if(null == ZoneLoops){
                ZoneLoops = new ArrayList<ZoneLoop>();
            }
            ZoneLoop device = fillDefault(cursor);
            ZoneLoops.add(device);
        }
        cursor.close();
        return  ZoneLoops;
    }

    public synchronized List<ZoneLoop> getZoneLoopAllList() {
        List<ZoneLoop> ZoneLoops = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_ZONELOOP);
        while(cursor.moveToNext()){
            if(null == ZoneLoops){
                ZoneLoops = new ArrayList<ZoneLoop>();
            }
            ZoneLoop device = fillDefault(cursor);
            ZoneLoops.add(device);
        }
        cursor.close();
        return ZoneLoops;
    }

    private ZoneLoop fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        ZoneLoop device = new ZoneLoop();
        device.mModuleUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_MODULEUUID));
//        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mLoop = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_LOOP));
        device.mDelayTime = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_DELAYTIME));
        device.mEnabled = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_ENABLED));
        device.mZoneType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ZONETYPE));
        device.mAlarmType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ALARMTYPE));
        device.mUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_ZONELOOP_INT,device.mId);
        return device;
    }

    public void zoneUpdate(JSONObject jsonObject) throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            ZoneLoop loop = getByUuid(uuid);
            loopForUpdate(loop,loopMapObject);
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            if(num > 0 && loopMapObject.has(CommonData.JSON_ALIASNAME_KEY)){
               DbCommonUtil.updateCommonName(mContext,uuid,loopMapObject.getString(CommonData.JSON_ALIASNAME_KEY));
            }
        }
    }

    public  void loopForUpdate(ZoneLoop loop,JSONObject loopMapObject) throws  JSONException{
        if(loopMapObject.has(CommonData.JSON_ALIASNAME_KEY)){
            loop.mName = loopMapObject.optString(CommonData.JSON_ALIASNAME_KEY);
        }
        if(loopMapObject.has(CommonData.JSON_KEY_ALARMTYPE)){
            loop.mAlarmType = loopMapObject.optString(CommonData.JSON_KEY_ALARMTYPE);
        }
        if(loopMapObject.has(CommonData.JSON_KEY_ZONETYPE)){
            loop.mZoneType = loopMapObject.optString(CommonData.JSON_KEY_ZONETYPE);
        }
        if(loopMapObject.has(CommonData.JSON_KEY_DELAYTIME)){
            loop.mDelayTime = Integer.valueOf(loopMapObject.optString(CommonData.JSON_KEY_DELAYTIME));
        }
        if(loopMapObject.has(CommonData.JSON_KEY_ENABLE)){
            loop.mEnabled = Integer.valueOf(loopMapObject.optString(CommonData.JSON_KEY_ENABLE));
        }
    }
    public void extensionZoneGet(JSONObject jsonObject) throws  JSONException{
        List<ZoneLoop>lists = getByModuleUuid(jsonObject.getString(CommonData.JSON_UUID_KEY));
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            ZoneLoop loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, ZoneLoop loop) throws  JSONException{
        loopMapObject.put(CommonData.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_ALIASNAME_KEY,loop.mName);
        loopMapObject.put(CommonData.JSON_KEY_LOOP,loop.mLoop);
        loopMapObject.put(CommonData.JSON_KEY_DELAYTIME,loop.mDelayTime+"");
        loopMapObject.put(CommonData.JSON_KEY_ENABLE,loop.mEnabled+"");
        loopMapObject.put(CommonData.JSON_KEY_ZONETYPE,loop.mZoneType);
        loopMapObject.put(CommonData.JSON_KEY_ALARMTYPE,loop.mAlarmType);
    }

    public void extensionZoneDelete(JSONObject jsonObject) throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            if(num > 0){
                CommonlDeviceManager.getInstance(mContext).deleteByUuid(uuid);
            }
        }
    }

    public void localZoneGet(JSONObject jsonObject) throws  JSONException{
        List<ZoneLoop>lists = getZoneLoopAllList();
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            ZoneLoop loop = lists.get(i);
            if(!TextUtils.isEmpty(loop.mModuleUuid)){
                break;
            }
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }
}
