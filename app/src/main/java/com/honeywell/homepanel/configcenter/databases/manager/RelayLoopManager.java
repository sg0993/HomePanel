package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.RelayLoop;

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

    public synchronized long add(String moduleUuid,String uuid,String name,int loop,int delayTime,int enabled){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_MODULEUUID, moduleUuid)
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_LOOP, loop)
                .put(ConfigConstant.COLUMN_DELAYTIME, delayTime)
                .put(ConfigConstant.COLUMN_ENABLED,enabled).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_RELAYLOOP,values);
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
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mLoop = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_LOOP));
        device.mDelayTime = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_DELAYTIME));
        device.mEnabled = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_ENABLED));
        return device;
    }
}
