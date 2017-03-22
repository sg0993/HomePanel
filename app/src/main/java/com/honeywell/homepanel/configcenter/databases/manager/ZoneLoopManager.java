package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.ZoneLoop;

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
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mLoop = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_LOOP));
        device.mDelayTime = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_DELAYTIME));
        device.mEnabled = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_ENABLED));
        device.mZoneType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ZONETYPE));
        device.mAlarmType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ALARMTYPE));
        return device;
    }
}
