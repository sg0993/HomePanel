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
import com.honeywell.homepanel.configcenter.databases.domain.RelayLoop;
import com.honeywell.homepanel.configcenter.databases.domain.ZoneLoop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void extensionModuleAdd(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String type = loopMapObject.optString(CommonData.JSON_TYPE_KEY);
            String version = loopMapObject.optString(CommonData.JSON_KEY_VERSION);
            String ip = loopMapObject.optString(CommonData.JSON_IP_KEY);
            String mac = loopMapObject.optString(CommonData.JSON_KEY_MAC);
            String name = type;
            String moduleUuid = UUID.randomUUID().toString();
            long rowId = add(moduleUuid,name,type,ip,mac,version,CommonData.NOTONLINE);
            //TODO add loop automaticlly for relay  and zone,and commondevice
            DbCommonUtil.putErrorCodeFromOperate(rowId,loopMapObject);
            if(rowId > 0){
               int loopNum = CommonData.RELAY_LOOP_NUM;
                if(type.equals(CommonData.COMMONDEVICE_TYPE_RELAY)){
                    loopNum = CommonData.RELAY_LOOP_NUM;
                }
                else if(type.equals(CommonData.COMMONDEVICE_TYPE_ZONE)){
                    loopNum = CommonData.ZONE_LOOP_NUM;
                }
                for (int j = 0; j < loopNum; j++) {
                    long _id = 0;
                    String deviceName = type+j+1;
                    String deviceUuid = "";
                    if(type.equals(CommonData.COMMONDEVICE_TYPE_RELAY)){
                        deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_RELAYLOOP_INT,DbCommonUtil.getSequenct(ConfigDatabaseHelper.getInstance(mContext),ConfigConstant.TABLE_RELAYLOOP));
                        _id = RelayLoopManager.getInstance(mContext).add(moduleUuid,deviceUuid,deviceName,j+1,0,CommonData.DISENABLE);
                    }
                    else if(type.equals(CommonData.COMMONDEVICE_TYPE_ZONE)){
                        deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_ZONELOOP_INT,DbCommonUtil.getSequenct(ConfigDatabaseHelper.getInstance(mContext),ConfigConstant.TABLE_ZONELOOP));
                        _id = ZoneLoopManager.getInstance(mContext).add(moduleUuid,deviceUuid,deviceName,
                                j+1,0,CommonData.DISENABLE,CommonData.ZONETYPE_24H,CommonData.ALARMTYPE_EMERGENCY);
                    }
                    if(_id > 0){
                        CommonlDeviceManager.getInstance(mContext).add(deviceUuid,deviceName,type);
                    }
                }
            }
        }
    }

    public void getExtensionModuleList(JSONObject jsonObject) throws JSONException {
        List<PeripheralDevice> lists = getPeripheralDeviceAllList();
        if (null == lists) {
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            PeripheralDevice loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY, loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY, CommonData.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, PeripheralDevice loop) throws JSONException{
        loopMapObject.put(CommonData.JSON_UUID_KEY, loop.mModuleUuid);
        loopMapObject.put(CommonData.JSON_ALIASNAME_KEY, loop.mName);
        loopMapObject.put(CommonData.JSON_ONLINE_KEY, loop.mOnLine+"");
        loopMapObject.put(CommonData.JSON_TYPE_KEY, loop.mType);
        loopMapObject.put(CommonData.JSON_IP_KEY, loop.mIpAddr);
        loopMapObject.put(CommonData.JSON_KEY_MAC, loop.mMacAddr);
    }

    public void extensionModuleDelete(JSONObject jsonObject) throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            PeripheralDevice device = getByModuleUuid(uuid);
            long num = deleteByModuleUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            //delete zone loop,relay loop
            //delete common device loop
            if(device.mType.equals(CommonData.COMMONDEVICE_TYPE_RELAY)){
                List<RelayLoop>relayLoops = RelayLoopManager.getInstance(mContext).getByModuleUuid(uuid);
                if(null != relayLoops){
                    for (int j = 0; j < relayLoops.size(); j++) {
                        String relayUuid = relayLoops.get(j).mUuid;
                        RelayLoopManager.getInstance(mContext).deleteByUuid(relayUuid);
                        CommonlDeviceManager.getInstance(mContext).deleteByUuid(relayUuid);
                    }
                }
            }
            else if(device.mType.equals(CommonData.COMMONDEVICE_TYPE_ZONE)){
                List<ZoneLoop>zoneLoops = ZoneLoopManager.getInstance(mContext).getByModuleUuid(uuid);
                if(null != zoneLoops) {
                    for (int j = 0; j < zoneLoops.size(); j++) {
                        String zoneUuid = zoneLoops.get(j).mUuid;
                        ZoneLoopManager.getInstance(mContext).deleteByUuid(zoneUuid);
                        CommonlDeviceManager.getInstance(mContext).deleteByUuid(zoneUuid);
                    }
                }
            }
        }
    }
}
