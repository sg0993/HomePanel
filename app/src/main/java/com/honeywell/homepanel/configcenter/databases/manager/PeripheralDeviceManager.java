package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.PeripheralDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/14/2017.
 */

public class PeripheralDeviceManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static PeripheralDeviceManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized PeripheralDeviceManager getInstance(Context context) {
        if(instance == null){
            instance = new PeripheralDeviceManager(context);
        }
        return instance;
    }
    private PeripheralDeviceManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String moduleUuid,String name,String type, String ipAddr,String macAddr,String version,int onLine){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_TYPE, type)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_IP, ipAddr)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_MAC, macAddr)
                .put(ConfigConstant.COLUMN_MODULEUUID,moduleUuid)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE,onLine)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_VERSION, version).getValues();

        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_PERIPHERALDEVICE,values);
    }

    public synchronized int deleteByModuleUuid(String  moduleUuid) {
        int num = -1;
        if(TextUtils.isEmpty(moduleUuid)){
            return num;
        }
        PeripheralDevice device = getByModuleUuid(moduleUuid);
        if(null == device){
            return num;
        }
        num = DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_PERIPHERALDEVICE,ConfigConstant.COLUMN_MODULEUUID,moduleUuid);
        if(num > 0){
            //TODO:delete all  device loop of such moduleuuid
        }
        return num;
    }

    public synchronized int deleteByPrimaryId(long primaryId) {

        int num = -1;
        PeripheralDevice device = getByPrimaryId(primaryId);
        if(null == device){
            return num;
        }
        num = DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_PERIPHERALDEVICE,primaryId);
        if(num > 0){
            //TODO:delete all  device loop of such moduleuuid
        }
        return  num;
    }

    private ContentValues putUpDateValues(PeripheralDevice device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mName)){
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if(!TextUtils.isEmpty(device.mVersion) && !device.mVersion.equals(CommonData.WIFIMODULE_DEFAULT_VERSION)){
            values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_VERSION, device.mVersion);
        }
        if(!TextUtils.isEmpty(device.mIpAddr) && CommonUtils.isIp(device.mIpAddr)){
            values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_IP, device.mIpAddr);
        }
        if(device.mOnLine == CommonData.ONLINE || device.mOnLine == CommonData.NOTONLINE){
            values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE, device.mOnLine);
        }
        return values;
    }
    public synchronized int updateByPrimaryId(long primaryId, PeripheralDevice device, boolean updateOnline) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        int num = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUpDateValues(device);
        try {
            num =  db.update(ConfigConstant.TABLE_PERIPHERALDEVICE, values,
                    ConfigConstant.COLUMN_ID + "=?",
                    new String[]{String.valueOf(primaryId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0 && !updateOnline){
            PreferenceManager.updateVersionId(mContext);
        }
        return num;
    }
    public synchronized int updateByModuleUuid(String  moduleUuid, PeripheralDevice device, boolean updateOnline) {
        int num = -1;
        if(null == device || TextUtils.isEmpty(moduleUuid)){
            return num;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUpDateValues(device);
        try {
            num =  db.update(ConfigConstant.TABLE_PERIPHERALDEVICE, values,
                    ConfigConstant.COLUMN_MODULEUUID + "=?",
                    new String[]{moduleUuid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0 && !updateOnline){
            PreferenceManager.updateVersionId(mContext);
        }
        return num;
    }

    public synchronized PeripheralDevice getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_PERIPHERALDEVICE,primaryId);
        PeripheralDevice device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized PeripheralDevice getByModuleUuid(String moduleUuid) {
        if(TextUtils.isEmpty(moduleUuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_PERIPHERALDEVICE,ConfigConstant.COLUMN_MODULEUUID,moduleUuid);
        PeripheralDevice device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<PeripheralDevice> getPeripheralDeviceAllList() {
        List<PeripheralDevice> peripheralDevices = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_PERIPHERALDEVICE);
        while(cursor.moveToNext()){
            if(null == peripheralDevices){
                peripheralDevices = new ArrayList<PeripheralDevice>();
            }
            PeripheralDevice device = fillDefault(cursor);
            peripheralDevices.add(device);
        }
        cursor.close();
        return peripheralDevices;
    }

    private PeripheralDevice fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        PeripheralDevice device = new PeripheralDevice();
        device.mType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_PERIPHERALDEVICE_TYPE));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mIpAddr = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_PERIPHERALDEVICE_IP));
        device.mMacAddr = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_PERIPHERALDEVICE_MAC));
        device.mOnLine = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE));
        device.mVersion = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_PERIPHERALDEVICE_VERSION));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mModuleUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_MODULEUUID));
        return device;
    }
}
