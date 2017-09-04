package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
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

/**
 * Created by H135901 on 3/14/2017.
 */

public class PeripheralDeviceManager {
    private Context mContext = null;
    private static final String TAG = ConfigService.TAG;
    private static PeripheralDeviceManager instance = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized PeripheralDeviceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PeripheralDeviceManager(context);
        }
        return instance;
    }

    private PeripheralDeviceManager(Context context) {
        mContext = context;
        dbHelper = ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String moduleUuid, String name, String type, String model, String ipAddr, String macAddr, String version, int onLine) {
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_TYPE, type)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_IP, ipAddr)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_MAC, macAddr)
                .put(ConfigConstant.COLUMN_MODULEUUID, moduleUuid)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE, onLine)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_VERSION, version)
                .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_MODEL, model)
                .getValues();

        return DbCommonUtil.add(mContext, dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, values);
    }

    public synchronized int deleteByModuleUuid(String moduleUuid) {
        int num = -1;
        if (TextUtils.isEmpty(moduleUuid)) {
            return num;
        }
        PeripheralDevice device = getByModuleUuid(moduleUuid);
        if (null == device) {
            return num;
        }
        num = DbCommonUtil.deleteByStringFieldType(mContext, dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, ConfigConstant.COLUMN_MODULEUUID, moduleUuid);
        return num;
    }

    public synchronized int deleteByPrimaryId(long primaryId) {

        int num = -1;
        PeripheralDevice device = getByPrimaryId(primaryId);
        if (null == device) {
            return num;
        }
        num = DbCommonUtil.deleteByPrimaryId(mContext, dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, primaryId);
        return num;
    }

    private ContentValues putUpDateValues(PeripheralDevice device) {
        ContentValues values = new ContentValues();
        if (null == device) {
            return values;
        }
        if (!TextUtils.isEmpty(device.mName)) {
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if (!TextUtils.isEmpty(device.mVersion) && !device.mVersion.equals(CommonData.WIFIMODULE_DEFAULT_VERSION)) {
            values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_VERSION, device.mVersion);
        }
        if (!TextUtils.isEmpty(device.mIpAddr) && CommonUtils.isIp(device.mIpAddr)) {
            values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_IP, device.mIpAddr);
        }

        if (device.mOnLine == CommonData.ONLINE || device.mOnLine == CommonData.NOTONLINE) {
            values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE, device.mOnLine);
        }
        return values;
    }

    private ContentValues putUpdateItems(PeripheralDevice device) {
        ContentValues values = new ContentValues();
        if (null == device) {
            return values;
        }

        values.put(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE, CommonData.NOTONLINE);

        return values;
    }


    public synchronized int updateByPrimaryId(long primaryId, PeripheralDevice device, boolean updateOnline) {
        if (null == device || primaryId <= 0) {
            return -1;
        }
        int num = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUpDateValues(device);
        try {
            num = db.update(ConfigConstant.TABLE_PERIPHERALDEVICE, values,
                    ConfigConstant.COLUMN_ID + "=?",
                    new String[]{String.valueOf(primaryId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0 && !updateOnline) {
            DbCommonUtil.onPublicConfigurationChanged(mContext, ConfigConstant.TABLE_PERIPHERALDEVICE);
        }
        return num;
    }

    public synchronized int updateByModuleUuid(String moduleUuid, PeripheralDevice device, boolean updateOnline) {
        int num = -1;
        if (null == device || TextUtils.isEmpty(moduleUuid)) {
            return num;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUpDateValues(device);
        try {
            num = db.update(ConfigConstant.TABLE_PERIPHERALDEVICE, values,
                    ConfigConstant.COLUMN_MODULEUUID + "=?",
                    new String[]{moduleUuid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            DbCommonUtil.onPublicConfigurationChanged(mContext, ConfigConstant.TABLE_PERIPHERALDEVICE);
        }
        return num;
    }

    /**
     * @param device update all rows by selectionArgs
     * @return
     */
    public synchronized int updateBySelection(PeripheralDevice device, String type, String[] args) {
        int num = -1;
        if (null == device) {
            return num;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUpdateItems(device);
        try {
            num = db.update(ConfigConstant.TABLE_PERIPHERALDEVICE, values, type + "=? OR " + type + " = ?", args);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(num > 0){
//            DbCommonUtil.onPublicConfigurationChanged(mContext, ConfigConstant.TABLE_PERIPHERALDEVICE);
//        } comment out by xiaochao
        return num;
    }

    public void getDeviceLoopDetailsInfo(JSONObject jsonObject) {
        JSONArray loopMapArray = jsonObject.optJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        StringBuilder sqlBuilder = new StringBuilder()
                .append("select * from ").append(ConfigConstant.TABLE_PERIPHERALDEVICE)
                .append(" where ").append(ConfigConstant.COLUMN_TYPE)
                .append(" like '").append(CommonData.COMMONDEVICE_TYPE_IPDC).append("%'");
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlBuilder.toString(), null);

        // Use the original jsonArray if JSONObject contains one, construct new JSONArray otherwise
        if (loopMapArray == null) {
            loopMapArray = new JSONArray();
        }

        try {
            // query loop from database and fill json object
            while (cursor.moveToNext()) {
                PeripheralDevice loop = fillDefault(cursor);
                if (null == loop) {
                    continue;
                }
                JSONObject loopMapObject = new JSONObject();

                loopToJson(loopMapObject, loop);
                loopMapArray.put(loopMapObject);
            }
            // updatet jsonObject passed in
            jsonObject.put(CommonJson.JSON_LOOPMAP_KEY, loopMapArray);
            jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // close cursor
            cursor.close();
        }
    }

    public synchronized PeripheralDevice getByPrimaryId(long primaryId) {
        if (primaryId <= 0) {
            return null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, primaryId);
        PeripheralDevice device = null;
        while (cursor.moveToNext()) {
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized PeripheralDevice getByModuleUuid(String moduleUuid) {
        if (TextUtils.isEmpty(moduleUuid)) {
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, ConfigConstant.COLUMN_MODULEUUID, moduleUuid);
        PeripheralDevice device = null;
        while (cursor.moveToNext()) {
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<PeripheralDevice> getPeripheralDeviceAllList() {
        List<PeripheralDevice> peripheralDevices = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE);
        while (cursor.moveToNext()) {
            if (null == peripheralDevices) {
                peripheralDevices = new ArrayList<PeripheralDevice>();
            }
            PeripheralDevice device = fillDefault(cursor);
            peripheralDevices.add(device);
        }
        cursor.close();
        return peripheralDevices;
    }

    private PeripheralDevice fillDefault(Cursor cursor) {
        if (null == cursor) {
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
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String moduleType = loopMapObject.optString(CommonData.JSON_TYPE_KEY);
            String version = loopMapObject.optString(CommonData.JSON_KEY_VERSION);
            String ip = loopMapObject.optString(CommonData.JSON_IP_KEY);
            String mac = loopMapObject.optString(CommonData.JSON_KEY_MAC);
            String model = loopMapObject.optString(CommonData.JSON_KEY_MODEL);
            int loopNum = Integer.parseInt(loopMapObject.optString(CommonData.JSON_KEY_LOOP, "0"));
            String moduleUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_PERIPHERALDEVICE_INT, DbCommonUtil.getSequenct(ConfigDatabaseHelper.getInstance(mContext), ConfigConstant.TABLE_PERIPHERALDEVICE));
            String name = moduleType;

            long rowId = add(moduleUuid, name, moduleType, model, ip, mac, version, CommonData.NOTONLINE);
            //add loop automaticlly for relay  and zone,and commondevice
            DbCommonUtil.putErrorCodeFromOperate(rowId, loopMapObject);
            if (rowId > 0) {
                for (int j = 0; j < loopNum; j++) {
                    long _id = 0;
                    String deviceUuid = "";
                    String deviceName = name + moduleUuid + (j + 1);

                    if (moduleType.equals(CommonData.JSON_MODULE_NAME_RELAY)) {
                        deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_RELAYLOOP_INT, DbCommonUtil.getSequenct(ConfigDatabaseHelper.getInstance(mContext), ConfigConstant.TABLE_RELAYLOOP));
                        _id = RelayLoopManager.getInstance(mContext).add(moduleUuid, deviceUuid, CommonData.DEVADAPTER_RELAY_HEJMODULE_HON,
                                deviceName, j + 1, 0, CommonData.DISENABLE);
                    } else if (moduleType.equals(CommonData.JSON_KEY_DEVICETYPE_WIREDZONE)) {
                        deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_ZONELOOP_INT, DbCommonUtil.getSequenct(ConfigDatabaseHelper.getInstance(mContext), ConfigConstant.TABLE_ZONELOOP));
                        _id = ZoneLoopManager.getInstance(mContext).add(moduleUuid, deviceUuid, CommonData.DEVADAPTER_ZONE_HEJMODULE_HON,
                                deviceName, j + 1, 0, CommonData.DISENABLE, CommonData.ZONETYPE_INSTANT, CommonData.ALARMTYPE_INTRUSION);
                    }

                    if (_id > 0) {
                        CommonDeviceManager.getInstance(mContext).add(deviceUuid, deviceName, moduleType, 0);
//                        LogMgr.d("mExtensionStateObserver:" + "j:" + j + "  loopNum:" + loopNum);
                        if (j + 1 == loopNum) {
                            /*擴展小模塊添加成功*/
                            if (moduleType.equals(CommonData.JSON_KEY_DEVICETYPE_WIREDZONE)) {
                                LogMgr.d("ExtensionZone: add " + j + " success");
                                Intent intent = new Intent(CommonData.INTENT_ACTION_EXTENSIONMODULE_CLOUD);
                                intent.putExtra("state", "add");
                                mContext.sendBroadcast(intent);
                            }
                        }
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

            if (CommonUtils.isHejModule(loop.mType)) {
                JSONObject loopMapObject = new JSONObject();
                loopToJson(loopMapObject, loop);
                loopMapArray.put(loopMapObject);
            }
        }

        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY, loopMapArray);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, PeripheralDevice loop) throws JSONException {
        loopMapObject.put(CommonJson.JSON_UUID_KEY, loop.mModuleUuid);
        loopMapObject.put(CommonJson.JSON_ALIASNAME_KEY, loop.mName);
        loopMapObject.put(CommonData.JSON_ONLINE_KEY, loop.mOnLine + "");
        loopMapObject.put(CommonData.JSON_TYPE_KEY, loop.mType);
        loopMapObject.put(CommonData.JSON_IP_KEY, loop.mIpAddr);
        loopMapObject.put(CommonData.JSON_KEY_MAC, loop.mMacAddr);
        loopMapObject.put(CommonData.JSON_KEY_VERSION, loop.mVersion);
    }

    public void extensionModuleDelete(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            Log.d(TAG, "extensionModuleDelete: uuid:" + uuid);
            PeripheralDevice device = getByModuleUuid(uuid);
            if (null == device) {
                continue;
            }
            long num = deleteByModuleUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num, loopMapObject);
            //delete zone loop,relay loop
            //delete common device loop
            Log.d(TAG, "extensionModuleDelete: device.mType:" + device.mType);
            if (device.mType.equals(CommonData.JSON_MODULE_NAME_RELAY)) {
                List<RelayLoop> relayLoops = RelayLoopManager.getInstance(mContext).getByModuleUuid(uuid);
                if (null != relayLoops) {
                    for (int j = 0; j < relayLoops.size(); j++) {
                        String relayUuid = relayLoops.get(j).mUuid;
                        RelayLoopManager.getInstance(mContext).deleteByUuid(relayUuid);
                        CommonDeviceManager.getInstance(mContext).deleteByUuid(relayUuid);
                    }
                }
            } else if (device.mType.equals(CommonData.JSON_KEY_DEVICETYPE_WIREDZONE)) {
                List<ZoneLoop> zoneLoops = ZoneLoopManager.getInstance(mContext).getByModuleUuid(uuid);
                if (null != zoneLoops) {
                    for (int j = 0; j < zoneLoops.size(); j++) {
                        String zoneUuid = zoneLoops.get(j).mUuid;
                        ZoneLoopManager.getInstance(mContext).deleteByUuid(zoneUuid);
                        CommonDeviceManager.getInstance(mContext).deleteByUuid(zoneUuid);
                    }
                }
            }
        }
    }

    public void extensionModuleUpdate(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            String ipAddr = loopMapObject.optString(CommonData.JSON_IP_KEY, "");
            String version = loopMapObject.optString(CommonData.JSON_KEY_VERSION, "");
            String online = loopMapObject.optString(CommonData.JSON_ONLINE_KEY, "");
            PeripheralDevice device = getByModuleUuid(uuid);

            if (device != null) {
                if (!TextUtils.isEmpty(ipAddr)) {
                    device.mIpAddr = ipAddr;
                }

                if (!TextUtils.isEmpty(version)) {
                    device.mVersion = version;
                }

                if (!TextUtils.isEmpty(online)) {
                    device.mOnLine = Integer.parseInt(online);
                } else {
                    device.mOnLine = -1;//prohibit update database. so this api can be use update specific item independently without affecting others  add  by xiaochao
                }
                updateByModuleUuid(device.mModuleUuid, device, false);
                loopMapObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
                loopMapObject.put(CommonJson.JSON_UUID_KEY, device.mModuleUuid);
            }
        }
    }

    public void updateIPDCInfo(JSONObject jsonObject) throws JSONException {
        Cursor cursor = null;

        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String ipdcType = loopMapObject.optString(CommonData.JSON_IPDCTYPE_KEY, "");
            String ipdcIpAddr = loopMapObject.optString(CommonData.JSON_IP_KEY, "");
            String ipdcMacAddr = loopMapObject.optString(CommonData.JSON_KEY_MAC, "");
            String ipdcVersion = loopMapObject.optString(CommonData.JSON_KEY_VERSION, "");
            String ipdcOnline = loopMapObject.optString(CommonData.JSON_ONLINE_KEY, "");

            try {
                cursor = DbCommonUtil.getByStringField(dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, ConfigConstant.COLUMN_TYPE, ipdcType);
                if (null == cursor) {
                    continue;
                }
                while (cursor.moveToNext()) {
                    PeripheralDevice device = fillDefault(cursor);

                    if (device != null) {
                        if (!TextUtils.isEmpty(ipdcIpAddr)) {
                            device.mIpAddr = ipdcIpAddr;
                        }

                        if (!TextUtils.isEmpty(ipdcMacAddr)) {
                            device.mMacAddr = ipdcMacAddr;
                        }

                        if (!TextUtils.isEmpty(ipdcVersion)) {
                            device.mVersion = ipdcVersion;
                        }

                        updateByModuleUuid(device.mModuleUuid, device, true);

                        loopMapObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
                        loopMapObject.put(CommonJson.JSON_UUID_KEY, device.mModuleUuid);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }

        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY, jsonArray);
        jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_RESPONSE);
    }

    public void clearConnectionStatus(JSONObject jsonObject) throws JSONException {
//        String online = jsonObject.optString(CommonData.JSON_ONLINE_KEY, "");

        String[] args = new String[]{CommonData.JSON_KEY_DEVICETYPE_WIREDZONE, CommonData.JSON_MODULE_NAME_RELAY};
        Cursor cursor = DbCommonUtil.getRecordCursor(dbHelper, ConfigConstant.TABLE_PERIPHERALDEVICE, ConfigConstant.COLUMN_TYPE, args);
        while (cursor.moveToNext()) {
            Log.d(TAG, "clearConnectionStatus123");
            PeripheralDevice device = fillDefault(cursor);
            if (device != null) {
                updateBySelection(device, ConfigConstant.COLUMN_TYPE, args);
            }
        }

        cursor.close();

        jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_RESPONSE);
    }
}
