package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.configcenter.ConfigDispatchCenter;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.CommonDevice;
import com.honeywell.homepanel.configcenter.databases.domain.RelayLoop;
import com.honeywell.homepanel.configcenter.databases.domain.ZoneLoop;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by H135901 on 3/15/2017.
 */

public class DbCommonUtil {

    private static  final  String TAG = ConfigService.TAG;
    public static final int TYPELENGTH = 3;
    public static long add(Context context,ConfigDatabaseHelper dbHelper,String table,ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = -1;
        try {
            rowId =  db.insert(table, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(rowId > 0){
            onPublicConfigurationChanged(context, table);
        }
        return  rowId;
    }
    public static int deleteByStringFieldType(Context context, ConfigDatabaseHelper dbHelper, String table, String columnName, String uuid) {
        int num = -1;
        if(TextUtils.isEmpty(uuid)){
            return num;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            num = db.delete(table,columnName + "=?", new String[]{uuid});
            if(num > 0){
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        if(num > 0){
            onPublicConfigurationChanged(context, table);
        }
        return num;
    }
    public static int deleteByPrimaryId(Context context, ConfigDatabaseHelper dbHelper,String table,long primaryId) {
        int num = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            num = db.delete(table, ConfigConstant.COLUMN_ID + "=?", new String[]{String.valueOf(primaryId)});
            if(num > 0){
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        if(num > 0){
            onPublicConfigurationChanged(context, table);
        }
        return num;
    }

    public static int updateByPrimaryId(Context context, ConfigDatabaseHelper dbHelper,String table,ContentValues values,long primaryId) {
        if(primaryId <= 0){
            return -1;
        }
        int num = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            num =  db.update(table,values,ConfigConstant.COLUMN_ID + "=?", new String[]{String.valueOf(primaryId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0){
            onPublicConfigurationChanged(context, table);
        }
        return num;
    }

    public static  int updateByStringFiled(Context context, ConfigDatabaseHelper dbHelper, String table, ContentValues values, String columnName, String uuid) {
        int num = -1;
        if(TextUtils.isEmpty(uuid)){
            return num;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            num =  db.update(table,values,columnName + "=?",new String[]{uuid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0){
            onPublicConfigurationChanged(context, table);
        }
        return num;
    }

    /*
    replace value contains in column if uuid is not null, otherwise replace all values locate in column
     */
    public static int updateFieldByUUID(Context context,ConfigDatabaseHelper dbHelper, String table, ContentValues values, String uuid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int num = -1;

        try {
            if(TextUtils.isEmpty(uuid)){
                num = db.update(table,values,null,null);
            } else {
                num = db.update(table,values,ConfigConstant.COLUMN_UUID + "=?",new String[] {uuid});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(num > 0){
            onPublicConfigurationChanged(context, table);
        }

        return num;
    }

    public static  Cursor getByPrimaryId(ConfigDatabaseHelper dbHelper,String table,long primaryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(table,null, ConfigConstant.COLUMN_ID + "=?",
                new String[] { String.valueOf(primaryId) }, null, null, null, null);
        return  cursor;
    }

    public static  Cursor getByStringField(ConfigDatabaseHelper dbHelper, String table, String columnName, String selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(table,null,columnName + "=?",
                new String[] { selectionArgs }, null, null, null, null);
        return  cursor;
    }

    public static  Cursor getRecordCursor(ConfigDatabaseHelper dbHelper, String table, String columnName, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(table, null, columnName + "=? OR " + columnName + " = ?",selectionArgs, null, null, null, null);
        return  cursor;
    }


    public static  Cursor getByIntField(ConfigDatabaseHelper dbHelper, String table, String columnName, int status) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;//modify by xiaochao
        if (status == 0 || status == 1) {//0:unread 1:read
            //query unread or read record
            cursor = db.query(table,null,columnName + "=?",
                    new String[] { String.valueOf(status) }, null, null, null, null);
        } else if (status == 2) {
            //query both read and unread data
            cursor = db.query(table,null,columnName + " =?"+" or " + columnName +" =?",
                    new String[] { String.valueOf(0), String.valueOf(1) }, null, null, null, null);
        }

        return  cursor;
    }

    public static  Cursor getAll(ConfigDatabaseHelper dbHelper,String table) {
        return getAll(dbHelper, table, "asc");
    }

    public static  Cursor getAll(ConfigDatabaseHelper dbHelper,String table, String sort) {
        return get(dbHelper, table, sort, 0, -1);
    }

    public static  Cursor get(ConfigDatabaseHelper dbHelper,String table, String sort, int start, int count) {
        String limitSqlString = null;
        if (start >= 0 && count >=0) {
            limitSqlString = new StringBuilder().append(start).append(",").append(count).toString();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(table, null, null, null, null, null, ConfigConstant.COLUMN_ID + " " + sort, limitSqlString);
        return  cursor;
    }

    public static Cursor getUnreportedAlarmByType(ConfigDatabaseHelper dbHelper, String table, String type, int start, int count)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.query(table, null, type +"=?", new String[] { String.valueOf("0") }, null, null, ConfigConstant.COLUMN_ID +" asc", null);// String.valueOf(count)

        return cursor;
    }

    public static  Cursor getDistinctAll(ConfigDatabaseHelper dbHelper,String table,String column) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct " + column + " from " + table, null);
        return  cursor;
    }

    public static  long getSequenct(ConfigDatabaseHelper dbHelper,String table) {
        return getSequenct(dbHelper.getReadableDatabase(), table);
    }

    public static  long getSequenct(SQLiteDatabase db,String table) {
        Cursor cursor = db.query("sqlite_sequence",null, null, null, null, null,null, null);
        long seq = 0;
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("name")).equals(table)){
                seq = cursor.getLong(cursor.getColumnIndex("seq"));
                break;
            }
        }
        cursor.close();
        return  (seq + 1);
    }

    public static  Cursor getDeviceLoopDetails(ConfigDatabaseHelper dbHelper, String table) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder sb = new StringBuilder().append("select A1.").append(ConfigConstant.COLUMN_TYPE).append(", A2.* from ")
                .append(ConfigConstant.TABLE_COMMONDEVICE).append(" A1,").append(table).append(" A2 ")
                .append("where A1.").append(ConfigConstant.COLUMN_UUID).append(" = A2.").append(ConfigConstant.COLUMN_UUID)
                .append(";");

        Cursor cursor = db.rawQuery(sb.toString(), null);
        return  cursor;
    }

    public static String generateDeviceUuid(int tableInt, long primaryId) {
        String uuid = String.valueOf(tableInt);
        if(uuid.length() == 1){
            uuid = "00"+uuid;
        }
        else if(uuid.length() == 2){
            uuid = "0"+uuid;
        }
        uuid = uuid + primaryId;
        return uuid;
    }

    public static int getTableIntFromDeviceUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return -1;
        }
        String moduleTypeStr = uuid.substring(0,TYPELENGTH);
        int moduleType =  Integer.valueOf(moduleTypeStr);
        return moduleType;
    }

    public static long getPrimaryIdFromDeviceUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return -1;
        }
        String idStr = uuid.substring(TYPELENGTH);
        return Long.valueOf(idStr);
    }

    public static void putKeyValueToJson(Context mContext, String deviceUuid, JSONObject jsonObject) throws JSONException {
        int tableInt = getTableIntFromDeviceUuid(deviceUuid);
        long primaryId = getPrimaryIdFromDeviceUuid(deviceUuid);
        switch (tableInt){
            case ConfigConstant.TABLE_ZONELOOP_INT:
                ZoneLoop zoneLoop = ZoneLoopManager.getInstance(mContext).getByPrimaryId(primaryId);
                if(null == zoneLoop){
                    break;
                }
                jsonObject.put(CommonJson.JSON_UUID_KEY,zoneLoop.mUuid);
                jsonObject.put(CommonJson.JSON_ALIASNAME_KEY,zoneLoop.mName);
                jsonObject.put(CommonData.JSON_KEY_ZONETYPE,zoneLoop.mZoneType);
                jsonObject.put(CommonData.JSON_KEY_ALARMTYPE,zoneLoop.mAlarmType);
                jsonObject.put(CommonData.JSON_KEY_ENABLE,zoneLoop.mEnabled+"");
                jsonObject.put(CommonData.JSON_KEY_DELAYTIME,zoneLoop.mDelayTime+"");
                break;
            case ConfigConstant.TABLE_RELAYLOOP_INT:
                RelayLoop relayLoop = RelayLoopManager.getInstance(mContext).getByPrimaryId(primaryId);
                if(null == relayLoop){
                    break;
                }
                jsonObject.put(CommonJson.JSON_UUID_KEY,relayLoop.mUuid);
                jsonObject.put(CommonJson.JSON_ALIASNAME_KEY,relayLoop.mName);
                jsonObject.put(CommonData.JSON_KEY_LOOP,relayLoop.mLoop);
                jsonObject.put(CommonData.JSON_KEY_ENABLE,relayLoop.mEnabled+"");
                jsonObject.put(CommonData.JSON_KEY_DELAYTIME,relayLoop.mDelayTime+"");
                break;
            default:
                break;
        }
    }

    public static void putErrorCodeFromOperate(long numOrRowid,JSONObject jsonObject) throws JSONException {
        String code = CommonJson.JSON_ERRORCODE_VALUE_FAIL;
        if(numOrRowid > 0){
            code = CommonJson.JSON_ERRORCODE_VALUE_OK;
        }
        else if(numOrRowid == Long.valueOf(CommonJson.JSON_ERRORCODE_VALUE_EXIST)){
            code = CommonJson.JSON_ERRORCODE_VALUE_EXIST;
        }
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY,code);
    }

    public static  long getCount(SQLiteDatabase db,String tableName){
        if(null == db || TextUtils.isEmpty(tableName)){
            return -1;
        }
        Cursor cursor = db.rawQuery("select count(*) from " + tableName, null);
        long result = 0;
        if (null != cursor) {
            cursor.moveToFirst();
            result = cursor.getLong(0);
            cursor.close();
        }
        Log.d(TAG, "getCount: result:"+result);
        return result;
    }

    public static long getFirstRecord(ConfigDatabaseHelper dbHelper,String tableName) {
        long _id = -1;
        if(null == dbHelper || TextUtils.isEmpty(tableName)){
            return  _id;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, ConfigConstant.COLUMN_ID +" asc", null);
        while(cursor.moveToNext()){
            _id = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
            break;
        }
        cursor.close();
        Log.d(TAG, "getFirstRecord() first _id:"+ _id+",,,11111");
        return _id;
    }

    public static void deleteExceedLimiteRecords(ConfigDatabaseHelper dbHelper,String tableName, int limit) {
        // DELETE FROM %1 WHERE %2 IN (SELECT %2 FROM %1 ORDER BY %2 DESC LIMIT %3,%4);
        String whereClause = new StringBuilder().append("delete from ").append(tableName)
                .append(" where ").append(ConfigConstant.COLUMN_ID).append(" in ")
                    .append("( select ").append(ConfigConstant.COLUMN_ID)
                    .append(" from ").append(tableName)
                    .append(" order by ").append(ConfigConstant.COLUMN_ID)
                    .append(" desc limit ").append(limit).append(", 50")
                .append(");").toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(whereClause);
    }


    public  static  String transferReadIntToString(int read){
        String status = CommonData.DATASTATUS_UNREAD;
        if(read == 1){
            status = CommonData.DATASTATUS_READ;
        }
        return status;
    }

    public  static  int transferReadStringToInt(String dataStatus){
        int status = 0;
        if(dataStatus.equals(CommonData.DATASTATUS_READ)){
            status = 1;
        } else if (dataStatus.equals(CommonData.DATASTATUS_ALL)) {// add by xiaochao
            status = 2;
        }
        return status;
    }

    public  static  int transferUploadStatusToInt(String alarmStatus){
        int status = 0;
        if(alarmStatus.equals(CommonData.ALARMREPORTSTATUS_REPORTED)){
            status = 1;
        } else if (alarmStatus.equals(CommonData.ALARMREPORTSTATUS_UNREPORTED)) {// add by xiaochao
            status = 0;
        }
        return status;
    }

    public static void updateCommonName(Context context,String uuid,String name, int enable) {
        CommonDevice commonDevice = CommonDeviceManager.getInstance(context).getByUuid(uuid);
        if(null == commonDevice){
            return;
        }
        commonDevice.mName = name;
        commonDevice.mEnabled = enable;

        CommonDeviceManager.getInstance(context).updateByUuid(uuid, commonDevice);
    }

    public static void onPublicConfigurationChanged(Context context, String table){
        PreferenceManager.updateVersionId(context);

        if (!TextUtils.isEmpty(table)) {
            ArrayList<String> configContent = convertTableNameToConfigTitle(table);

            for (int index = 0; index < configContent.size(); index ++) {
                ConfigDispatchCenter.getInstance().broadcastConfigurationUpdated(CommonData.JSON_CONFIGDATA_CATEGORY_PUBLIC,
                        configContent.get(index));
            }
        }
    }

    private static ArrayList<String> convertTableNameToConfigTitle(String dbTable) {
        ArrayList<String> names = new ArrayList<String>();

        if ( ConfigConstant.TABLE_LOCALDEVICE.equals(dbTable)
                || ConfigConstant.TABLE_DEVICEADAPTER.equals(dbTable)
                || ConfigConstant.TABLE_PERIPHERALDEVICE.equals(dbTable)
                || ConfigConstant.TABLE_RELAYLOOP.equals(dbTable)
                || ConfigConstant.TABLE_ZONELOOP.equals(dbTable)) {
            names.add(CommonData.JSON_CONFIGDATA_CONFIGNAME_COMMONDEVLIST);
        }

        return names;
    }
}
